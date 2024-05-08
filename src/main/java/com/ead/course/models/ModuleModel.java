package com.ead.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)// para ocutar os dados null, também tem outras opcoes, não somente para null
@Table(name = "TB_MODULES")
public class ModuleModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id // Vai ser o id
	@GeneratedValue(strategy = GenerationType.AUTO) // gerar id att
	private UUID moduleId; 
	
	@Column(nullable = false, length = 150)
	private String title; 
	
	@Column(nullable = false, length = 250)
	private String description; 
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'") // padrão da data 
	private LocalDateTime creationDate; 
	
	//mostrar que o modulo está vinculado a um determinado curso 
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY, optional = false) // varios modulos para um curso // se pode ou não ser opcional. precisa assimilar sempre um M a um Curso
	//fetch como os dados vao ser carregados no banco lazy mais recomendado.  
	private CourseModel course;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
	private Set<LessonModel> lessons;
	
	
 }
