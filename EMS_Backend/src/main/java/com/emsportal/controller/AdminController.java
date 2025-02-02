package com.emsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsportal.dto.SuccessandMessageDto;
import com.emsportal.dto.OrganiserRegisterDto;
import com.emsportal.model.OrganiserEntity;
import com.emsportal.repository.AdminRepo;
import com.emsportal.repository.OrganiserRepo;
import com.emsportal.security.JwtGenerator;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OrganiserRepo teacherRepo;
	@Autowired
	private AdminRepo adminRepo;

	@PostMapping("/register")
	public ResponseEntity<SuccessandMessageDto> registerTeacher(@RequestBody OrganiserRegisterDto teacherRegisterDto, @RequestHeader(name="Authorization") String token) {
		SuccessandMessageDto response = new SuccessandMessageDto();
		if(teacherRepo.existsByEmail(teacherRegisterDto.getEmail())) {
			response.setMessage("Email is already registered !!");
			response.setSuccess(false);
			return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.BAD_REQUEST);
		}
		OrganiserEntity teacherEntity = new OrganiserEntity();
		teacherEntity.setName(teacherRegisterDto.getUsername());
		teacherEntity.setPassword(passwordEncoder.encode(teacherRegisterDto.getPassword()));
		teacherEntity.setEmail(teacherRegisterDto.getEmail());
		teacherEntity.setStatus(true);
		try {
			teacherEntity.setCreatedBy(adminRepo.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.setMessage("Unauthorized request");
			response.setSuccess(false);
			return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.UNAUTHORIZED);
		}
		teacherRepo.save(teacherEntity);
		response.setMessage("Profile Created Successfully !!");
		response.setSuccess(true);
		return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
	}
}
