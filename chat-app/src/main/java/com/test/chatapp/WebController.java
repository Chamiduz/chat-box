package com.test.chatapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping("chat")
	public String chatBox() {
		return "chatbox.html";
	}
}
