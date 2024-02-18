package com.gealocker.be.gealockerbe.api;

// import com.example.simpletask.model.JokeResponse;
// import com.example.simpletask.model.bdi.InquiryBalanceRequest;
// import com.example.simpletask.model.bdi.InquiryBalanceResponse;
// import com.example.simpletask.model.db.ApiRequest;
// import com.example.simpletask.model.db.ApiResponse;
// import com.example.simpletask.service.BookUsecase;
// import com.example.simpletask.service.InquiryBalanceBdiService;
// import com.example.simpletask.service.InvokeJoke;
// import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.Locker;
import com.gealocker.be.gealockerbe.entity.User;
import com.gealocker.be.gealockerbe.services.RentService;
import com.gealocker.be.gealockerbe.services.UserService;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@RestController
public class MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private RentService rentService;

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse login(@RequestBody User requestBody) {
		if (requestBody == null || requestBody.getEmail() == null || requestBody.getPassword() == null) {
			return null;
		}

		String email = requestBody.getEmail();
		String password = requestBody.getPassword();
		ApiResponse result = null;
		try {
			result = userService.getUserByEmail(email, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse register(
			@RequestBody User requestBody) {

		ApiResponse result = null;
		try {
			result = userService.insertUser(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@RequestMapping(value = { "/rental" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse rental(
			@RequestBody Locker requestBody) {

		ApiResponse result = null;
		try {
			result = rentService.rent(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@RequestMapping(value = { "/pengembalian" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse pengembalian(
			@RequestBody Locker requestBody) {

		ApiResponse result = null;
		try {
			result = rentService.pengembalian(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@RequestMapping(value = { "/validatePasswordLocker" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse checkPasswordLocker(
			@RequestBody Locker requestBody) {

		ApiResponse result = null;
		try {
			result = rentService.validatePasswordLocker(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@RequestMapping(value = { "/lihatDataPeminjaman" }, method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse lihatData(
			@RequestBody User requestBody) {

		ApiResponse result = null;
		try {
			result = rentService.lihatData(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	// @RequestMapping(value = { "/lihatDataOngoing" }, method = RequestMethod.POST)
	// @ResponseBody
	// public ApiResponse lihatDataOngoing(
	// @RequestBody User requestBody) {

	// ApiResponse result = null;
	// try {
	// result = rentService.lihatDataOngoing(requestBody);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }

	// return result;
	// }

	@RequestMapping(value = { "/lihatAvailable" }, method = RequestMethod.GET)
	@ResponseBody
	public ApiResponse lihatAvailable() {

		ApiResponse result = null;
		try {
			// You can perform any necessary logic here
			result = rentService.lihatAvailable(); // Assuming your service method does not require any parameters
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

}
