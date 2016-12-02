package com.example.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Users;
import com.example.repo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@JsonSerialize
@RestController
public class UserController {
	
	@Autowired
	UserRepository repo;

	@RequestMapping("/login")
	@HystrixCommand
	public Users getOneUser(@RequestParam("name") String name) {
		return repo.findByName(name);
	}
	
	@RequestMapping("/getinstance")
	public String getInstance() throws JsonProcessingException, IOException {
		String version = System.getenv("VERSION");
		
		String vcap = System.getenv("VCAP_APPLICATION");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode vcap_app = mapper.readTree(vcap);
		return "User App = " + vcap_app.get("instance_index").asText() + " (version = " + version+ ")";
	}

	@RequestMapping("/v")
	@HystrixCommand
	public String showVersion() {
		return System.getenv("VERSION");
	}	

	@HystrixCommand
	@RequestMapping("/kill")
	public String kill() {
		System.exit(-1);
		return "killed!";
	}
	
	@RequestMapping("/getlocalinfo")
	public String[] getLocalInfo() throws JsonProcessingException, IOException {
		String vcap = System.getenv("VCAP_APPLICATION");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode vcap_app = mapper.readTree(vcap);
		String[] list = new String[3];
		list[0] = System.getenv("CF_INSTANCE_ADDR");
		list[1] = System.getenv("VERSION");
		list[2] = vcap_app.get("instance_index").asText();
		return list;
	}
}
