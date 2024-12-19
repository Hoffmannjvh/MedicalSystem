package com.medicalsystem.medicalapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErroResponse {

    private List<String> errors;

    public ErroResponse(List<String> errors) {
        this.errors = errors;
    }

}
