package com.ead.course.dtos;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CourseUserDto {
	
	UUID userId; 
	
	@NotNull
	UUID courseId; 
}
