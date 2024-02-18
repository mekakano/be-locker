package com.gealocker.be.gealockerbe.services;

import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.User;

public interface UserService {
	 ApiResponse getUserByEmail(String email, String Password);
     ApiResponse insertUser(User user);
}

