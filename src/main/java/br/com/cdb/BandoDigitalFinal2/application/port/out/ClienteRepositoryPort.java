package br.com.cdb.BandoDigitalFinal2.application.port.out;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    public Optional<Cliente> findById(Long idCliente);
    public Optional<Cliente> findByCpf(String cpf);
    public boolean save(Cliente cliente);
    public List<Cliente> findAll();
    public boolean update(Cliente cliente);
    public boolean deleteById(Long idCliente);
}
