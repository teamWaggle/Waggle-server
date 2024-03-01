package com.example.waggle.global.payload;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleHolder {

    private Example holder;
    private String name;
    private int code;

}