package com.example.DataModellingProject.privacy.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ListValue {
    // For handling the <ListValue> arrays when using the "in" operator
    private List<String> stringValues = new ArrayList<>();
    private List<Integer> integerValues = new ArrayList<>();
}
