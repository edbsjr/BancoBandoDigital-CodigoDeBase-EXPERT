package br.com.cdb.BandoDigitalFinal2.dto;

import br.com.cdb.BandoDigitalFinal2.enums.Situacao;

public class AlterarSituacaoCartaoDto {

	private Long idCartao;
	private String senha;
	private Situacao situacao;
	
	
	
	public AlterarSituacaoCartaoDto() {
		super();
	}
	public AlterarSituacaoCartaoDto(Long idCartao, String senha, Situacao situacao) {
		super();
		this.idCartao = idCartao;
		this.senha = senha;
		this.situacao = situacao;
	}
	public Long getIdCartao() {
		return idCartao;
	}
	public void setIdCartao(Long idCartao) {
		this.idCartao = idCartao;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Situacao getSituacao() {
		return situacao;
	}
	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	
}
