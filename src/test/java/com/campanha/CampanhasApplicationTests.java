package com.campanha;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.BeforeMapping;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;


import com.campanha.domain.model.Campanha;
import com.campanha.domain.model.Clube;
import com.campanha.domain.repository.CampanhaRepository;
import com.campanha.domain.service.CampanhaService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import util.ResourceUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CampanhasApplicationTests {
	@LocalServerPort
	private int port;
	
	private String JsonOK;
	
	@Autowired
	CampanhaRepository campanhaRepository;
	
	@BeforeEach
	public void setUp() {
		
		RestAssured.port = port;
		RestAssured.basePath = "/campanhas";
		
		JsonOK = ResourceUtils.getContentFromResource("/json/ok/campanha.json");
		
		
		PrepararDados();
	}
	
	@Test
	public void listarClientesTest() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void SalvarNovaCampanhaTest() {
		
		RestAssured.given()
			.body(JsonOK)
		.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
		
	}
	
	public void PrepararDados() {
		
		Campanha campanha = new Campanha();
		Clube clube = new Clube();
		clube.setId(1L);
		campanha.setClube(clube);
		campanha.setDataInicio(LocalDate.now());
		campanha.setDataVigencia(LocalDate.now().plusDays(1));
		campanha.setNomeCampanha("Campanha teste");		
		clube.setNomeClube("Palmeiras");
		
		campanhaRepository.save(campanha);
		
	}

}
