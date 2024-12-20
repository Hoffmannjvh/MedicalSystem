package com.medicalsystem.medicalapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorsResponse {

    private List<String> errors;

    public ErrorsResponse(List<String> errors) {
        this.errors = errors;
    }

}
