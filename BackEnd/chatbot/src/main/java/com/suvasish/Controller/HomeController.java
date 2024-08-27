package com.suvasish.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suvasish.Response.ApiResponse;

@RestController
public class HomeController {
	//we will declare a method which will return ResponseEntity Object
	//this method will directly parse the prompts given by the User 
	//and provide the solutions.
	@GetMapping()
	public ResponseEntity<ApiResponse> homeController(){
		ApiResponse apiResponse=new ApiResponse();
		apiResponse.setMessage("Welcome To AI Chatbot");
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
}
