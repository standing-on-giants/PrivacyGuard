package com.example.DataModellingProject.privacy.model;

import lombok.Data;

@Data
public class RowFilter {
    // Only ONE of these will be populated by the XML parser
    private Condition condition;
    private Logic logic;

    // Quick helper to check if this filter is empty
    public boolean isEmpty() {
        return condition == null && logic == null;
    }
}