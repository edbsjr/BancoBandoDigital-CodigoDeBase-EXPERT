package br.com.cdb.BandoDigitalFinal2.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.BandoDigitalFinal2.dto.AdicionarContaCorrenteDto;
import br.com.cdb.BandoDigitalFinal2.dto.AdicionarContaPoupancaDto;
import br.com.cdb.BandoDigitalFinal2.dto.NumeroValorDto;
import br.com.cdb.BandoDigitalFinal2.dto.TransferenciaBancariaDto;
import br.com.cdb.BandoDigitalFinal2.entity.Conta;
import br.com.cdb.BandoDigitalFinal2.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaService contaService;

	
	@PostMapping("/add/{idCliente}/corrente") 
	public ResponseEntity<String> addContaCorrente(@PathVariable Long idCliente) // INFORMA CLIENTE PARA ABRIR CONTA CORRENTE 
	{
		
		try {
		contaService.addContaCorrente(idCliente);
		return new ResponseEntity<>("Conta Corrente adicionada com sucesso", HttpStatus.CREATED);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add/{idCliente}/poupanca")
	public ResponseEntity<String> addContaPoupanca(@PathVariable Long idCliente) //INFORMA CLIENTE PARA ABRIR CONTA POUPANCA 
	{
		
		try {
		contaService.addContaPoupanca(idCliente);
		return new ResponseEntity<>("Conta Poupanca adicionada com sucesso", HttpStatus.CREATED);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/listarTodos")
	public ResponseEntity<List<Conta>> listarTodos() //RETORNA TODOS AS CONTAS
	{
		List<Conta> listaDeContas = contaService.listarTodos();
		
		return new ResponseEntity<List<Conta>>(listaDeContas, HttpStatus.OK);
	}
	
	@GetMapping("/{idConta}")
	public ResponseEntity<Conta> obterConta(@PathVariable Long idConta) //RETORNA JSON(CONTA) UNICA PARA CONFERIR
	{
		try {
		Conta contaAchada = contaService.obterDetalhes(idConta);
			return new ResponseEntity<Conta>(contaAchada, HttpStatus.FOUND);
		} catch(NoSuchElementException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}
	}

	@PostMapping("/transferencia")
	public ResponseEntity<String> transferencia(@RequestBody TransferenciaBancariaDto transferencia) //REALIZA TRANSFERENCIA ENTRE ID's 
	{
		try {
			contaService.transferirValor(transferencia.getIdOrigem(), transferencia.getValor(),
					transferencia.getIdDestino());
			return new ResponseEntity<>("Transferência realizada com sucesso.", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO ENTENDER MELHOR E MUDAR ISSO
																					// TALVEZ
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Erro ao realizar transferência: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} /*catch (NoSuchElementException e) { //TODO
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}*/
	}

	@GetMapping("/{idConta}/saldo") 
	public ResponseEntity<?> obterSaldoConta(@PathVariable Long idConta) //METODO ALTERNATIVO
	{
		ResponseEntity<?> detalhesResponse = obterConta(idConta); // Wildcard (?) CORINGA
		
		if (detalhesResponse.getStatusCode() == HttpStatus.FOUND) {
			Conta conta = (Conta) detalhesResponse.getBody();
			return new ResponseEntity<>("Saldo da conta: " + conta.getSaldo(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Falha ao buscar conta e saldo",HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/pix")
	public ResponseEntity<?> pagarPix(@RequestBody NumeroValorDto numeroValorDto)
	{
		try {
		contaService.pagarPix(numeroValorDto.getNumero(), numeroValorDto.getValor());//TODO TRATAR ERROS
		return new ResponseEntity<>("Pagamento Realizado", HttpStatus.OK);
		
		} catch(RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*OUTRA FORMA DE FAZER EM BAIXO
		ResponseEntity<?> detalhesResponse = obterConta(idConta);
		if (detalhesResponse.getStatusCode() == HttpStatus.FOUND) 
		{
			Conta conta = (Conta) detalhesResponse.getBody();
			if(conta.getSaldo()>= valor) {
				conta.setSaldo(conta.getSaldo()-valor);
			    return new ResponseEntity<>("Pagamento de R$"+valor+" realizado no PIX", HttpStatus.OK);
			} else
				return new ResponseEntity<>("Saldo insuficiente", HttpStatus.NOT_ACCEPTABLE);
		} else
			return detalhesResponse; //TODO VERIFICAR SE ESTA MOSTRANDO A MENSAGEM EM FALHA*/
		
	}
	
	@PostMapping("/deposito")
	public ResponseEntity<String> depositar(@RequestBody NumeroValorDto numeroValorDto)
	{
		try {
		contaService.depositar(numeroValorDto.getNumero(), numeroValorDto.getValor()); //TODO TRATAR ERROS
		return new ResponseEntity<>("Deposito Realizado com sucesso", HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>("Conta não encontrada: " + e.getMessage(),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) 
		{ 
			return new ResponseEntity<>("Conta ou valor invalido: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
		/*ResponseEntity<?> detalhesResponse = obterConta(idConta);
		if(detalhesResponse.getStatusCode() == HttpStatus.FOUND)
			{
			Conta conta = (Conta) detalhesResponse.getBody();
			if(conta.getSaldo()>= valor) {
				conta.setSaldo(conta.getSaldo()-valor);
			    return new ResponseEntity<>("Pagamento de R$"+valor+" realizado no PIX", HttpStatus.OK);
			} else
				return new ResponseEntity<>("Saldo insuficiente", HttpStatus.NOT_ACCEPTABLE);
			}
		else
			return detalhesResponse;*/
		
	}
	
	@PostMapping("/saque")
	public ResponseEntity<?> sacar(@RequestBody NumeroValorDto numeroValorDto)
	{
		try {
		contaService.sacar(numeroValorDto.getNumero(), numeroValorDto.getValor());
		return new ResponseEntity<>("Saque realizado com sucesso", HttpStatus.OK);
		} catch(RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{idConta}/manutencao")
	public ResponseEntity<String> manutencao(@PathVariable Long idConta)
	{
		contaService.manutencao(idConta);
		return new ResponseEntity<>("Manutençãp aplicada com sucesso", HttpStatus.OK);
	}

	@PutMapping("/{idConta}/rendimento")
	public ResponseEntity<String> rendimento(@PathVariable Long idConta)
	{
		contaService.rendimento(idConta);
		return new ResponseEntity<>("Rendimentos aplicado com sucesso", HttpStatus.OK);
	}
}
