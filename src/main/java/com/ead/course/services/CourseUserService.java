package com.ead.course.services;

import java.util.UUID;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

public interface CourseUserService {

	boolean existByCourseAndUserId(CourseModel courseModel, UUID userId);

	CourseUserModel save(CourseUserModel courseUserModel);

	CourseUserModel salveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel);
	
	boolean existsByUserByUser(UUID userId);
	
	void deleteCouseUserByUser(UUID userId); 

}
