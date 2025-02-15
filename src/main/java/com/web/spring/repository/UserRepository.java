package com.web.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.spring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserId(String userId);
	Boolean existsByUserId(String userId);
	User findByNameAndPhone(String name, String phone);
	void deleteByUserId(String userId);
	
	//@Query(value = "SELECT m FROM Menu m WHERE m.menuNo IN (SELECT DISTINCT c.menu.menuNo FROM Choice c WHERE c.userNo = :userNo )")
	//List<Menu> getChartInfo(@Param("userNo") int userNo);
	
}
