package com.ead.course.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;

import lombok.extern.log4j.Log4j2;
// controller responsavel por receber do outro micro service. 
@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
   
	@Autowired
	AuthUserClient authUserClient; 
	
	@Autowired
	CourseService courseService; 
	
	@Autowired
	CourseUserService courseUserService;
	
	@GetMapping("/courses/{courseId}/users")
	// quando pode ter mais de um tipo de retorno dele colocar o object 
	public ResponseEntity<Object> getAllUserByCourses(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Direction.ASC) Pageable pageable,
			                                                                  @PathVariable(value = "courseId") UUID courseId) {
		
	Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
	
	if(!courseModelOptional.isPresent()) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado !");
	}
		
			
	return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourses(courseId, pageable));
	
	}
	
	//Método para cadastrar usuario a um curso 
	@PostMapping("courses/{courseId}/users/subscription")
	public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
			                                                   @RequestBody @Valid SubscriptionDto subscriptionDto){
		ResponseEntity<UserDto> responseUser; 
		// buscar o curso por id 
		Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
		
		
		if(!optionalCourseModel.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado");
		}
		if(courseUserService.existByCourseAndUserId(optionalCourseModel.get(), subscriptionDto.getUserId())) {
			return	ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já cadastrado no curso");
		}
		//TO-DO CONSULTA A AUTHUSER
		try {
			responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
			if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
				ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário com status bloqueado!");
			}
		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
			}
		}
		CourseUserModel courseUserModel = courseUserService
				//salva e envia para o outro microserviço authuser. 
				.salveAndSendSubscriptionUserInCourse(optionalCourseModel.get().convertToCourseUserModel(subscriptionDto.getUserId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
		
	}
	
	@DeleteMapping("/courses/users/{userId}")
	public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId){
		if(courseUserService.existsByUserByUser(userId)) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser não encontrado");
		}
		courseUserService.deleteCouseUserByUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body("CouseUser deletado com sucesso!"); 
	}
	
}
