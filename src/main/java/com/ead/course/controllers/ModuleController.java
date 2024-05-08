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

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationsTemplate;
//Specification Avançado para Relacionamento e Pageable
@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // permite acesso em todas as origens. pode utilizar por métodos 
public class ModuleController {
 
	@Autowired
	ModuleService moduleService; 
	
	@Autowired
	CourseService courseService;
    
	//buscar um modulo vinculado a um curso. e salvar o modulo no banco  
	@PostMapping("/courses/{courseId}/modules")
	public ResponseEntity<Object>saveModule(@RequestBody @Valid ModuleDto moduleDto, 
			                                @PathVariable(value = "courseId") UUID courseId){
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
		if(!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado");
		}
		var moduleModel = new  ModuleModel();
		BeanUtils.copyProperties(moduleDto, moduleModel);
		moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		// como foi feito um mapeamento deve ser usetado o modulo
		moduleModel.setCourse(courseModelOptional.get());
		return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel)); 
		
	}
	
	// Para deletar modulo relacionado a um determinado curso.
		@DeleteMapping("/courses/{courseId}/modules/{moduleId}")
		public ResponseEntity<Object>deleteModule(@PathVariable(value = "courseId") UUID courseId,
				                                  @PathVariable(value = "moduleId") UUID moduleId){
			Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId); 
			if(!moduleModelOptional.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo não encontrado para esse curso");
			}
				moduleService.delete(moduleModelOptional.get());
				return ResponseEntity.status(HttpStatus.OK).body("Modulo deletado com sucesso");
		}
		
   // Para atualizar um modulo 
		@PutMapping("/courses/{courseId}/modules/{moduleId}")
		public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
				                                   @PathVariable(value = "moduleId") UUID moduleId,
				                                   @RequestBody @Valid ModuleDto moduleDto) {
			
			Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);  
			if(!moduleModelOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo não encontrado para esse curso"); 
			}
			// método de atualizar o modulo 
			var moduleModel = moduleModelOptional.get(); 
			BeanUtils.copyProperties(moduleDto, moduleModel);
			moduleModel.setTitle(moduleDto.getTitle());
			moduleModel.setDescription(moduleDto.getDescription());
	        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
		
		}
		
		//Listar os modulos por um determinado curso 
		// depois de criado o método do criteria bilder moduleCourseId na classe SpecificationsTemplate. deve ser implementado o método	
		@GetMapping("/courses/{courseId}/modules")
	    ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId")UUID courseId, 
	    		    SpecificationsTemplate.ModuleSpec spec,
				    @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Direction.ASC)
		            Pageable pageable){
			// aqui tem relacionamento e recebemos um curso na url como um patch var 
			// vamos ter que fazer uma combinação com o especification 
			// declarar uma nova consulta em SpecificationsTemplate usando JpaCriteriaBuilder e criteriaQuery
			return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse
					// passando o método criado no specifications com o parametro e juntanto com o spec e a paginação. 
					(SpecificationsTemplate.moduleCourseId(courseId).and(spec), pageable)); 
		}
		
		// método que busca um modulo relacionado a um curso
		@GetMapping("/courses/{courseId}/modules/{modulesId}")
		ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId, 
			                             	@PathVariable(value = "modulesId") UUID modulesId){
			Optional<ModuleModel> moduleModelOptional =  moduleService.findModuleIntoCourse(courseId, modulesId);
			if(!moduleModelOptional.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo não encontrado para esse curso");
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
		}
		
		
		
}
