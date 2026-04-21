package com.example.DataModellingProject.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequestScope
public class UserContext {

    @Setter
    @Getter
    private String userEmail;

    @Setter
    @Getter
    private String role;

    @Setter
    @Getter
    private String databaseId; // The actual ID (e.g., doct_id)

    @Setter
    @Getter
    private Instant requestTimestamp;

    private final Map<String, Object> customAttributes = new HashMap<>();

    public void addAttribute(String key, Object value) {
        customAttributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return customAttributes.get(key);
    }

    public boolean isAuthenticated() {
        return databaseId != null;
    }
}