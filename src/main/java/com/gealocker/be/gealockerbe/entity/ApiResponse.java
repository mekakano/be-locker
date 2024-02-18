package com.gealocker.be.gealockerbe.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ApiResponse {
    private int statuscode;
    private String message;
    private Object object;

  }
