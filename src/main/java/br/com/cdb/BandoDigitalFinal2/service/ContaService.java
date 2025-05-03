package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.dao.ContaDao;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


//TODO CRIAR COMANDO RESETAR LIMITE DIARIO PARA LIBERAR LIMITE NO FINAL DO MES

@Service
public class ContaService {

	

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private ContaDao contaDao;
	
	//ABERTURA DE CONTAS
	public boolean addContaCorrente(Long clienteId) //RETORNO DE EXCEPTION DIFERENTE DA CONTA POUPANCA
	{
		ContaEntity conta = new ContaEntity();
		Cliente clienteEncontrado= clienteDao.findById(clienteId);
		conta.setSaldo(BigDecimal.ZERO);
		conta.setIdCliente(clienteEncontrado.getIdCliente());
		conta.setTipoConta(TipoConta.CORRENTE);
		BigDecimal taxa = null;
		switch( clienteEncontrado.getCategoria()) {
		
		case COMUM:
			taxa = BigDecimal.valueOf(12.00);
			conta.setManutencao(taxa);
			break;
		case SUPER:
			taxa = BigDecimal.valueOf(8.00);
			conta.setManutencao(taxa);
			break;
		case PREMIUM:
			taxa = BigDecimal.valueOf(0.00);
			conta.setManutencao(taxa);
			break;
		}
		
		return contaDao.save(conta);
		
	}	
	public boolean addContaPoupanca(Long clienteId) 
	{
		ContaEntity conta = new ContaEntity();
		Cliente clienteEncontrado = clienteDao.findById(clienteId);;
		conta.setSaldo(BigDecimal.ZERO);
		conta.setIdCliente(clienteEncontrado.getIdCliente());
		conta.setTipoConta(TipoConta.POUPANCA);
		BigDecimal rendimento = null;
		switch( clienteEncontrado.getCategoria()) {
		
		case COMUM:
			rendimento = BigDecimal.valueOf(0.500);
			conta.setRendimento(rendimento);
			break;
		case SUPER:
			rendimento = BigDecimal.valueOf(0.700);
			conta.setRendimento(rendimento);
			break;
		case PREMIUM:
			rendimento = BigDecimal.valueOf(0.900);
			conta.setRendimento(rendimento);
			break;
		}
		return contaDao.save(conta);
	}
	
	//DETALHES DE CONTAS
	public List<ContaEntity> listarTodos(){
		
		return contaDao.findAll(); 
		}
	public ContaEntity obterDetalhes(Long idConta) 
	{
		ContaEntity conta = contaDao.findById(idConta);
		
		return conta;
	}
	public BigDecimal consultarSaldo(Long idConta) 
	{
		ContaEntity conta = contaDao.findById(idConta);
		return conta.getSaldo();
		
	}
	
	//TRANSFERENCIA ENTRE CONTAS
		public void transferirValor(Long idOrigem, BigDecimal valor, Long idDestino)
	{
		ContaEntity contaOrigem = contaDao.findById(idOrigem);
		
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		if (contaOrigem.getSaldo().compareTo(valor)>=0) {
		
		ContaEntity contaDestino = contaDao.findById(idDestino);
		
			if(contaDestino != null) 
			{
				contaOrigem.debitar(valor);
				contaDestino.creditar(valor);
				contaDao.update(contaDestino);
				contaDao.update(contaOrigem);
			}
		}
		else
			throw new RuntimeException("Saldo insuficiente");
		
	}
	

	//MOVIMENTACAO DE CONTA
	public void pagarPix(Long idConta, BigDecimal valor) 
	{
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		debitarValor(idConta, valor);		
	}

	public void depositar(Long idConta, BigDecimal valor) 
	{
		ContaEntity conta = contaDao.findById(idConta);
		if(conta != null && valor.compareTo(BigDecimal.ZERO) > 0) {
			conta.setSaldo(conta.getSaldo().add(valor));
			contaDao.update(conta);
		}
		else
			throw new RuntimeException("Conta ou valor invalidos");
	}

	public void sacar(Long idConta, BigDecimal valor) 
	{
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new RuntimeException("Não é permitido VALOR < 0");
		debitarValor(idConta, valor);
	}
	
	//MOVIMENTACAO INTERNA DE CONTA
	public void manutencao(Long idConta)
	{
		ContaEntity conta = obterConta(idConta);
		debitarValor(idConta, conta.getManutencao());
	}

	public void rendimento(Long idConta) 
	{
		ContaEntity conta = obterConta(idConta);
		conta.setSaldo(conta.getSaldo().add(conta.getSaldo().multiply(conta.getRendimento().divide(BigDecimal.valueOf(100.00)))));
		contaDao.update(conta);
	}
	

	//METODOS GENERICOS
	private ContaEntity obterConta(Long idConta)
	{
		return contaDao.findById(idConta);
	}
	private void debitarValor(Long idConta, BigDecimal valor) 
	{
		ContaEntity conta = obterConta(idConta);
		if(conta.getSaldo().compareTo(valor)>=0) {
			conta.setSaldo(conta.getSaldo().subtract(valor));
			contaDao.update(conta);
		} else{
			throw new RuntimeException("Saldo insuficiente");}
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
