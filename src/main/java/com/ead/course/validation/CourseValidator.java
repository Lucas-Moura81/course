package com.ead.course.validation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserType;

// implementando Validação Customizada
@Component
public class CourseValidator implements Validator{
	
	@Autowired
	Validator validator; 
	
	@Autowired
	AuthUserClient authUserClient;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		//tem que fazer um cacth para transformar obj no dto 
		CourseDto courseDto = (CourseDto) o; 
		//validar os atributos 
		validator.validate(courseDto, errors);
		
		if(!errors.hasErrors()) {
			validateUserInscructor(courseDto.getUserInstructor(), errors);
		}
		
	}
   // validação personalizada. 
	private void validateUserInscructor(UUID userInstructor, Errors errors) {
		ResponseEntity<UserDto> responseUserInstructor; 
		try {
			responseUserInstructor = authUserClient.getOneUserById(userInstructor);
			if(responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
				errors.rejectValue("userInstructor", "userInstructorError", "Usuario deve ser instrutor ou adm para realizar essa operação");
			}
		} catch (HttpStatusCodeException e) {
	       
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				errors.rejectValue("userInstructor", "userInstructorError", "Instrutor não encontrado");
			}
		}
	}
	
	// para utilizar vá na controller.
}
