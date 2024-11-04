package com.web.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.spring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserId(String userId);
	@Query(value = "SELECT u FROM User u Where userId = ?1")
	User duplicateCheck(String userId);
	Boolean existsByUserId(String userId);
	
	
}
