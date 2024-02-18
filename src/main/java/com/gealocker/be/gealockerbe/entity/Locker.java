package com.gealocker.be.gealockerbe.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Locker {
    @JsonProperty("id_user")
    private int id_user;

    @JsonProperty("id_locker")
    private int id_locker;

    @JsonProperty("nomor_locker")
    private String nomor_locker;

    @JsonProperty("tanggal_peminjaman")
    private Timestamp tanggal_peminjaman;

    @JsonProperty("tanggal_pengembalian")
    private Timestamp tanggal_pengembalian;

    @JsonProperty("created_at")
    private Timestamp created_at;

    @JsonProperty("updated_at")
    private Timestamp updated_at;

    @JsonProperty("deposit")
    private int deposit;

    @JsonProperty("denda")
    private int denda;

    @JsonProperty("password_locker")
    private String password_locker;

    @JsonProperty("attemp_password")
    private int attemp_password;

    @JsonProperty("isopen")
    private boolean isopen;

    @JsonProperty("isclosed")
    private boolean isclosed;
}
