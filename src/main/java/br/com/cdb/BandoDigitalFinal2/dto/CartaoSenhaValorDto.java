package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class CartaoSenhaValorDto {

	private String senha;
	private BigDecimal valor;
	
	
	
	public CartaoSenhaValorDto() {
	}
	public CartaoSenhaValorDto(String senha, BigDecimal valor) {
		this.senha = senha;
		this.valor = valor;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
