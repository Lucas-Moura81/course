package com.ead.course.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LessonDto {

	@NotBlank // nem null nem vazio 
	private String title; 
	
	private String description; 
	
	@NotBlank
	private String videoUrl; 
	
	
}
