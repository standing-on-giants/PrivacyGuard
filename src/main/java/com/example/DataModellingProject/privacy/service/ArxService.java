package com.example.DataModellingProject.privacy.service;

import com.example.DataModellingProject.privacy.model.AnonymizationRule;
import com.example.DataModellingProject.privacy.model.TableRule;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.PrivacyCriterion;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ArxService {

    private final PrivacyService privacyService;

    public ArxService(@Lazy PrivacyService privacyService) {
        this.privacyService = privacyService;
    }

    private AttributeType.Hierarchy buildDynamicHierarchy(java.util.Set<String> uniqueValues, Class<?> fieldType) {
        String[][] hierarchyData = new String[uniqueValues.size()][4]; // 4 Levels of depth
        int index = 0;

        for (String val : uniqueValues) {
            if (val == null || val.trim().isEmpty()) {
                hierarchyData[index] = new String[]{"", "*", "*", "*"};
                index++;
                continue;
            }

            // Level 0 is always the exact raw value
            hierarchyData[index][0] = val;

            try {
                if (fieldType.equals(Integer.class) || fieldType.equals(int.class) || fieldType.equals(Long.class)) {
                    // NUMERIC STRATEGY: Bucket into ranges (e.g., 10s and 50s)
                    long num = Long.parseLong(val);
                    long lower10 = (num / 10) * 10;
                    long lower50 = (num / 50) * 50;

                    hierarchyData[index][1] = "[" + lower10 + "-" + (lower10 + 10) + ")";
                    hierarchyData[index][2] = "[" + lower50 + "-" + (lower50 + 50) + ")";

                } else if (fieldType.equals(java.time.LocalDate.class)) {
                    // DATE STRATEGY: Generalize mathematically, but pad the string
                    // to maintain strict ISO-8601 (YYYY-MM-DD) format for the parser.
                    String[] parts = val.split("-");
                    if (parts.length >= 3) {
                        hierarchyData[index][1] = parts[0] + "-" + parts[1] + "-01";
                        hierarchyData[index][2] = parts[0] + "-01-01";
                    } else {
                        hierarchyData[index][1] = "*";
                        hierarchyData[index][2] = "*";
                    }

                } else {
                    // STRING STRATEGY: Character masking from right to left (Redaction)
                    // Good for Zip codes, Phone numbers, IDs.
                    if (val.length() > 4) {
                        hierarchyData[index][1] = val.substring(0, val.length() - 2) + "**";
                        hierarchyData[index][2] = val.substring(0, val.length() - 4) + "****";
                    } else if (val.length() > 2) {
                        hierarchyData[index][1] = val.substring(0, val.length() - 1) + "*";
                        hierarchyData[index][2] = val.substring(0, 1) + "**";
                    } else {
                        hierarchyData[index][1] = "*";
                        hierarchyData[index][2] = "*";
                    }
                }
            } catch (Exception e) {
                // Fallback if parsing fails
                hierarchyData[index][1] = "*";
                hierarchyData[index][2] = "*";
            }

            // Level 3 is always total suppression
            hierarchyData[index][3] = "*";
            index++;
        }

        return AttributeType.Hierarchy.create(hierarchyData);
    }

    public void applyArxAnonymization(Collection<?> items, String role, String primaryTableName) throws Exception {
        if (items == null || items.isEmpty()) return;

        TableRule primaryRule = privacyService.getTableRule(role, primaryTableName);
        if (primaryRule == null || primaryRule.getAnonymization() == null) return;

        List<Object> itemList = new ArrayList<>(items);
        Class<?> clazz = itemList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        Data.DefaultData data = Data.create();

        // Track dynamic cross-table privacy models
        int globalMaxK = primaryRule.getAnonymization().getKAnonymity() != null ? primaryRule.getAnonymization().getKAnonymity() : 0;
        List<PrivacyCriterion> dynamicColumnModels = new ArrayList<>();

        String[] arxUniqueHeaders = new String[fields.length];
        String[] columnTypes = new String[fields.length];

        //  Setup Headers, Types, and DYNAMIC PRIVACY MODELS
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            String targetTable = primaryTableName;
            String targetColumn = field.getName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();

            com.example.DataModellingProject.privacy.annotation.PrivacyField privacyField =
                    field.getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyField.class);

            if (privacyField != null) {
                if (!privacyField.table().isEmpty()) targetTable = privacyField.table();
                if (!privacyField.column().isEmpty()) targetColumn = privacyField.column();
            }

            String uniqueArxHeader = targetTable + "_" + targetColumn;
            arxUniqueHeaders[i] = uniqueArxHeader;

            TableRule specificTableRule = privacyService.getTableRule(role, targetTable);
            String type = "INSENSITIVE";

            if (specificTableRule != null && specificTableRule.getAnonymization() != null) {
                AnonymizationRule specificAnon = specificTableRule.getAnonymization();
                type = specificAnon.getAttributes().getOrDefault(targetColumn, "INSENSITIVE");

                // Find the strictest K-Anonymity
                if (specificAnon.getKAnonymity() != null) {
                    globalMaxK = Math.max(globalMaxK, specificAnon.getKAnonymity());
                }

                // Attach L-Diversity for this specific cross-table column
                if (specificAnon.getLDiversities() != null) {
                    for (AnonymizationRule.LDiversity lDiv : specificAnon.getLDiversities()) {
                        if (lDiv.getColumn().equalsIgnoreCase(targetColumn)) {
                            dynamicColumnModels.add(new org.deidentifier.arx.criteria.DistinctLDiversity(uniqueArxHeader, lDiv.getL()));

                            globalMaxK = Math.max(globalMaxK, lDiv.getL());
                        }
                    }
                }

                // Attach T-Closeness for this specific cross-table column
                if (specificAnon.getTClosenesses() != null) {
                    for (AnonymizationRule.TCloseness tClose : specificAnon.getTClosenesses()) {
                        if (tClose.getColumn().equalsIgnoreCase(targetColumn)) {
                            dynamicColumnModels.add(new org.deidentifier.arx.criteria.EqualDistanceTCloseness(uniqueArxHeader, tClose.getT()));
                        }
                    }
                }
            }
            columnTypes[i] = type;
        }

        data.add(arxUniqueHeaders);

        // Add Rows
        for (Object item : itemList) {
            String[] row = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object val = fields[i].get(item);
                row[i] = (val != null) ? val.toString() : "";
            }
            data.add(row);
        }

        // Configure Attribute Types
        for (int i = 0; i < arxUniqueHeaders.length; i++) {
            String uniqueColName = arxUniqueHeaders[i];
            String type = columnTypes[i];

            switch (type.toUpperCase().trim()) {
                case "IDENTIFYING":
                    data.getDefinition().setAttributeType(uniqueColName, AttributeType.IDENTIFYING_ATTRIBUTE);
                    break;
                case "SENSITIVE":
                    data.getDefinition().setAttributeType(uniqueColName, AttributeType.SENSITIVE_ATTRIBUTE);
                    break;
                case "QUASI_IDENTIFYING":
                    java.util.Set<String> uniqueValues = new java.util.HashSet<>();
                    Field targetField = fields[i];

                    // Extract values
                    for (Object item : itemList) {
                        try {
                            targetField.setAccessible(true);
                            Object val = targetField.get(item);
                            uniqueValues.add(val != null ? val.toString() : "");
                        } catch (Exception e) {
                            uniqueValues.add("");
                        }
                    }

                    // Use the Smart Factory, passing the values AND the Field Type
                    AttributeType.Hierarchy dynamicHierarchy = buildDynamicHierarchy(uniqueValues, targetField.getType());
                    data.getDefinition().setAttributeType(uniqueColName, dynamicHierarchy);
                    break;
                default:
                    data.getDefinition().setAttributeType(uniqueColName, AttributeType.INSENSITIVE_ATTRIBUTE);
            }
        }

        // Configure Privacy Models (Using Aggregated Math)
        ARXConfiguration config = ARXConfiguration.create();
        config.setSuppressionLimit(1d);

        // Apply strictest global K
        if (globalMaxK > 0) {
            config.addPrivacyModel(new KAnonymity(globalMaxK));
        }

        // Apply dynamically collected cross-table models
        for (PrivacyCriterion criterion : dynamicColumnModels) {
            config.addPrivacyModel(criterion);
        }

        // If the XML had NO privacy models at all, force k=1 so the engine doesn't crash.
        if (config.getPrivacyModels().isEmpty()) {
            config.addPrivacyModel(new KAnonymity(1));
        }

        // Run the Anonymizer
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXResult result = anonymizer.anonymize(data, config);

        if (!result.isResultAvailable()) {
            throw new RuntimeException("ARX could not find a solution to anonymize the data with the given rules.");
        }

        // Map the safe data back into your Java Objects
        DataHandle output = result.getOutput(false);
        int numRows = output.getNumRows();

        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            Object item = itemList.get(rowIndex); // row 0 → item 0, row 1 → item 1...

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                String uniqueColName = arxUniqueHeaders[i];
                int arxColIndex = output.getColumnIndexOf(uniqueColName);

                String safeValue = "*";
                if (arxColIndex != -1) {
                    safeValue = output.getValue(rowIndex, arxColIndex);
                }

                boolean isSuppressed = (safeValue == null || safeValue.equals("*") || safeValue.isEmpty());

                if (field.getType().equals(String.class)) {
                    field.set(item, isSuppressed ? "*" : safeValue);
                } else if (field.getType().equals(Integer.class)) {
                    field.set(item, isSuppressed ? null : Integer.valueOf(safeValue));
                } else if (field.getType().equals(java.time.LocalDate.class)) {
                    field.set(item, isSuppressed ? null : java.time.LocalDate.parse(safeValue));
                } else if (field.getType().equals(java.time.LocalTime.class)) {
                    field.set(item, isSuppressed ? null : java.time.LocalTime.parse(safeValue));
                }
            }
        }
    }
}