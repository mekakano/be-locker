package com.gealocker.be.gealockerbe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    @JsonProperty("id_user")
    private int id_user;

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("email")
    private String email;

    @JsonProperty("no_ktp")
    private String no_ktp;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("isOpen")
    private boolean isOpen;

    @JsonProperty("isClosed")
    private boolean isClosed;

}
