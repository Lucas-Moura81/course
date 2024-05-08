package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationsTemplate;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // permite acesso em todas as origens. pode utilizar por métodos
public class LessonController {
 	
	 @Autowired
	 LessonService lessonService; 
	 
	 @Autowired
	 ModuleService moduleService; 
	 
	 //Salvar uma lesson 
	 @PostMapping("/modules/{moduleId}/lessons")
		public ResponseEntity<Object> saveLesson(
				                                  @PathVariable(value = "moduleId") UUID moduleId,
				                                  @RequestBody @Valid LessonDto lessonDto){
			Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
			if (!moduleModelOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo não encontrado");
			}

			var lessonModel = new LessonModel();
			BeanUtils.copyProperties(lessonDto, lessonModel);
			lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
			lessonModel.setModule(moduleModelOptional.get());
			return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));

		}
	  
	   // deleção do uma lição 
	   @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
		public ResponseEntity<Object>deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
				                                  @PathVariable(value = "lessonId") UUID lessonId){
			Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId); 
			if(!lessonModelOptional.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lição não encontrado para esse modulo");
			}
			lessonService.delete(lessonModelOptional.get());
				return ResponseEntity.status(HttpStatus.OK).body("Lição deletado com sucesso");
		}
	   
	   // Para atualizar uma lição 
			@PutMapping("/modules/{moduleId}/lessons/{lessonId}")
			public ResponseEntity<Object> updateLessons(@PathVariable(value = "moduleId") UUID moduleId,
					                                   @PathVariable(value = "lessonId") UUID lessonId,
					                                   @RequestBody @Valid LessonDto lessonDto) {
				
				Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId); 
				if(!lessonModelOptional.isPresent()) {
					ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lição não encontrado para esse modulo");
				}
				// método de atualizar o curso 
				var lessonModel = lessonModelOptional.get(); 
				BeanUtils.copyProperties(lessonDto, lessonModel);
				lessonModel.setTitle(lessonDto.getTitle());
				lessonModel.setDescription(lessonDto.getDescription());
				lessonModel.setVideoUrl(lessonDto.getVideoUrl());
		        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
			
			}
			
			//listar as lições de um modulo 
			@GetMapping("/modules/{moduleId}/lessons")
			ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId")UUID moduleId, 
					SpecificationsTemplate.LessonSpec spec,
				    @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Direction.ASC)
		            Pageable pageable){
				return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModules
						(SpecificationsTemplate.lessonModuleId(moduleId).and(spec), pageable)); 
			}
			
			// método que busca uma lição relacionado a um modulo
			@GetMapping("/modules/{moduleId}/lessons/{lessonId}")
			ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId, 
				                             	@PathVariable(value = "lessonId") UUID lessonId){
				Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId); 
				if(!lessonModelOptional.isPresent()) {
					ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lição não encontrado para esse modulo");
				}
				
				return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
			}
			
	 
}
