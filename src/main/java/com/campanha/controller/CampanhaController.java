package com.campanha.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.campanha.domain.exception.EntidadeEmUsoException;
import com.campanha.domain.exception.EntidadeNaoEncontradaException;
import com.campanha.domain.model.Campanha;
import com.campanha.domain.repository.CampanhaRepository;
import com.campanha.domain.service.CampanhaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Api(tags = "Campanhas")
@RestController
@RequestMapping("/campanhas")
public class CampanhaController {

	@Autowired
	CampanhaService campanhaService;

	@Autowired
	CampanhaRepository repository;

	
	@ApiOperation("Cadastra uma nova campanha")
	@PostMapping
	public ResponseEntity<Campanha> salvarCampanha(@ApiParam(name = "corpo",value = "Representa uma campanha")
												  @Valid @RequestBody Campanha campanha) {
		campanha = campanhaService.salvarCampanha(campanha);

		return ResponseEntity.status(HttpStatus.CREATED).body(campanha);

	}

	@ApiOperation("Atualiza uma determinada campanha")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarCampanha(@ApiParam(name = "id de uma campanha",example = "1")			
											   @PathVariable long id,
											   @ApiParam(name = "corpo",value = "Representa uma campanha")
											   @Valid @RequestBody Campanha campanha) {
		try {

			Campanha campanhaAtualizada = campanhaService.atualizarCampanha(id, campanha);
			if (campanhaAtualizada != null) {
				return ResponseEntity.ok(campanhaAtualizada);
			}

			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@ApiOperation("Lista camapanhas ativas")
	@GetMapping
	public List<Campanha> listarCampanhas() {

		return campanhaService.listarCampanhas();
	}
	
	@ApiOperation("Lista camapanhas por clube")
	@GetMapping("{id}")
	public List<Campanha> listarCampanhasPorClube(@ApiParam(name = "id de uma campanha",example = "1")
										  @PathVariable long id) {

		return campanhaService.listarCampanhasPorClube(id);
	}

	@ApiOperation("Deleta uma campanha")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerCampanha(@ApiParam(name = "id de uma campanha",example = "1")
											 @PathVariable long id) {
		try {

			campanhaService.deletarCampanha(id);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	@ApiOperation("Lista campanhas por período")
	@GetMapping("/periodo")
	public List<Campanha> listarPeriodos(@ApiParam(name = "data inicial", value = "representa a data inicial")
										 String dataInicio, 
										 @ApiParam(name = "data final", value = "representa a data final")
										 String dataVigencia) {

		LocalDate dataInicial = LocalDate.parse(dataInicio);
		LocalDate dataFinal = LocalDate.parse(dataVigencia);

		return repository.listarCampanhasPeriodoVigencia(dataInicial, dataFinal);
	}

}
