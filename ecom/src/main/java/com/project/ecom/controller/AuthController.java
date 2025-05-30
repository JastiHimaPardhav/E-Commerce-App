package com.project.ecom.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.project.ecom.dto.AuthenticationRequest;
import com.project.ecom.dto.SignupRequest;
import com.project.ecom.dto.UserDto;
import com.project.ecom.entity.User;
import com.project.ecom.repository.UserRepository;
import com.project.ecom.services.auth.AuthService;
import com.project.ecom.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private AuthService authService;

    @Autowired
    public AuthController(
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        JwtUtil jwtUtil,
        UserRepository userRepository,
        AuthService authService
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authService = authService;
    }
	
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
	@PostMapping("/authenticate")
	public void createAutenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws IOException, JSONException {
		try {
		    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
		        authenticationRequest.getPassword()));
		} 
		catch(BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password.");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername()); 
		Optional<User> optionalUser = userRepository.findFirstByEmail (userDetails.getUsername());
		final String jwt=jwtUtil.generateToken (userDetails.getUsername());
		
		if (optionalUser.isPresent()){
			response.getWriter().write(new JSONObject()
			.put("userId", optionalUser.get().getUserId()) 
			.put("role", optionalUser.get().getRole()) 
			.toString()
			);
			
			response.addHeader("Access-Control-Expose-Headers", "Authorization");
			response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, "+
			"X-Requested-With, Content-Type, Accept, X-Custom-header");
			
			response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
		
	}
	
}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		if(authService.hasUserWithEmail(signupRequest.getEmail())) {
			return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);		
		}
		UserDto userDto = authService.createUser(signupRequest);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	
}
