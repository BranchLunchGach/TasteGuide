package com.web.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.spring.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	Optional<List<Menu>> findByNationInAndCategoryIn(List<String> nation, List<String> category);
	Optional<List<Menu>> findByNationInOrCategoryIn(List<String> nation, List<String> category);
}
