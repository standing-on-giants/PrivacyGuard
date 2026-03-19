package com.example.DataModellingProject.privacy.service;

import com.example.DataModellingProject.privacy.model.AnonymizationRule;
import com.example.DataModellingProject.privacy.model.TableRule;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
public class ArxService {

    /**
     * Helper to resolve the database column name from a Java field.
     * Checks for @PrivacyField (DTOs), then @Column (Entities), then defaults to field name.
     */
    private String getEffectiveColumnName(Field field) {
        // 1. Check for custom @PrivacyField
        com.example.DataModellingProject.privacy.annotation.PrivacyField privacyField = 
                field.getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyField.class);
        if (privacyField != null && privacyField.column() != null && !privacyField.column().isEmpty()) {
            return privacyField.column();
        }

        // 2. Check for JPA @Column
        jakarta.persistence.Column columnAnnotation = field.getAnnotation(jakarta.persistence.Column.class);
        if (columnAnnotation != null && columnAnnotation.name() != null && !columnAnnotation.name().isEmpty()) {
            return columnAnnotation.name();
        }

        // 3. Fallback to raw Java field name
        return field.getName();
    }

    public void applyArxAnonymization(Collection<?> items, TableRule rule) throws Exception {
        if (items == null || items.isEmpty() || rule.getAnonymization() == null) return;

        AnonymizationRule anonRule = rule.getAnonymization();
        List<Object> itemList = new ArrayList<>(items);
        Class<?> clazz = itemList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // 1. Initialize ARX Data Structure
        Data.DefaultData data = Data.create();

        // Add Headers mapped to true Database Columns to match XML
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = getEffectiveColumnName(fields[i]);
        }
        data.add(headers);

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

        // 2. Configure Attribute Types based on mapped XML columns
        for (int i = 0; i < headers.length; i++) {
            String colName = headers[i];
            String type = anonRule.getAttributes().getOrDefault(colName, "INSENSITIVE");

            switch (type.toUpperCase()) {
                case "IDENTIFYING":
                    data.getDefinition().setAttributeType(colName, AttributeType.IDENTIFYING_ATTRIBUTE);
                    break;
                case "SENSITIVE":
                    data.getDefinition().setAttributeType(colName, AttributeType.SENSITIVE_ATTRIBUTE);
                    break;
                case "QUASI_IDENTIFYING":
                    java.util.Set<String> uniqueValues = new java.util.HashSet<>();
                    // Access field safely by index rather than string name
                    Field targetField = fields[i];
                    for (Object item : itemList) {
                        try {
                            targetField.setAccessible(true);
                            Object val = targetField.get(item);
                            uniqueValues.add(val != null ? val.toString() : "");
                        } catch (Exception e) {
                            uniqueValues.add("");
                        }
                    }

                    AttributeType.Hierarchy dynamicHierarchy = ArxHierarchyUtil.createRedactionHierarchy(uniqueValues);
                    data.getDefinition().setAttributeType(colName, dynamicHierarchy);
                    break;
                default:
                    data.getDefinition().setAttributeType(colName, AttributeType.INSENSITIVE_ATTRIBUTE);
            }
        }
        
        // 3. Configure Privacy Models
        ARXConfiguration config = ARXConfiguration.create();
        config.setSuppressionLimit(1d); 

        if (anonRule.getKAnonymity() != null) {
            config.addPrivacyModel(new KAnonymity(anonRule.getKAnonymity()));
        }

        if (anonRule.getLDiversities() != null) {
            for (AnonymizationRule.LDiversity lDiv : anonRule.getLDiversities()) {
                config.addPrivacyModel(new org.deidentifier.arx.criteria.DistinctLDiversity(lDiv.getColumn(), lDiv.getL()));
            }
        }

        if (anonRule.getTClosenesses() != null) {
            for (AnonymizationRule.TCloseness tClose : anonRule.getTClosenesses()) {
                config.addPrivacyModel(new org.deidentifier.arx.criteria.EqualDistanceTCloseness(tClose.getColumn(), tClose.getT()));
            }
        }

        // 4. Run the Anonymizer
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXResult result = anonymizer.anonymize(data, config);

        if (!result.isResultAvailable()) {
            throw new RuntimeException("ARX could not find a solution to anonymize the data with the given rules.");
        }

        // 5. Map the safe data back into your Java Objects
        DataHandle output = result.getOutput(false);
        Iterator<String[]> iterator = output.iterator();
        iterator.next(); // Skip the header row

        for (Object item : itemList) {
            String[] safeRow = iterator.next();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                
                String safeValue = safeRow[i];
                
                // If ARX suppressed the value, it outputs "*". We map that to null for non-strings.
                boolean isSuppressed = (safeValue == null || safeValue.equals("*") || safeValue.isEmpty());

                // Safe parsing to prevent crashes, covering Strings, Integers, Dates, and Times
                if (field.getType().equals(String.class)) {
                    field.set(item, safeValue);
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