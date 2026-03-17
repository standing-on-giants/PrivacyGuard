package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AnonymizationRule {
    private Map<String, String> attributes = new HashMap<>();

    private Integer kAnonymity;
    private List<LDiversity> lDiversities = new ArrayList<>();
    private List<TCloseness> tClosenesses = new ArrayList<>();

    @Data
    public static class LDiversity {
        private String column;
        private int l;
    }

    @Data
    public static class TCloseness {
        private String column;
        private double t;
    }
}