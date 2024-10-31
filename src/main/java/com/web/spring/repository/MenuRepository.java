package com.web.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.spring.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
