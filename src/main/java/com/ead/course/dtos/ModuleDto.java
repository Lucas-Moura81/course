package com.ead.course.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ModuleDto {

	@NotBlank // nem null nem vazio 
	private String title; 
	
	@NotBlank
	private String description; 
	
}
