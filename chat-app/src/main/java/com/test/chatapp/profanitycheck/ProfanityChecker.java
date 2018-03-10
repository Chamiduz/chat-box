package com.test.chatapp.profanitycheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProfanityChecker {
	
	/**
	 * this can be done by another micro service
	 */
/*	public String filter(String message) {
		String msg = "";
		
		RestTemplate restTemplate = new RestTemplate();
		msg = restTemplate.getForObject("http://profanity.service/check", String.class);
		return msg;
	}*/

	private Set<String> profanities = new HashSet<>();

	public long getMessageProfanity(String message) {
		return Arrays.stream(message.split(" ")) 
				.filter(word -> profanities.contains(word)) 
				.count();
	}

	public String filter(String message) {
		return Arrays.stream(message.split(" "))
				.filter(word -> !profanities.contains(word)) 
				.collect(Collectors.joining(" "));
	}

	public Set<String> getProfanities() {
		return profanities;
	}

	public void setProfanities(Set<String> profanities) {
		this.profanities = profanities;
	}
}