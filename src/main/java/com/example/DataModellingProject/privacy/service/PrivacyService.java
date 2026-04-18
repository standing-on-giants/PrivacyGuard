package com.example.DataModellingProject.privacy.service;

import com.example.DataModellingProject.privacy.model.PrivacyConfig;
import com.example.DataModellingProject.privacy.model.TableRule;
import com.example.DataModellingProject.privacy.parser.PrivacyRuleParser;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class PrivacyService {

    private final PrivacyRuleParser parser;
    private PrivacyConfig privacyConfig;

    public PrivacyService(PrivacyRuleParser parser) {
        this.parser = parser;
    }

    @PostConstruct
    public void init() {
        try {
            // Looks for "privacy-rules.xml" inside src/main/resources folder
            ClassPathResource resource = new ClassPathResource("privacy-rules.xml");
            try (InputStream is = resource.getInputStream()) {
                this.privacyConfig = parser.parse(is);
                System.out.println("Privacy Rules loaded successfully into memory!");
            }
        } catch (Exception e) {
            System.err.println("Failed to load Privacy Rules: " + e.getMessage());
            e.printStackTrace();
            // throw new RuntimeException("Failed to load Privacy Rules");
        }
    }

    public PrivacyConfig getConfig() {
        return this.privacyConfig;
    }

    public TableRule getTableRule(String role, String tableName) {
        if (privacyConfig == null || !privacyConfig.getRuleSets().containsKey(role)) {
            System.out.println("Privacy Rules not found!");
            return null;
        }
        return privacyConfig.getRuleSets().get(role).getTableRules().get(tableName);
    }

}