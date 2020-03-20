package com.campanha.domain.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "tb_campanha")
public class Campanha {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCampanha;

	@ApiModelProperty(example = "Avanti Palestra", required = true)
	@NotNull
	@Column(name = "nm_campanha", nullable = false)
	private String nomeCampanha;

	@ApiModelProperty(required = true)
	@Valid
	@NotNull
	@ManyToOne
	@JoinColumn(name = "clube_id", nullable = false)
	private Clube clube;

	@ApiModelProperty(example = "2020-02-03", required = true)
	@Column(name = "dt_inicio")
	private LocalDate dataInicio;

	@ApiModelProperty(example = "2020-02-03", required = true)
	@Column(name = "dt_vigencia")
	private LocalDate dataVigencia;
	
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tb_cliente_campanha", joinColumns = 
	@JoinColumn(name = "campanha_id"), inverseJoinColumns = @JoinColumn(name = "cliente_id"))
	private List<Cliente> clientes = new ArrayList<Cliente>();
	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Long getIdCampanha() {
		return idCampanha;
	}

	public void setIdCampanha(Long idCampanha) {
		this.idCampanha = idCampanha;
	}

	public String getNomeCampanha() {
		return nomeCampanha;
	}

	public void setNomeCampanha(String nomeCampanha) {
		this.nomeCampanha = nomeCampanha;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public LocalDate getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(LocalDate dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCampanha == null) ? 0 : idCampanha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campanha other = (Campanha) obj;
		if (idCampanha == null) {
			if (other.idCampanha != null)
				return false;
		} else if (!idCampanha.equals(other.idCampanha))
			return false;
		return true;
	}

}
