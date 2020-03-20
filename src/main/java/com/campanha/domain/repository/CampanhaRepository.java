package com.campanha.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campanha.domain.model.Campanha;

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Long>, CampanhaRepositoriesQueries {
	
	

}
