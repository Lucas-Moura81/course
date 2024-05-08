package com.ead.course.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.CourseModel;

public interface CourseService {

	//criando método para deleção com mais controle 
	
	public void delete(CourseModel courseModel);

	public CourseModel save(CourseModel courseModel);

	public Optional<CourseModel> findById(UUID courseId);
   
	//depois do modification tem que alterar o repository para sustentar. 
	//Specification e o tipo como parametro diferente da forma que foi feito no user 
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);

}
