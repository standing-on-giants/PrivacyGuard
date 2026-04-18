package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Logic {
    private String operator; // "AND" or "OR"

    // Jackson/JAXB will automatically append to these lists as it reads the XML
    private List<Condition> conditions = new ArrayList<>();
    private List<Logic> logics = new ArrayList<>();
}
