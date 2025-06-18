package br.com.cdb.BandoDigitalFinal2.application.port.in;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import java.util.List;

public interface ClienteInputPort {
    public Cliente buscarCliente(Long idCliente);
    public void salvarCliente(Cliente cliente);
    public List<Cliente> listarTodos();
    public void atualizarCliente(Cliente cliente);
    public void deletarCliente(Long idCliente);
}
