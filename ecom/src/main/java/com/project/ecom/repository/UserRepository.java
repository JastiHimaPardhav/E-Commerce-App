package com.project.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.ecom.entity.User;
import com.project.ecom.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
	
	Optional<User> findFirstByEmail(String email);
	
	User findByRole(UserRole userRole);
}
