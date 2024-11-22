package com.web.spring;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.web.spring.entity.Gender;
import com.web.spring.entity.User;
import com.web.spring.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //이거 안붙이면 아랫부분에 테스트 ordering이 안됨
@AutoConfigureTestDatabase(replace = Replace.NONE)//NONE 실제디비, ANY 가짜디비
@DataJpaTest
public class JpaRepositoryJUnitTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Rollback(value = false)
	@Order(1)
	public void saveUser() {
		log.info("=================== 1. save ===================");
		User user = User.builder()
				.userId("KOSTA284")
				.password("1234")
				.name("BAEK")
				.address("Inchoen")
				.gender(Gender.MAN)
				.phone("010-1234-5678")
				.taste("X")
				.birthDate(LocalDate.parse("2000-08-29"))
				.role(null)
				.build();
		
		User saveUser = userRepository.save(user);
		User findUser = userRepository.findByUserId(saveUser.getUserId());
		
		Assertions.assertThat(saveUser).isEqualTo(findUser);
	}
	
	@Test
	@Order(2)
	public void existsUserId() {
		log.info("=================== 2. existsByUserId ===================");
		Boolean exisit = userRepository.existsByUserId("KOSTA284");
		Assertions.assertThat(exisit).isEqualTo(true);
	}
	
	@Test
	@Order(3)
	public void findUser() {
		log.info("=================== 3. findByNameAndPhone ===================");
		
		//올바른 입력
		User findUser1 = userRepository.findByNameAndPhone("BAEK", "010-1234-5678");
		
		//잘못된 입력
		User findUser2 = userRepository.findByNameAndPhone("BAEK", "010-1234-5999");
		
		Assertions.assertThat(findUser1.getName()).isEqualTo("BAEK");
		Assertions.assertThat(findUser2).isNull();
	}
	
	@Test
	@Rollback(value = false)
	@Order(4)
	public void updateUser() {
		User findUser = userRepository.findByUserId("KOSTA284");
		findUser.setPhone("010-9999-8888"); //기존 010-1234-5678 -> 010-9999-8888 변경
		findUser.setPassword("9999"); //기존 1234 -> 9999 변경
		
		Assertions.assertThat(findUser.getPhone()).isEqualTo("010-9999-8888");
		Assertions.assertThat(findUser.getPassword()).isEqualTo("9999");
	}
	
	@Test
	@Rollback(value = false)
	@Order(5)
	public void deleteUser() {
		userRepository.deleteByUserId("KOSTA284");
		
		User findUser = userRepository.findByUserId("KOSTA284");
		Assertions.assertThat(findUser).isNull();
	}
}















