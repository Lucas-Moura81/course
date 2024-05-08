package com.ead.course.services.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;

@Service
public class CourseUserImpl implements CourseUserService{

	@Autowired
	CourseUserRepository courseUserRepository;
	
	@Autowired
	AuthUserClient authUserClient;

	@Override
	public boolean existByCourseAndUserId(CourseModel courseModel, UUID userId) {
		return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
	}

	@Override
	public CourseUserModel save(CourseUserModel courseUserModel) {
		return courseUserRepository.save(courseUserModel);
	}
    
	@Transactional // caso der ruim volta ao estado de antes 
	@Override
	public CourseUserModel salveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
		courseUserModel = courseUserRepository.save(courseUserModel);
		authUserClient.postSubscriptionInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId()); 
		return courseUserModel;
	}

	@Override
	public boolean existsByUserByUser(UUID userId) {
		return courseUserRepository.existsByUserId(userId);
	}
    
	@Transactional
	@Override
	public void deleteCouseUserByUser(UUID userId) {
		courseUserRepository.deleteAllUserId(userId);		
	} 
	
	
}
