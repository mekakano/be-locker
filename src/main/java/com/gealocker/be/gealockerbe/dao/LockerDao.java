package com.gealocker.be.gealockerbe.dao;

import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.Locker;
import com.gealocker.be.gealockerbe.entity.User;

import java.util.List;

public interface LockerDao {
    ApiResponse rent(Locker locker);

    ApiResponse pengembalian(Locker locker);

    ApiResponse validatePasswordLocker(Locker locker);

    ApiResponse lihatData(User user);

    ApiResponse lihatAvailable();

}