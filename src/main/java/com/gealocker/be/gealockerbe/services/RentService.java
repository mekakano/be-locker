package com.gealocker.be.gealockerbe.services;

import org.springframework.stereotype.Service;

import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.Locker;
import com.gealocker.be.gealockerbe.entity.User;

@Service
public interface RentService {
    ApiResponse rent(Locker locker);

    ApiResponse pengembalian(Locker locker);

    ApiResponse validatePasswordLocker(Locker locker);

    ApiResponse lihatData(User user);

    ApiResponse lihatDataOngoing(User user);

    ApiResponse lihatAvailable();
}
