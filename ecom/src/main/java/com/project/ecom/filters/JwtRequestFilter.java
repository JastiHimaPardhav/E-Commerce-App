package com.project.ecom.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.ecom.services.jwt.UserDetailsServiceImpl;
import com.project.ecom.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{
		
	@Autowired
    private JwtUtil jwtUtil;

	@Autowired
    private UserDetailsServiceImpl userDetailsService;
    
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain)
	            throws ServletException, IOException {
		 
		String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        String path = request.getServletPath();
        if (path.equals("/authenticate") || path.equals("/signup")) {
            filterChain.doFilter(request, response); // skip JWT check
            return;
        }

        
        // Check if header has JWT token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	token = authHeader.substring(7); // Remove "Bearer " prefix
            username = jwtUtil.extractUserName(token); // You define this in your JwtUtil
        }
        
        // If we got the username and context is not authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Proceed with the filter chain
        filterChain.doFilter(request, response);
        
	 }
}
