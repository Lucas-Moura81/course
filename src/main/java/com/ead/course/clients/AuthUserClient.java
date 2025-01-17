package com.ead.course.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AuthUserClient {
 
	@Autowired
	RestTemplate restTemplate; 
	
	@Autowired
	UtilsService utilsService;
	
	@Value("${ead.api.url.authuser}")
	String REQUEST_URL_AUTHUSER;
	
	public Page<UserDto> getAllUsersByCourses(UUID courseId, Pageable pageable){
		List<UserDto> searchResult = null;
		 String url = REQUEST_URL_AUTHUSER + utilsService.createUrl(courseId, pageable); 
		 log.debug("Request URL: {} ", url);
	        log.info("Request URL: {} ", url);
	        try{
	            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
	            ResponseEntity<ResponsePageDto<UserDto>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
	            searchResult = result.getBody().getContent();
	            log.debug("Response Number of Elements: {} ", searchResult.size());
	        } catch (HttpStatusCodeException e){
	            log.error("Error request /courses {} ", e);
	        }
	        log.info("Ending request /users courseId {} ", courseId);
	        return new PageImpl<>(searchResult);
	    }
	
	   // get one user by id. 
	   public ResponseEntity<UserDto>getOneUserById(UUID userId){
		   String url = REQUEST_URL_AUTHUSER + "/users/" + userId; 
		   return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class); 
	   }

	public void postSubscriptionInCourse(UUID courseId, UUID userId) {
		//primeiro construir a url 
		String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
		var courseUserDto = new CourseUserDto();
		courseUserDto.setCourseId(courseId);
		courseUserDto.setUserId(userId);
		// usando o método do rest para enviar. String é como vai enviado. 
	   restTemplate.postForObject(url, courseUserDto, String.class);
	}

	public void deleteCourseInAuthUser(UUID courseId) {
		//criar url 
		String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
		// enviar a requisição para o outro micro 
		restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
	}
   
	
}
