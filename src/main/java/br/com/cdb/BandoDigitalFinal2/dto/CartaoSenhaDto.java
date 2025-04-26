package br.com.cdb.BandoDigitalFinal2.dto;

public class CartaoSenhaDto {

	private Long idCartao;
	private String senha;
	public CartaoSenhaDto(Long idCartao, String senha) {
		super();
		this.idCartao = idCartao;
		this.senha = senha;
	}
	public CartaoSenhaDto() {
		super();
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
	
	
}
