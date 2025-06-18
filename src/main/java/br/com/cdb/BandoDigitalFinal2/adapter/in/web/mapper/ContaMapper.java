package br.com.cdb.BandoDigitalFinal2.adapter.in.web.mapper;

import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.dto.in.ContaRequestDto;
import br.com.cdb.BandoDigitalFinal2.dto.out.ContaResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContaMapper {
    public ContaResponseDto toResponseDto(Conta conta){
        ContaResponseDto contaResponseDto = new ContaResponseDto();
        contaResponseDto.setIdConta(conta.getIdConta());
        contaResponseDto.setIdCliente(conta.getIdCliente());
        contaResponseDto.setSaldo(conta.getSaldo());
        contaResponseDto.setTipoConta(conta.getTipoConta());
        contaResponseDto.setRendimento(conta.getRendimento());
        contaResponseDto.setManutencao(conta.getManutencao());
        return contaResponseDto;
    }

    public Conta toModel(ContaRequestDto contaRequestDto){
        Conta conta = new Conta();
        conta.setIdConta(contaRequestDto.getIdConta());
        conta.setIdCliente(contaRequestDto.getIdCliente());
        conta.setSaldo(contaRequestDto.getSaldo());
        conta.setTipoConta(contaRequestDto.getTipoConta());
        conta.setRendimento(contaRequestDto.getRendimento());
        conta.setManutencao(contaRequestDto.getManutencao());
        return conta;
    }

    public List<ContaResponseDto> toResponseDtoList(List<Conta> listaDeConta){
        return listaDeConta.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
