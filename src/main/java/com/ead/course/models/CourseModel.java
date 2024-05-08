package com.ead.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)// para ocutar os dados null, também tem outras opcoes, não somente para null
@Table(name = "TB_COURSES")
// implementando Specification e Pageable. primeiro criar a classe config e depois o tamplet. 
public class CourseModel implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id // Vai ser o id 
    @GeneratedValue(strategy = GenerationType.AUTO) // gerar id att
    private UUID courseId;
   
    @Column(nullable = false, length = 150)
    private String name;
   
    @Column(nullable = false, length = 250)
    private String description;
  
    @Column
    private String imageUrl;
  
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'") // padrão da data 
    @Column(nullable = false)
    private LocalDateTime creationDate;
   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'") //padrão ISO0681 
    //PODE USAR DE FORMA GLOBAL DENTRO DE UMA CLASSE DE CONFIGURAÇÃO 
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
   
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)// para salvar no banco como uma string
    private CourseStatus courseStatus;
   
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)// para salvar no banco como uma string 
    private CourseLevel courseLevel;
   
    @Column(nullable = false)
    private UUID userInstructor;
    
    
    // monstrando para course que ele  tem relação com module entidade, pode ser usado set ou list. 
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //serialização. tipo de acesso ao atributo modules. WRITE_ONLY quando consultar um dado e assionar um get, ele não vai mostrar o campo. 
    // um curso pode ter varios modulos 1 curso para muitos modulos 
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY )// para para o atributo que sera a key stragenira mappedBy.  
    @Fetch(FetchMode.SUBSELECT) //fetch Mode forma de buscar os dados select(ele vai consultar de forma separada.Join(unica consulta e traz todos os dados vinculados). subselect(ele faz 2 consultas)  
   // pode ter conflito com o fetch type A não ser que não declare e ele esteja defould ai ele usa o lazy(carregamento lento). zoin não usa com o lazy. Se n definir nem um o JPA USA DE DEFOULD O join. 
    private Set<ModuleModel> modules;  // usou set no lugar de list o set não é ordenado e não duplica. lista aceita duplicar e é ordenada 
	
    // modo de deleção com o JPA por cascata. 
    // @onDelete action = onDeleteAction.CASCADE remoção delegada pelo banco de dados. 
    // criar método transcational para remoção. 
    
    
    // fazendo a relação com a tabela intermediaria 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY )
    private Set<CourseUserModel> courseUsers;
	
    
    // MÉTODO DE CONVERSÃO + UMA OPÇÃO ALÉM DO BEANSUTIL
    
    public CourseUserModel convertToCourseUserModel(UUID userId) {
    	// id gera att, this pq ja estamos em curse model. user id que vem como param do método 
    	return new CourseUserModel(null, userId, this);
    }
	
}
