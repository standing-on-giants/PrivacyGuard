package com.example.DataModellingProject.privacy.model;

import lombok.Data;

@Data
public class Condition {
    private String operator; // "eq", "gt", "in", "contains", etc.
    private Operand leftOperand;
    private Operand rightOperand;
}
