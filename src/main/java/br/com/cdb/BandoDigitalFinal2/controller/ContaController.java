package br.com.cdb.BandoDigitalFinal2.controller;

import br.com.cdb.BandoDigitalFinal2.dto.ContaTipoDto;
import br.com.cdb.BandoDigitalFinal2.dto.NumeroValorDto;
import br.com.cdb.BandoDigitalFinal2.dto.TransferenciaBancariaDto;
import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.service.ContaService;
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

	
	@PostMapping("/add")
	public ResponseEntity<String> addConta(@RequestBody ContaTipoDto contaTipoDto) // INFORMA CLIENTE PARA ABRIR CONTA CORRENTE
	{
		contaService.addConta(contaTipoDto.getIdCliente(), contaTipoDto.getTipoConta());
		return new ResponseEntity<>("Conta adicionada com sucesso", HttpStatus.CREATED);
	}
	
	@GetMapping("/listarTodos")
	public ResponseEntity<List<ContaEntity>> listarTodos() //RETORNA TODOS AS CONTAS
	{
		List<ContaEntity> listaDeContas = contaService.listarTodos();
		return new ResponseEntity<>(listaDeContas, HttpStatus.OK);
	}
	
	@GetMapping("/{idConta}")
	public ResponseEntity<ContaEntity> obterConta(@PathVariable Long idConta) //RETORNA JSON(CONTA) UNICA PARA CONFERIR
	{
		ContaEntity contaAchada = contaService.obterDetalhes(idConta);
			return new ResponseEntity<>(contaAchada, HttpStatus.FOUND);
	}

	@PostMapping("/transferencia")
	public ResponseEntity<String> transferencia(@RequestBody TransferenciaBancariaDto transferencia) //REALIZA TRANSFERENCIA ENTRE ID's 
	{
		contaService.transferirValor(transferencia.getIdOrigem(), transferencia.getValor(),
				transferencia.getIdDestino());
		return new ResponseEntity<>("Transferência realizada com sucesso.", HttpStatus.OK);
	}

	@GetMapping("/{idConta}/saldo") 
	public ResponseEntity<String> obterSaldoConta(@PathVariable Long idConta) //METODO ALTERNATIVO
	{
		return new ResponseEntity<>("Saldo da conta: " + contaService.consultarSaldo(idConta), HttpStatus.OK);
	}
	
	@PostMapping("/pix")
	public ResponseEntity<?> pagarPix(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaService.pagarPix(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Pagamento Realizado", HttpStatus.OK);
	}
	
	@PostMapping("/deposito")
	public ResponseEntity<String> depositar(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaService.depositar(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Deposito Realizado com sucesso", HttpStatus.OK);
	}
	
	@PostMapping("/saque")
	public ResponseEntity<?> sacar(@RequestBody NumeroValorDto numeroValorDto)
	{
		contaService.sacar(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Saque realizado com sucesso", HttpStatus.OK);
	}
	
	@PutMapping("/{idConta}/manutencao")
	public ResponseEntity<String> manutencao(@PathVariable Long idConta)
	{
		contaService.manutencao(idConta);
		return new ResponseEntity<>("Manutenção aplicada com sucesso", HttpStatus.OK);
	}

	@PutMapping("/{idConta}/rendimento")
	public ResponseEntity<String> rendimento(@PathVariable Long idConta)
	{
		contaService.rendimento(idConta);
		return new ResponseEntity<>("Rendimento aplicado com sucesso", HttpStatus.OK);
	}
}
