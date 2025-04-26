package br.com.cdb.BandoDigitalFinal2.dto;


public class AdicionarCartaoDto {

	private Long contaId;
	private String senha;
	
	public AdicionarCartaoDto() {
	}
	
	public AdicionarCartaoDto(Long contaId, String senha) {
		super();
		this.contaId = contaId;
		this.senha = senha;
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
	
}
