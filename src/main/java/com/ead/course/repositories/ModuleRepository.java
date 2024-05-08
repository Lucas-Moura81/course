package com.ead.course.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.course.models.ModuleModel;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel>{
 
	 // como utilizar o Graph paara uma consulta especifica 
     // para usar o especification deve ter essa classe na repository  JpaSpecificationExecutor
     // usando de um atributo  com um retorno de Module para trazer um curso relacionado. 
	// se tiver uzando o carregamento lento. 
//	
//	@EntityGraph(attributePaths = {"course"})
//	ModuleModel findByTitle(String title); 
	
	
	// para consulta nativas e personalizadas
	// value passa o comando. modulos onde o curso id seja igual o parametro.  deopis nativeQuery true. 
	//@Modifying para realizar consultar que modificam os dados. atualiza delete entre outros. 
 	@Query(value = "select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
	List<ModuleModel> findAllLModulesIntoCourse(@Param("courseId") UUID courseId);
    
 	@Query(value = "select * from tb_modules where course_course_id = :courseId and module_id = :moduleId", nativeQuery = true)
	Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}