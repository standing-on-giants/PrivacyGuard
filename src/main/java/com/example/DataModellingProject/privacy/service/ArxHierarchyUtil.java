package com.example.DataModellingProject.privacy.service;

import org.deidentifier.arx.AttributeType;
import java.util.Set;

public class ArxHierarchyUtil {

    /**
     * Creates a redaction hierarchy that masks strings from right to left.
     * Example: "Male" -> "Mal*" -> "Ma**" -> "M***" -> "****"
     */
    public static AttributeType.Hierarchy createRedactionHierarchy(Set<String> uniqueValues) {

        // 1. Find the longest string so we know how many "blur levels" we need
        int maxLength = 0;
        for (String val : uniqueValues) {
            if (val != null && val.length() > maxLength) {
                maxLength = val.length();
            }
        }
        if (maxLength == 0) maxLength = 1;

        // 2. Initialize the 2D Array: [Number of unique items][Number of blur levels + raw level]
        String[][] hierarchyArray = new String[uniqueValues.size()][maxLength + 1];

        // 3. Fill the array
        int rowIndex = 0;
        for (String val : uniqueValues) {
            String safeVal = (val == null) ? "" : val;

            // Level 0 is always the raw, untouched value
            hierarchyArray[rowIndex][0] = safeVal;

            // Generate the blurred versions
            for (int level = 1; level <= maxLength; level++) {
                if (safeVal.length() <= level) {
                    // If the string is shorter than the blur level, mask the whole thing
                    hierarchyArray[rowIndex][level] = "*".repeat(safeVal.length() > 0 ? safeVal.length() : 1);
                } else {
                    // Keep the left part, mask the right part
                    String visiblePart = safeVal.substring(0, safeVal.length() - level);
                    String maskedPart = "*".repeat(level);
                    hierarchyArray[rowIndex][level] = visiblePart + maskedPart;
                }
            }
            rowIndex++;
        }

        return AttributeType.Hierarchy.create(hierarchyArray);
    }
}