package com.ead.course.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface UtilsService {
	public String createUrl(UUID courseId, Pageable pageable);
}
