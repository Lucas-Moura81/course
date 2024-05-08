package com.ead.course.configs;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;

// resolver = para filtrar pesquisas em uma API
//Implementação do Resolver. primeiro deve ser adicionado a dependencia, depois criar a classe de conf. 
//essa biblioteca converte os dados do parametros para tipos basicos java(addArgumentResolvers)
@Configuration
public class ResolverConfig extends WebMvcConfigurationSupport{
        
	// esse é um método padrão quando se cria a classe resolver 
	// deve extender da classe WebMvcConfigurationSupport
	   @Override
	    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	        argumentResolvers.add(new SpecificationArgumentResolver()); 
// depois foi criado esse método para receber a paginação e também os filtros. 
			PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
			argumentResolvers.add(resolver);
			super.addArgumentResolvers(argumentResolvers);
			
// depois cria um pacote com uma classe para implementar os especifictions. 
		}
	   }
