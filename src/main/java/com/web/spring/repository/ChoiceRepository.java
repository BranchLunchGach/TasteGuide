package com.web.spring.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.spring.entity.Choice;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Integer> {
	Optional<List<Choice>> findByChoiceDateAfterAndUser_UserNo(LocalDateTime choiceDate, int userNo);
	
	@Query(value = "select c from Choice c join fetch c.user u where u.userNo = :userNo and c.choiceDate > :choiceDate")
	Optional<List<Choice>> findChoice(LocalDateTime choiceDate, int userNo);
	
}
