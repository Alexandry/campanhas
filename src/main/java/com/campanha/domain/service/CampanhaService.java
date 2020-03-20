package com.campanha.domain.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.campanha.domain.exception.EntidadeEmUsoException;
import com.campanha.domain.exception.EntidadeNaoEncontradaException;
import com.campanha.domain.model.Campanha;
import com.campanha.domain.repository.CampanhaRepository;



@Service
public class CampanhaService {

	@Autowired
	private CampanhaRepository campanhaRepository;
	
	@Autowired
	private MessageSource mensagem;
	
	private static final String message = "Não existe campanha com o id %d cadastrado";
	

	public Campanha salvarCampanha(Campanha campanha) {

		verificarPeriodoCampanhas(campanha);

		return campanhaRepository.save(campanha);
	}

	public List<Campanha> listarCampanhas() {

		List<Campanha> campanhasVigentes = campanhaRepository.listarCampanhasAtivas();
		return campanhasVigentes;

	}
	
	public void deletarCampanha(Long id) {
		
		try {
			Long campanhaId = id;
			String msg = mensagem.getMessage("campanha.nao.cadastrada",null, LocaleContextHolder.getLocale());
			Campanha campanha = campanhaRepository.findById(campanhaId).orElseThrow(
					() -> new com.campanha.domain.exception.EntidadeNaoEncontradaException(String.format(msg, id)));
			
			campanhaRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe cozinha com id %d ", id));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cozinha com o codigo %d não pode ser removida", id));
		}
	}
	
	public Campanha atualizarCampanha(long id, Campanha campanha) {
		
		try {
					
		Campanha putCampanha = buscarCampanhaPorId(id);
		Campanha campanhaAtualizada = null;
		if (putCampanha != null) {
			
			BeanUtils.copyProperties(campanha, putCampanha, "idCampanha");
			campanhaAtualizada = campanhaRepository.save(putCampanha);
			
		}
		return campanhaAtualizada;
		} catch (EntidadeNaoEncontradaException e) {
			 throw new EntidadeNaoEncontradaException(String.format("Não existe campanha com id %d ", id));
		}//TODO novo catch para tratar SQLIntegrityConstraintViolationException
		
	}
	
	public Campanha buscarCampanhaPorId(long id) {
		try {
			
		Campanha campanha = campanhaRepository.findById(id).orElseThrow(
				() -> new com.campanha.domain.exception.EntidadeNaoEncontradaException(String.format(message, id)));
		return campanha;
		
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe campanha com id %d ", id));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("campanha com o codigo %d não pode ser removida", id));
		}
		
	}
	
	public List<Campanha> listarCampanhasPorClube(long id) {
		try {
			
		List<Campanha> campanhas = campanhaRepository.listarCampanhasPorClube(id);
				
		return campanhas;
		
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe clube cadastrado com id %d ", id));
		}
		
	}
	
		

	private void verificarPeriodoCampanhas(Campanha campanha) {

		LocalDate plusDate = null;

		List<Campanha> campanhas = campanhaRepository.listarCampanhasPeriodoVigencia(campanha.getDataInicio(),
				campanha.getDataVigencia());

		if (campanhas.size() > 0 && campanhas != null) {
			for (Campanha campanha2 : campanhas) {
				plusDate = campanha2.getDataVigencia().plusDays(1);
				campanha2.setDataVigencia(plusDate);
				if (campanha2.getDataVigencia().equals(campanha.getDataVigencia())) {
					plusDate = campanha2.getDataVigencia().plusDays(1);

				}
				while (isDataFimPeriodoVigencia(campanha2, plusDate)) {
					plusDate = campanha2.getDataVigencia().plusDays(1);
					campanha2.setDataVigencia(plusDate);
					System.out.println(campanha2.getDataVigencia().plusDays(1));
					System.out.println(plusDate.toString());
				}
				campanha2.setDataVigencia(plusDate);
				atualizarCampanha(campanha2);
			}
		}
	}

	private void atualizarCampanha(Campanha campanhaPeriodo) {

		Optional<Campanha> campanha = campanhaRepository.findById(campanhaPeriodo.getIdCampanha());

		if (campanha.isPresent()) {

			BeanUtils.copyProperties(campanhaPeriodo, campanha.get(), "idCampanha");

			campanhaRepository.save(campanha.get());
		}
	}

	private boolean isDataFimPeriodoVigencia(Campanha campanhaValidator, LocalDate data) {

		List<Campanha> campanhas = campanhaRepository.listarVigencias(data);

		if (campanhas != null && campanhas.size() > 0) {
			for (Campanha campanha : campanhas) {
				if (!campanhaValidator.getIdCampanha().equals(campanha.getIdCampanha())) {

					return true;

				}
			}
		}

		return false;
	}
	
	

}
