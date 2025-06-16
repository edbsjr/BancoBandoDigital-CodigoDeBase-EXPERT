package br.com.cdb.BandoDigitalFinal2.application.port.in;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;

public interface ClienteInputPort {
    public Cliente buscarCliente(Long idCliente);
}
