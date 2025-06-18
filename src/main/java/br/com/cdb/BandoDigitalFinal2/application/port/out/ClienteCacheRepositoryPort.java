package br.com.cdb.BandoDigitalFinal2.application.port.out;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;

import java.util.Optional;

public interface ClienteCacheRepositoryPort {
    public void putById(Long id, Cliente cliente);
    public Optional<Cliente> getById(Long id);
    public void evictById(Long id);
    public void evictAllClientesCache();
    public boolean containsKey(Long id);
}
