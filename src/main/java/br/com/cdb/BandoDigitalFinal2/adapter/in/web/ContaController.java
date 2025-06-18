package br.com.cdb.BandoDigitalFinal2.adapter.in.web;

import br.com.cdb.BandoDigitalFinal2.adapter.in.web.mapper.ContaMapper;
import br.com.cdb.BandoDigitalFinal2.application.port.in.ContaInputPort;
import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.dto.ContaTipoDto;
import br.com.cdb.BandoDigitalFinal2.dto.NumeroValorDto;
import br.com.cdb.BandoDigitalFinal2.dto.TransferenciaBancariaDto;
import br.com.cdb.BandoDigitalFinal2.application.service.ContaService;
import br.com.cdb.BandoDigitalFinal2.dto.out.ContaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaService contaService;
	@Autowired
	private ContaInputPort contaInputPort;
	@Autowired
	private ContaMapper contaMapper;
	
	@PostMapping("/add")
	public ResponseEntity<String> addConta(@RequestBody ContaTipoDto contaTipoDto) // INFORMA CLIENTE PARA ABRIR CONTA CORRENTE
	{
		contaInputPort.addConta(contaTipoDto.getIdCliente(), contaTipoDto.getTipoConta());
		return new ResponseEntity<>("Conta adicionada com sucesso", HttpStatus.CREATED);
	}
	
	@GetMapping("/listarTodos")
	public ResponseEntity<List<ContaResponseDto>> listarTodos() //RETORNA TODOS AS CONTAS
	{
		List<Conta> listaDeContas = contaInputPort.listarTodos();
		return new ResponseEntity<>(contaMapper.toResponseDtoList(listaDeContas), HttpStatus.OK);
	}
	
	@GetMapping("/{idConta}")
	public ResponseEntity<ContaResponseDto> obterConta(@PathVariable Long idConta) //RETORNA JSON(CONTA) UNICA PARA CONFERIR
	{
		Conta contaAchada = contaInputPort.buscarConta(idConta);
			return new ResponseEntity<>(contaMapper.toResponseDto(contaAchada), HttpStatus.FOUND);
	}

	@PostMapping("/transferencia")
	public ResponseEntity<String> transferencia(@RequestBody TransferenciaBancariaDto transferencia) //REALIZA TRANSFERENCIA ENTRE ID's 
	{
		contaInputPort.transferirValor(transferencia.getIdOrigem(), transferencia.getValor(),
				transferencia.getIdDestino());
		return new ResponseEntity<>("Transferência realizada com sucesso.", HttpStatus.OK);
	}

	@GetMapping("/{idConta}/saldo") 
	public ResponseEntity<String> obterSaldoConta(@PathVariable Long idConta) //METODO ALTERNATIVO
	{
		return new ResponseEntity<>("Saldo da conta: " + contaInputPort.consultarSaldo(idConta), HttpStatus.OK);
	}
	
	@PostMapping("/pix")
	public ResponseEntity<String> pagarPix(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaInputPort.pagarPix(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Pagamento Realizado", HttpStatus.OK);
	}
	
	@PostMapping("/deposito")
	public ResponseEntity<String> depositar(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaInputPort.depositar(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Deposito Realizado com sucesso", HttpStatus.OK);
	}
	
	@PostMapping("/saque")
	public ResponseEntity<?> sacar(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaInputPort.sacar(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Saque realizado com sucesso", HttpStatus.OK);
	}
	
	@PutMapping("/{idConta}/manutencao")
	public ResponseEntity<String> manutencao(@PathVariable Long idConta)
	{
		contaInputPort.manutencao(idConta);
		return new ResponseEntity<>("Manutenção aplicada com sucesso", HttpStatus.OK);
	}

	@PutMapping("/{idConta}/rendimento")
	public ResponseEntity<String> rendimento(@PathVariable Long idConta)
	{
		contaInputPort.rendimento(idConta);
		return new ResponseEntity<>("Rendimento aplicado com sucesso", HttpStatus.OK);
	}
}
