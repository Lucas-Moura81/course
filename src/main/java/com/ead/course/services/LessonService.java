package com.ead.course.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.LessonModel;

public interface LessonService {

	LessonModel save(LessonModel lessonModel);

	Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

	void delete(LessonModel lessonModel);

	List<LessonModel> findAllByModules(UUID moduleId);

	Page<LessonModel> findAllByModules(Specification<LessonModel> spec, Pageable pageable);

}
