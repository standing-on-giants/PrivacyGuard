package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class RuleSet {
    private String role;
    private Map<String, TableRule> tableRules = new HashMap<>();
}