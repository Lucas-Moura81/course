package com.ead.course.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.ModuleModel;

public interface ModuleService {
   public void delete(ModuleModel moduleModel);

public ModuleModel save(ModuleModel moduleModel);

public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

public List<ModuleModel> findAllByCourse(UUID courseId);

public Optional<ModuleModel> findById(UUID moduleId);

public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable); 

}
