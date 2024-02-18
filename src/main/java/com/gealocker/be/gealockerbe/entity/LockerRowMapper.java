package com.gealocker.be.gealockerbe.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LockerRowMapper implements RowMapper<Locker> {
    @Override
    public Locker mapRow(ResultSet row, int rowNum) throws SQLException {
        Locker locker = new Locker();
        locker.setId_locker(row.getInt("id_locker"));
        locker.setDenda(row.getInt("denda"));
        locker.setDeposit(row.getInt("deposit"));
        locker.setNomor_locker(row.getString("nomor_locker"));
        locker.setPassword_locker(row.getString("password_locker"));
        locker.setTanggal_peminjaman(row.getTimestamp("tanggal_peminjaman"));
        locker.setTanggal_pengembalian(row.getTimestamp("tanggal_pengembalian"));
        locker.setCreated_at(row.getTimestamp("created_at"));
        locker.setUpdated_at(row.getTimestamp("updated_at"));

        return locker;
    }
}