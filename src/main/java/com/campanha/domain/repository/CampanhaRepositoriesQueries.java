package com.campanha.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.campanha.domain.model.Campanha;

public interface CampanhaRepositoriesQueries {
	
	
	 List<Campanha> listarCampanhasAtivas();
	 List<Campanha> listarCampanhasPeriodoVigencia(LocalDate dataInicio,LocalDate dataVigencia);
	 List<Campanha> listarVigencias(LocalDate date);
	 List<Campanha> listarCampanhasPorClube(long id);
}
