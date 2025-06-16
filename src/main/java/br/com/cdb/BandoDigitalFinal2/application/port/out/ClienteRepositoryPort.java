package br.com.cdb.BandoDigitalFinal2.application.port.out;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;

import java.util.Optional;

public interface ClienteRepositoryPort {
    public Optional<Cliente> findById(Long idCliente);
}
