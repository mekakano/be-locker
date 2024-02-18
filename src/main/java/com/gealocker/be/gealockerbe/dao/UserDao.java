package com.gealocker.be.gealockerbe.dao;

import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.User;
import java.util.List;

public interface UserDao {
	 ApiResponse getUserByEmail(String email, String password);
     ApiResponse insertUser(User user);

}

