package com.ead.course.specifications;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationsTemplate {
	// Verificar as notações 
	// classe que implementa a classe de config do especification 

	
	   // and para juntar todas as consultas.  Também pode usar o OR.
	   // usar a notação spec, path é qual atributo ou enum vai ser usado 
	  // spec = o tipo de condição .class 
		
		 @And({
	         @Spec(path = "courseLevel", spec = Equal.class), //quando é enum tem que usar do equals 
	         @Spec(path = "courseStatus", spec = Equal.class),
	         @Spec(path = "name", spec = Like.class),
	 })
		// criar uma interface que extende Specification <Qual a entidade que contém os atributos para a consulta>
		 public static interface CourseSpec extends Specification<CourseModel>{
		}
		      
		// depois de criado aqui deve ser chamado no método da controller. 
		 @Spec(path = "title", spec = Like.class)
		 public static interface ModuleSpec extends Specification<ModuleModel>{
			 
		 }
		 @Spec(path = "title", spec = Like.class)
		 public static interface LessonSpec extends Specification<LessonModel>{
			 
		 }
		 
		 // método do criteriaBuilder 
		 // VINCULAR UMA PESQUISA DE UM ID COM OS FILTROS DO SPECIFICTIONS.LISTA DE MODULOS VINCULADOS A UM CURSO ID 
		    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
		    	//recebe um curso id como parametro 
		    	//cb criteria bilder, root, e um query 
		        return (root, query, cb) -> {
		        	// na query não pode ter valores duplicados 
		            query.distinct(true);
		            //entidades que fazem parte da consulta entidade a - b. 
		            Root<ModuleModel> module = root;
		            Root<CourseModel> course = query.from(CourseModel.class);
		            // Extrair a coleção da entidade A que esta presente na entidade B 
		            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
		            
		            // utilizar o criteriabuilde para consultruir a query and para que os 2filtros sejam satisfeito
		            // vai filtrar o curso Id que esta vindo do método. is member como se fosse um sub select 	
		            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, coursesModules));
		        };
		        
		        // depois de criado o método para implementar consulta com filtro passado pelo cliente o specfication, e também a paginação
		        // agora deve levar o método até a controller. 
		 }
		    
		    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
		        return (root, query, cb) -> {
		            query.distinct(true);
		            Root<LessonModel> lesson = root;
		            Root<ModuleModel> module = query.from(ModuleModel.class);
		            // get lesson dentro de modulos aonde faz a relacao 
		            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
		            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
		        };
		    }
		        
			public static Specification<CourseModel> courseUserId(final UUID userId) {
				return (root, query, cb) -> {
					query.distinct(true);
					Join<CourseModel, CourseUserModel> curseProd = root.join("courseUsers");
					return cb.equal(curseProd.get("userId"), userId);
				};

			}

		}
