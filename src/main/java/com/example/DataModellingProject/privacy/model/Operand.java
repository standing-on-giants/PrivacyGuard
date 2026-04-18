package com.example.DataModellingProject.privacy.model;

import lombok.Data;

@Data
public class Operand {
    // Only ONE of these will be populated per operand
    private String column;
    private String contextAttribute;
    private String stringValue;
    private Integer integerValue;
    private Double doubleValue;
    private ListValue listValue;

    // To handle the self-closing <Null/> tag.
    // If <Null/> is present in XML, your parser usually instantiates this as an empty Object/String.
    private String nullValue;

    /**
     * Helper method to easily extract whatever value was actually provided.
     */
    public Object getActualValue() {
        if (column != null) return column;
        if (contextAttribute != null) return contextAttribute;
        if (stringValue != null) return stringValue;
        if (integerValue != null) return integerValue;
        if (doubleValue != null) return doubleValue;
        if (listValue != null) return listValue;
        return null;
    }
}
