package com.campanha.infrastructure.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.campanha.domain.model.Campanha;
import com.campanha.domain.repository.CampanhaRepositoriesQueries;

@Repository
public class CampanhaRepositoryImpl implements CampanhaRepositoriesQueries {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Campanha> listarCampanhasAtivas() {

		LocalDate date = LocalDate.now();

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Campanha> criteria = builder.createQuery(Campanha.class);

		Root<Campanha> root = criteria.from(Campanha.class);

		criteria.where(builder.greaterThanOrEqualTo(root.get("dataVigencia"), date));
		TypedQuery<Campanha> query = em.createQuery(criteria);

		return query.getResultList();
	}

	@Override
	public List<Campanha> listarCampanhasPeriodoVigencia(LocalDate dataInicio,LocalDate dataVigencia) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		CriteriaQuery<Campanha> criteria = builder.createQuery(Campanha.class);

		Root<Campanha> root = criteria.from(Campanha.class);
		
		List<Predicate> predicates = new ArrayList<>(); 
						
				if (dataInicio != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("dataInicio"), dataInicio));			
				}
				if (dataVigencia != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("dataVigencia"), dataVigencia));
					
				}
		
		criteria.where(predicates.toArray(new Predicate [0]));
		criteria.orderBy(builder.asc(root.get("dataVigencia")));
		
		TypedQuery<Campanha> query = em.createQuery(criteria);
			

		return query.getResultList();
	}

	@Override
	public List<Campanha> listarVigencias(LocalDate date) {
		

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Campanha> criteria = builder.createQuery(Campanha.class);

		Root<Campanha> root = criteria.from(Campanha.class);
		
		criteria.where(builder.equal(root.get("dataVigencia"), date));
		
		TypedQuery<Campanha> query = em.createQuery(criteria);

		return query.getResultList();
	}
	
	@Override
	public List<Campanha> listarCampanhasPorClube(long id) {
		
		StringBuilder jpql =  new StringBuilder();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		
		jpql.append("from Campanha where clube_id = :id");
		parametros.put("id", id);
		
		
		TypedQuery<Campanha> query = em.createQuery(jpql.toString(), Campanha.class);
		parametros.forEach((key, value) -> query.setParameter(key, value));
		

		return query.getResultList();
	}

}
