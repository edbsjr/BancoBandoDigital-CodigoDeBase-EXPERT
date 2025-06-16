package br.com.cdb.BandoDigitalFinal2.dto.out;

import br.com.cdb.BandoDigitalFinal2.enums.Categoria;

import java.time.LocalDate;

public class ClienteResponseDto {
    private Long idCliente;
    private String nome;
    private String cpf;
    private LocalDate dataNasc;
    private Categoria categoria;

    public ClienteResponseDto(Long idCliente, String nome, String cpf, LocalDate dataNasc, Categoria categoria){
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNasc = dataNasc;
        this.categoria = categoria;
    }
    public ClienteResponseDto(){}

    public Long getIdCliente() {return idCliente;}
    public void setIdCliente(Long idCliente) {this.idCliente = idCliente;}
    public String getNome() {return nome;}
    public void setNome(String nome){this.nome = nome;}
    public String getCpf(){return cpf;}
    public void setCpf(String cpf){this.cpf = cpf;}
    public LocalDate getDataNasc(){return dataNasc;}
    public void setDataNasc(LocalDate dataNasc){this.dataNasc = dataNasc;}
    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria){this.categoria = categoria;}
}
