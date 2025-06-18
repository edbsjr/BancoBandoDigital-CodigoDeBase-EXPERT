package br.com.cdb.BandoDigitalFinal2.adapter.in.web.mapper;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import br.com.cdb.BandoDigitalFinal2.dto.in.ClienteRequestDto;
import br.com.cdb.BandoDigitalFinal2.dto.out.ClienteResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public ClienteResponseDto toResponseDto(Cliente cliente) {
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();
        clienteResponseDto.setIdCliente(cliente.getIdCliente());
        clienteResponseDto.setNome(cliente.getNome());
        clienteResponseDto.setCpf(cliente.getCpf());
        clienteResponseDto.setDataNasc(cliente.getDataNasc());
        clienteResponseDto.setCategoria(cliente.getCategoria());
        return clienteResponseDto;
    }

    public Cliente toModel(ClienteRequestDto clienteRequestDto){
        Cliente cliente = new Cliente();
        cliente.setIdCliente(clienteRequestDto.getIdCliente());
        cliente.setNome(clienteRequestDto.getNome());
        cliente.setCpf(clienteRequestDto.getCpf());
        cliente.setDataNasc(clienteRequestDto.getDataNasc());
        cliente.setCategoria(cliente.getCategoria());
        return cliente;
    }

    public List<ClienteResponseDto> toResponseDtoList(List<Cliente> listaDeCliente){
        return listaDeCliente.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
