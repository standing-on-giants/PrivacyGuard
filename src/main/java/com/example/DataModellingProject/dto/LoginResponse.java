package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private boolean success;
    private String message;
    private Integer referenceId;
    private String token;
}