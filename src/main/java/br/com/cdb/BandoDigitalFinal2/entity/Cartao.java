package br.com.cdb.BandoDigitalFinal2.entity;

import java.math.BigDecimal;

import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String senha;

	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "contaId")
	private Conta conta;
	
	

	public Cartao() {

	}

	public Cartao(Long id, String senha, Situacao situacao, Conta conta) {
		this.id = id;
		this.senha = senha;
		this.situacao = situacao;
		this.conta = conta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	public abstract void pagar(BigDecimal valor);

	
}
