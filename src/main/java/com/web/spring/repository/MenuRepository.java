package com.web.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.spring.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	Optional<List<Menu>> findByNationInAndMajorCategorieIn(List<String> nation, List<String> majorCategorie);
//	@Query(value = "select m from Menu m where m.major_categorie in :major_categorie and m.nation in :nation")
//	List<Menu> getMenuByNationAndMajorCategorie(@Param("major_categorie") List<String> categories, @Param("nation") List<String> nation);
}
