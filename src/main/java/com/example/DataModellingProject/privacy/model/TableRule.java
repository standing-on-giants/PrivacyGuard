package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableRule {
    private String tableName;
    private RowFilter rowFilter;
    private List<String> maskColumns = new ArrayList<>();
    private List<String> allowOnlyColumns = new ArrayList<>();
    private AnonymizationRule anonymization;
}