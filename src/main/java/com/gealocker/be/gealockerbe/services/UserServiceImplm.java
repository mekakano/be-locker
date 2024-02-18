package com.gealocker.be.gealockerbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gealocker.be.gealockerbe.dao.UserDao;
import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.User;
import com.gealocker.be.gealockerbe.services.UserService;

@Service
public class UserServiceImplm implements UserService {
	@Autowired
	private UserDao userDAO;

	@Override
	public ApiResponse getUserByEmail(String email, String password) {
		ApiResponse obj = userDAO.getUserByEmail(email, password);
		return obj;
	}	

    @Override
	public ApiResponse insertUser(User user) {
		ApiResponse obj = userDAO.insertUser(user);
		return obj;
	}	
}