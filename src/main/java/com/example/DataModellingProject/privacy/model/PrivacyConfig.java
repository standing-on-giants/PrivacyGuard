package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class PrivacyConfig {
    private Map<String, RuleSet> ruleSets = new HashMap<>();
}