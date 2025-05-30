package br.com.cdb.BandoDigitalFinal2.dto;


import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;

public class AdicionarCartaoDto {

	private Long contaId;
	private String senha;
	private TipoCartao tipo;
	
	public AdicionarCartaoDto() {
	}
	
	public AdicionarCartaoDto(Long contaId, String senha, TipoCartao tipo) {
		super();
		this.contaId = contaId;
		this.senha = senha;
		this.tipo = tipo;
	}
	public Long getContaId() {
		return contaId;
	}
	public void setContaId(Long contaId) {
		this.contaId = contaId;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public TipoCartao getTipo() {return tipo;}
	public void setTipo(TipoCartao tipo) {this.tipo = tipo;}
}
