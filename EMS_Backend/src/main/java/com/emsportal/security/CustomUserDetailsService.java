package com.emsportal.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emsportal.model.AdminEntity;
import com.emsportal.model.UserEntity;
import com.emsportal.model.OrganiserEntity;
import com.emsportal.model.UserType;
import com.emsportal.repository.AdminRepo;
import com.emsportal.repository.UserRepo;
import com.emsportal.repository.OrganiserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private OrganiserRepo organiserRepo;
	@Autowired
	private UserRepo userRepo;
	
	private UserType userType;
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(userType);
		if(userType==UserType.ADMIN) {
			
			AdminEntity adminEntity = adminRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Admin Username "+ username+ "not found"));
			
			SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(UserType.ADMIN.toString());
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(adminAuthority);
			return new User(adminEntity.getUsername(), adminEntity.getPassword(), authorities);
		} else if(userType == UserType.ORGANISER) {
			OrganiserEntity organiserEntity = organiserRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Organiser Email "+ username+ "not found"));
			
			SimpleGrantedAuthority organiserAuthority = new SimpleGrantedAuthority(UserType.ORGANISER.toString());
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(organiserAuthority);
			return new User(organiserEntity.getEmail(), organiserEntity.getPassword(), authorities);
		} else if(userType == UserType.USER) {
			UserEntity userEntity = userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Email "+ username+ "not found"));
			
			SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(UserType.USER.toString());
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(userAuthority);
			return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
		}
		return null;
	}
	
}
