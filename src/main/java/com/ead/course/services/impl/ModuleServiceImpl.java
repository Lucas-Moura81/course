package com.ead.course.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService{
  
	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	LessonRepository lessonRepository; 
	
	// buscando lesson vinculadas a um modulo e deletando. 
	@Transactional // sempre anotar para se der merda voltar. 
	@Override
	public void delete(ModuleModel moduleModel) {
		List<LessonModel> lessonModuleList = lessonRepository.findAllLessonIntoModule(moduleModel.getModuleId());
		if (!lessonModuleList.isEmpty()) {
			lessonRepository.deleteAll(lessonModuleList);
		}
        moduleRepository.delete(moduleModel);
	}

	@Override
	public ModuleModel save(ModuleModel moduleModel) {
		return moduleRepository.save(moduleModel);
	}

	@Override
	public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
		return moduleRepository.findModuleIntoCourse(courseId, moduleId); 
	}

	@Override
	public List<ModuleModel> findAllByCourse(UUID courseId) {
		return moduleRepository.findAllLModulesIntoCourse(courseId);
	}

	@Override
	public Optional<ModuleModel> findById(UUID moduleId) {
		return moduleRepository.findById(moduleId);
	}

	@Override
	public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
		// para usar o specification sempre a classe de repository deve extender o JpaEspecificationExecuter 
		// findAll deve receber o spec e pageable
		return moduleRepository.findAll(spec, pageable);
	}
}
