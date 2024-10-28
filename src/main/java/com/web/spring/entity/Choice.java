package com.web.spring.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Choice {
	@Id
	@GeneratedValue
	@Column(name = "choice_id")
	private int choiceId;

	@CreatedDate
	@Column(name = "choice_date")
	private LocalDate choiceDate;
	
	@ManyToOne
	@JoinColumn(name = "user_no")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Override
	public String toString() {
		return "Choice [choiceId=" + choiceId + ", choiceDate=" + choiceDate + "]";
	}
	
	
	
}
