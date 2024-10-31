package com.web.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.spring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
}
