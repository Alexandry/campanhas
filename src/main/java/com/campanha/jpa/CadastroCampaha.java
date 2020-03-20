package com.campanha.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.campanha.domain.model.Campanha;

@Component
public class CadastroCampaha {
	
	
	@PersistenceContext
	private EntityManager em;

	
	public List<Campanha> listarCampanhas(){
		
	 return em.createQuery("from Campanha", Campanha.class).getResultList();
		
	}
	
	@Transactional
	public Campanha salvarCampanha(Campanha campanha) {
		
		return em.merge(campanha);
	}
	
	
	public Campanha ListarCampanhaById(Long id) {
		
		return em.find(Campanha.class, id);
	}
	
	@Transactional
	public void removerCampanha(Campanha campanha) {
		campanha = ListarCampanhaById(campanha.getIdCampanha());
		
		em.remove(campanha);
		
	}
}
