package com.suvasish.Service;

import com.suvasish.Response.ApiResponse;

public interface ChatBotService {
	//this end point will return the answer to all the prompt thrown by the user
	// in the form of ApiResponse Object.
	ApiResponse getCoinDetails(String prompt) throws Exception;
	
	//this will return answer to the simple question which will not be related to 
	//the coins.
	String simpleChart(String prompt);
	
}
