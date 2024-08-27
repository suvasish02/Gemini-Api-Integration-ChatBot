package com.suvasish.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suvasish.Dto.Prompt;
import com.suvasish.Response.ApiResponse;
import com.suvasish.Service.ChatBotService;

@RestController
@RequestMapping("/ai/chat")
public class ChatBotController {
	private  ChatBotService chatBotService;
	@Autowired
	public ChatBotController(ChatBotService chatBotService) {
		this.chatBotService=chatBotService;
	}
	@PostMapping
	public ResponseEntity<ApiResponse> getCoinDetails(@RequestBody Prompt prompt) throws Exception{
		ApiResponse response=chatBotService.getCoinDetails(prompt.getPrompt());
		response.setMessage(prompt.getPrompt());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/simple")
	public ResponseEntity<String> simpleChatHandler(@RequestBody Prompt prompt) throws Exception{
		String response=chatBotService.simpleChart(prompt.getPrompt());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
