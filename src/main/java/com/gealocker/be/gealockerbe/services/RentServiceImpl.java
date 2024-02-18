package com.gealocker.be.gealockerbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gealocker.be.gealockerbe.dao.LockerDao;
import com.gealocker.be.gealockerbe.dao.UserDao;
import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.Locker;
import com.gealocker.be.gealockerbe.entity.User;
import com.gealocker.be.gealockerbe.services.RentService;

@Service
public class RentServiceImpl implements RentService {
    @Autowired
    private LockerDao lockerDAO;

    @Override
    public ApiResponse rent(Locker locker) {
        ApiResponse obj = lockerDAO.rent(locker);
        return obj;
    }

    @Override
    public ApiResponse pengembalian(Locker locker) {
        ApiResponse obj = lockerDAO.pengembalian(locker);
        return obj;
    }

    @Override
    public ApiResponse validatePasswordLocker(Locker locker) {
        ApiResponse obj = lockerDAO.validatePasswordLocker(locker);
        return obj;
    }

    @Override
    public ApiResponse lihatData(User user) {
        ApiResponse obj = lockerDAO.lihatData(user);
        return obj;
    }

    @Override
    public ApiResponse lihatDataOngoing(User user) {
        ApiResponse obj = lockerDAO.lihatData(user);
        return obj;
    }

    @Override
    public ApiResponse lihatAvailable() {
        ApiResponse obj = lockerDAO.lihatAvailable();
        return obj;
    }

    // @Override
    // public ApiResponse insertUser(User user) {
    // ApiResponse obj = userDAO.insertUser(user);
    // return obj;
    // }
}