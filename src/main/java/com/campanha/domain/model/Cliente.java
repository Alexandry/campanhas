package com.campanha.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;


/**
 * Classe que representa a Entidade Cliente utilizando recursos do Spring como:
 * Bean Validation, para validacao dos atributos obrigatorios.
 * Anotacoes Swagger para descrever os atributos na documentacao da API
 * Anotacoes Hibernate que auxiliam na manipulacao de dados junto ao database
 * @author Alexandre
 */
@Entity
@Table(name = "tb_cliente")
public class Cliente implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(example = "Alexandre", required = true)
	@NotNull
	@Column(name = "nm_cliente", nullable = false)
	private String nome;
	
	@ApiModelProperty(example = "Alexandre@dominio.com.br", required = true)
	@Column(name = "ds_email", nullable = false)
	@NotNull
	private String email;
	
	@ApiModelProperty(example = "1990-10-08", required = true)
	@NotNull
	@Column(name = "dt_nascimento", nullable = false)
	private LocalDate dataNascimento;
	
	@JsonIgnoreProperties(value = "nome", allowGetters = true)
	@ApiModelProperty(example = "Palmeiras")
	@Valid
	@NotNull
	@OneToOne
	@JoinColumn(name = "clube_id", nullable = false)
	private Clube clube;
	
	@JsonIgnore
	@OneToMany
	@JoinTable(name = "tb_cliente_campanha", joinColumns = 
	@JoinColumn(name = "cliente_id"), inverseJoinColumns = @JoinColumn(name = "campanha_id"))
	private List<Campanha> campanhas = new ArrayList<Campanha>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public List<Campanha> getCampanhas() {
		return campanhas;
	}

	public void setCampanhas(List<Campanha> campanhas) {
		this.campanhas = campanhas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
