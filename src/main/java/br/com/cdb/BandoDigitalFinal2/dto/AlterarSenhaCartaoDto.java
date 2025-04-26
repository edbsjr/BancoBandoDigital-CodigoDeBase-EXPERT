package br.com.cdb.BandoDigitalFinal2.dto;

public class AlterarSenhaCartaoDto {

	private Long idCartao;
	private String senhaAntiga;
	private String senhaNova;
	private String senhaConfirmada;
	
	
	
	public AlterarSenhaCartaoDto() {
		super();
	}
	public AlterarSenhaCartaoDto(Long idCartao, String senhaAntiga, String senhaNova, String senhaConfirmada) {
		super();
		this.idCartao = idCartao;
		this.senhaAntiga = senhaAntiga;
		this.senhaNova = senhaNova;
		this.senhaConfirmada = senhaConfirmada;
	}
	public Long getIdCartao() {
		return idCartao;
	}
	public void setIdCartao(Long idCartao) {
		this.idCartao = idCartao;
	}
	public String getSenhaAntiga() {
		return senhaAntiga;
	}
	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
	public String getSenhaNova() {
		return senhaNova;
	}
	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
	public String getSenhaConfirmada() {
		return senhaConfirmada;
	}
	public void setSenhaConfirmada(String senhaConfirmada) {
		this.senhaConfirmada = senhaConfirmada;
	}
	
	
}
