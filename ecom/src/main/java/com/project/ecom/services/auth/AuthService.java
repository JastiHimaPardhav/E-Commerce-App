package com.project.ecom.services.auth;

import com.project.ecom.dto.SignupRequest;
import com.project.ecom.dto.UserDto;

public interface AuthService {
	
	UserDto createUser(SignupRequest signupRequest);
	
	Boolean hasUserWithEmail(String email);
	
}
