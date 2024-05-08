package com.ead.course.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor // para aceitar construtor com parametros 
@NoArgsConstructor // para aceitar construtor sem parametros 
@JsonInclude(JsonInclude.Include.NON_NULL)// para ocutar os dados null, também tem outras opcoes, não somente para null
@Table(name = "TB_COURSES_USERS")
public class CourseUserModel implements Serializable {
	// criar uma tabela intermediaria relacionando curso e usuarios 

 private static final long serialVersionUID = 1L;
 
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private UUID id; 
 
 @Column(nullable = false)
 private UUID userId; 

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 private CourseModel course; 
 

}
