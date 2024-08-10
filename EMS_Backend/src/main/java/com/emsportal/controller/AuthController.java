package com.emsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsportal.dto.AdminAuthDto;
import com.emsportal.dto.AdminLoginResponseDto;
import com.emsportal.dto.UserLoginDto;
import com.emsportal.dto.UserLoginResponseDto;
import com.emsportal.dto.UserRegisterDto;
import com.emsportal.dto.SuccessandMessageDto;
import com.emsportal.dto.OrganiserLoginDto;
import com.emsportal.dto.OrganiserLoginResponseDto;
import com.emsportal.model.AdminEntity;
import com.emsportal.model.UserEntity;
import com.emsportal.model.OrganiserEntity;
import com.emsportal.model.UserType;
import com.emsportal.repository.AdminRepo;
import com.emsportal.repository.UserRepo;
import com.emsportal.repository.OrganiserRepo;
import com.emsportal.security.CustomUserDetailsService;
import com.emsportal.security.JwtGenerator;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AuthController {
	
	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private OrganiserRepo organiserRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@PostMapping("api/v1/adminRegister")
	public ResponseEntity<String> adminRegister(@RequestBody AdminAuthDto adminAuthDto) {
		if(adminRepo.existsByUsername(adminAuthDto.getUsername())) {
			return new ResponseEntity<String>("Adminname is taken !! ",HttpStatus.BAD_REQUEST);
		}
		AdminEntity adminEntity = new AdminEntity();
		adminEntity.setUsername(adminAuthDto.getUsername());
		adminEntity.setPassword(passwordEncoder.encode(adminAuthDto.getPassword()));
		adminRepo.save(adminEntity);
		return new ResponseEntity<String>("Admin Register successfull !! ", HttpStatus.CREATED);
	}
	
	@PostMapping("api/v1/adminLogin")
	public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminAuthDto adminAuthDto) {
		System.out.println("adminLogin");
		customUserDetailsService.setUserType(UserType.ADMIN);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(adminAuthDto.getUsername(), adminAuthDto.getPassword())); 
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtGenerator.generateToken(authentication,UserType.ADMIN.toString());
		AdminLoginResponseDto responseDto = new AdminLoginResponseDto();
		responseDto.setSuccess(true);
		responseDto.setMessage("login successful !!");
		responseDto.setToken(token);
		AdminEntity admin = adminRepo.findByUsername(adminAuthDto.getUsername()).orElseThrow();
		responseDto.setAdmin(admin.getUsername(), admin.getId());
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping("api/v1/organiserLogin")
	public ResponseEntity<OrganiserLoginResponseDto> teacherLogin(@RequestBody OrganiserLoginDto teacherLoginDto) {
		System.out.println("organiserLogin");
		customUserDetailsService.setUserType(UserType.ORGANISER);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(teacherLoginDto.getEmail(), teacherLoginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication, UserType.ORGANISER.toString());
		OrganiserLoginResponseDto responseDto = new OrganiserLoginResponseDto();
		responseDto.setSuccess(true);
		responseDto.setMessage("login successful !!");
		responseDto.setToken(token);
		OrganiserEntity teacher = organiserRepo.findByEmail(teacherLoginDto.getEmail()).orElseThrow();
		responseDto.setTeacher(teacher.getName(), teacher.getEmail(), teacher.getId());
		return new ResponseEntity<OrganiserLoginResponseDto>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping("api/v1/userRegister")
	public ResponseEntity<SuccessandMessageDto> studentRegister(@RequestBody UserRegisterDto studentRegisterDto) {
		System.out.println("userRegister");
		SuccessandMessageDto response = new SuccessandMessageDto();
		if(userRepo.existsByEmail(studentRegisterDto.getEmail())) {
			response.setMessage("Email is already registered !!");
			response.setSuccess(false);
			return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.BAD_REQUEST);
		}
		UserEntity studentEntity = new UserEntity();
		studentEntity.setName(studentRegisterDto.getUsername());
		studentEntity.setPassword(passwordEncoder.encode(studentRegisterDto.getPassword()));
		studentEntity.setEmail(studentRegisterDto.getEmail());
		userRepo.save(studentEntity);
		response.setMessage("Profile Created Successfully !!");
		response.setSuccess(true);
		return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
	}
	
	@PostMapping("api/v1/userLogin")
	public ResponseEntity<UserLoginResponseDto> studentLogin(@RequestBody UserLoginDto studentLoginDto) {
		System.out.println("userLogin");
		customUserDetailsService.setUserType(UserType.USER);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(studentLoginDto.getEmail(), studentLoginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication, UserType.USER.toString());
		UserLoginResponseDto responseDto = new UserLoginResponseDto();
		responseDto.setSuccess(true);
		responseDto.setMessage("login successful !!");
		responseDto.setToken(token);
		UserEntity student = userRepo.findByEmail(studentLoginDto.getEmail()).orElseThrow();
		responseDto.setStudent(student.getName(), student.getEmail(), student.getId());
		return new ResponseEntity<UserLoginResponseDto>(responseDto, HttpStatus.OK);
	}
}
