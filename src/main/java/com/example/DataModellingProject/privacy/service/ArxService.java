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

    public void applyArxAnonymization(Collection<?> items, TableRule rule) throws Exception {
        if (items == null || items.isEmpty() || rule.getAnonymization() == null) return;

        AnonymizationRule anonRule = rule.getAnonymization();
        List<Object> itemList = new ArrayList<>(items);
        Class<?> clazz = itemList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // 1. Initialize ARX Data Structure
        Data.DefaultData data = Data.create();

        // Add Headers
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName();
        }
        data.add(headers);

        // Add Rows (Convert Java objects to String arrays)
        for (Object item : itemList) {
            String[] row = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object val = fields[i].get(item);
                row[i] = (val != null) ? val.toString() : "";
            }
            data.add(row);
        }

        // 2. Configure Attribute Types (IDENTIFYING, SENSITIVE, etc.)
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
                    // 1. Gather all unique values currently in this column
                    java.util.Set<String> uniqueValues = new java.util.HashSet<>();
                    for (Object item : itemList) {
                        try {
                            java.lang.reflect.Field field = item.getClass().getDeclaredField(colName);
                            field.setAccessible(true);
                            Object val = field.get(item);
                            uniqueValues.add(val != null ? val.toString() : "");
                        } catch (Exception e) {
                            uniqueValues.add("");
                        }
                    }

                    // 2. Build the programmatic hierarchy and assign it to the column
                    AttributeType.Hierarchy dynamicHierarchy = ArxHierarchyUtil.createRedactionHierarchy(uniqueValues);
                    data.getDefinition().setAttributeType(colName, dynamicHierarchy);
                    break;
                default:
                    data.getDefinition().setAttributeType(colName, AttributeType.INSENSITIVE_ATTRIBUTE);
            }
        }
        
        // 3. Configure Privacy Models (K-Anonymity, L-Diversity, T-Closeness)
        ARXConfiguration config = ARXConfiguration.create();
        config.setSuppressionLimit(1d); // Allow up to 100% suppression if the rules are extremely strict

        // Add K-Anonymity
        if (anonRule.getKAnonymity() != null) {
            config.addPrivacyModel(new KAnonymity(anonRule.getKAnonymity()));
        }

        // Add Distinct L-Diversity
        for (AnonymizationRule.LDiversity lDiv : anonRule.getLDiversities()) {
            config.addPrivacyModel(new org.deidentifier.arx.criteria.DistinctLDiversity(lDiv.getColumn(), lDiv.getL()));
        }

        // Add Equal Distance T-Closeness
        for (AnonymizationRule.TCloseness tClose : anonRule.getTClosenesses()) {
            config.addPrivacyModel(new org.deidentifier.arx.criteria.EqualDistanceTCloseness(tClose.getColumn(), tClose.getT()));
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
                fields[i].setAccessible(true);
                // Only overwrite Strings for simplicity in this example.
                // You would need type-casting logic here for Integers, Dates, etc.
                if (fields[i].getType().equals(String.class)) {
                    fields[i].set(item, safeRow[i]);
                }
            }
        }
    }
}