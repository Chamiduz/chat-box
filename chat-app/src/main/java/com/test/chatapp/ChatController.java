package com.test.chatapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.chatapp.model.Message;
import com.test.chatapp.profanitycheck.ProfanityChecker;
import com.test.chatapp.profanitycheck.SessionProfanity;

@Controller
public class ChatController {

	
	@Autowired
	private ProfanityChecker profanityFilter;
	
	@Autowired 
	private SessionProfanity profanity;
	
	//@RequestMapping("/chat.sendMessage")
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/group")
    public Message sendMessage(@Payload Message chatMessage) {
    	System.out.println(">>>>>>>>>>>>>>>>>>>" + chatMessage.getContent());
    	
    	try {
			checkProfanityAndSanitize(chatMessage);
		} catch (Exception e) {
			chatMessage.setContent(e.getMessage());
		}
    	
        return chatMessage;
    
    }

    
    
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        
        return chatMessage;
    }
    
    
    private void checkProfanityAndSanitize(Message chatMessage) throws Exception {
		long profanityLevel = profanityFilter.getMessageProfanity(chatMessage.getContent());
		profanity.increment(profanityLevel);
		chatMessage.setContent(profanityFilter.filter(chatMessage.getContent()));
	}

}