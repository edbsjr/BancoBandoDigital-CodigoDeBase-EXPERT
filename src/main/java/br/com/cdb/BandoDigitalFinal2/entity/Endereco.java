package br.com.cdb.BandoDigitalFinal2.entity;

import br.com.cdb.BandoDigitalFinal2.enums.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Embeddable
public class Endereco {
	

	private String rua;
	@Column(nullable = false)
	private Integer num;
	private String complemento;
	private String cidade;
	@Enumerated(EnumType.STRING)
	private Estado estado;
	private String cep;

	//TODO GARANTIR QUE O ENUM MOSTRE AS OPCOES APENAS SEGUNDO O ESTADO
	
	public Endereco() {}
	
	public Endereco(String rua, Integer num, String complemento, String cidade, Estado estado, String cep) {
		super();
		this.rua = rua;
		this.num = num;
		this.complemento = complemento;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public int getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

}
