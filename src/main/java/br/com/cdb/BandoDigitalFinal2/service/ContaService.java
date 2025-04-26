package br.com.cdb.BandoDigitalFinal2.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.Conta;
import br.com.cdb.BandoDigitalFinal2.entity.ContaCorrente;
import br.com.cdb.BandoDigitalFinal2.entity.ContaPoupanca;
import br.com.cdb.BandoDigitalFinal2.repository.ClienteRepository;
import br.com.cdb.BandoDigitalFinal2.repository.ContaRepository;
import jakarta.transaction.Transactional;


//TODO CRIAR COMANDO RESETAR LIMITE DIARIO PARA LIBERAR LIMITE NO FINAL DO MES

@Service
public class ContaService {

	
	@Autowired
	private ContaRepository contaRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	//ABERTURA DE CONTAS
	public Conta addContaCorrente(Long clienteId) //RETORNO DE EXCEPTION DIFERENTE DA CONTA POUPANCA
	{
		ContaCorrente contaCorrente = new ContaCorrente();
		Cliente clienteEncontrado= clienteRepository.findById(clienteId).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
		
		contaCorrente.setCliente(clienteEncontrado);
		
		BigDecimal taxa = null;
		switch( contaCorrente.getCliente().getCategoria()) {
		
		case COMUM:
			taxa = BigDecimal.valueOf(12.00);
			contaCorrente.setTaxa(taxa);
			break;
		case SUPER:
			taxa = BigDecimal.valueOf(8.00);
			contaCorrente.setTaxa(taxa);
			break;
		case PREMIUM:
			taxa = BigDecimal.valueOf(0.00);
			contaCorrente.setTaxa(taxa);
			break;
		}
		
		return contaRepository.save(contaCorrente);
		
	}	
	public Conta addContaPoupanca(Long clienteId) 
	{
		ContaPoupanca contaPoupanca = new ContaPoupanca();
		Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteId);
		
		if(clienteEncontrado != null) {
		contaPoupanca.setCliente(clienteEncontrado.get());
		BigDecimal rendimento = null;
		switch( contaPoupanca.getCliente().getCategoria()) {
		
		case COMUM:
			rendimento = BigDecimal.valueOf(0.500);
			contaPoupanca.setRendimento(rendimento);
			break;
		case SUPER:
			rendimento = BigDecimal.valueOf(0.700);
			contaPoupanca.setRendimento(rendimento);
			break;
		case PREMIUM:
			rendimento = BigDecimal.valueOf(0.900);
			contaPoupanca.setRendimento(rendimento);
			break;
		}
		return contaRepository.save(contaPoupanca);
		}
		else
			throw new NoSuchElementException("Cliente do não encontrado! Informe outro para abertura da conta poupança");
	}
	
	//DETALHES DE CONTAS
	public List<Conta> listarTodos(){
		
		return contaRepository.findAll(); 
		}
	public Conta obterDetalhes(Long idConta) 
	{
		Conta conta = obterConta(idConta);
		
		return conta;
	}
	public BigDecimal consultarSaldo(Long idConta) 
	{
		Conta conta = obterConta(idConta);
		return conta.getSaldo();
		
	}
	
	//TRANSFERENCIA ENTRE CONTAS
	@Transactional
	public void transferirValor(Long idOrigem, BigDecimal valor, Long idDestino) 
	{
		Conta contaOrigem = obterConta(idOrigem);
		
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		if (contaOrigem.getSaldo().compareTo(valor)>=0) {
		
		Conta contaDestino = obterConta(idDestino);
		
			if(contaDestino != null) 
			{
				contaOrigem.debitar(valor);
				contaDestino.creditar(valor);
			}
		}
		else
			throw new RuntimeException("Saldo insuficiente");
		
	}
	

	//MOVIMENTACAO DE CONTA
	@Transactional
	public void pagarPix(Long idConta, BigDecimal valor) 
	{
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		debitarValor(idConta, valor);		
	}
	@Transactional
	public void depositar(Long idConta, BigDecimal valor) 
	{
		Conta conta = contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
		if(conta != null && valor.compareTo(BigDecimal.ZERO) > 0)
			conta.setSaldo(conta.getSaldo().add(valor));
			
		else
			throw new RuntimeException("Conta ou valor invalidos");
	}
	@Transactional
	public void sacar(Long idConta, BigDecimal valor) 
	{
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		debitarValor(idConta, valor);
	}
	
	//MOVIMENTACAO INTERNA DE CONTA
	@Transactional
	public void manutencao(Long idConta) 
	{
		ContaCorrente conta = (ContaCorrente) obterConta(idConta);
		debitarValor(idConta, conta.getTaxa());
	}
	@Transactional
	public void rendimento(Long idConta) 
	{
		ContaPoupanca conta = (ContaPoupanca)obterConta(idConta);
		conta.setSaldo(conta.getSaldo().add(conta.getSaldo().multiply(conta.getRendimento().divide(BigDecimal.valueOf(100.00)))));
	}
	

	//METODOS GENERICOS
	private Conta obterConta(Long idConta) 
	{
		return contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
	}
	private void debitarValor(Long idConta, BigDecimal valor) 
	{
		Conta conta = obterConta(idConta);
		if(conta.getSaldo().compareTo(valor)>=0)
			conta.setSaldo(conta.getSaldo().subtract(valor));
		else
			throw new RuntimeException("Saldo insuficiente");
	}
	
	/*private void creditarValor(Long idConta, Double valor) 
	{
		Conta conta = obterConta(idConta);
		if(valor > 0)
			conta.setSaldo(conta.getSaldo()+valor);
		else
			throw new RuntimeException("Valor invalido");
	}*/
}
