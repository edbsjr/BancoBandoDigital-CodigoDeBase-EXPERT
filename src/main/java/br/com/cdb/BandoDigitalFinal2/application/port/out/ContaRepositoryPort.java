package br.com.cdb.BandoDigitalFinal2.application.port.out;

import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;

import java.util.List;

public interface ContaRepositoryPort {
    public boolean save(Conta conta);
    public Conta findById(Long idConta);
    public List<Conta> findAll();
    public boolean update(Conta conta);
    public boolean deleteById(Long idConta);
}
