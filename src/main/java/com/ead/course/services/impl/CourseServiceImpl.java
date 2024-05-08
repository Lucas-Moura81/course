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

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService{
 
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	ModuleRepository moduleRepository; 
	
	@Autowired
	LessonRepository lessonRepository; 
	
    @Autowired
    CourseUserRepository courseUserRepository;
    
    @Autowired
    AuthUserClient authUserClient;

//	@Transactional // caso de errado ele volta aos dados anteriores. 
//	@Override
//	public void delete(CourseModel courseModel) {
//		// recuperar os modulos que pertecem ao curso 
//		List<ModuleModel> moduleModelsList = moduleRepository.findAllLModulesIntoCourse(courseModel.getCourseId()); 
//		if(!moduleModelsList.isEmpty()) {
//				//buscando lesson que estão vinculados aos modulos 
//			for(ModuleModel module: moduleModelsList) {
//				List<LessonModel> lessonModulesList = lessonRepository.findAllLessonIntoModule(module.getModuleId()); 
//				if(!lessonModulesList.isEmpty()) {
//					// usando do jpa para deletar a lista, não passar o sem parametro pq se não exclui todos
//					lessonRepository.deleteAll(lessonModulesList);
//				}
//			}
//			// deletando o modulo obs dentro do mesmo if
//			moduleRepository.deleteAll(moduleModelsList);
//		}
//		// agora deletenado o curso 
//		courseRepository.delete(courseModel);
//	}
//	
	   @Transactional
	    @Override
	    public void delete(CourseModel courseModel) {
		   // var para verificar se preciso enviar para o outro micro 
		   boolean deleteCourseUserInAuthUser = false;
	        List<ModuleModel> moduleModelList = moduleRepository.findAllLModulesIntoCourse(courseModel.getCourseId());
	        if (!moduleModelList.isEmpty()){
	            for(ModuleModel module : moduleModelList){
	                List<LessonModel> lessonModelList = lessonRepository.findAllLessonIntoModule(module.getModuleId());
	                if (!lessonModelList.isEmpty()){
	                    lessonRepository.deleteAll(lessonModelList);
	                }
	            }
	            moduleRepository.deleteAll(moduleModelList);
	        }
	        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
	        if(!courseUserModelList.isEmpty()){
	            courseUserRepository.deleteAll(courseUserModelList);
	            deleteCourseUserInAuthUser = true; // se ela existir aqui vira true 
	        }
	        courseRepository.delete(courseModel);
	        //enviar para authUser quando o curso for deletado. 
	        if(deleteCourseUserInAuthUser) {
	        	authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
	        }
	    }

	@Override
	public CourseModel save(CourseModel courseModel) {
		return courseRepository.save(courseModel);
	}

	@Override
	public Optional<CourseModel> findById(UUID courseId) {
		return courseRepository.findById(courseId); 
	}

	@Override
	// deve ser alterado o findAll para o que aceita a paginação. 
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
		return courseRepository.findAll(spec, pageable);
	}

}
