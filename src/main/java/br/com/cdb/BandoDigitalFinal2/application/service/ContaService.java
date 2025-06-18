package br.com.cdb.BandoDigitalFinal2.application.service;

import br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.ContaDao;
import br.com.cdb.BandoDigitalFinal2.application.port.in.ContaInputPort;
import br.com.cdb.BandoDigitalFinal2.application.port.out.ContaRepositoryPort;
import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;
import br.com.cdb.BandoDigitalFinal2.exceptions.CampoInvalidoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.SaldoInsuficienteException;
import br.com.cdb.BandoDigitalFinal2.exceptions.ValorNegativoNaoPermitidoException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContaService implements ContaInputPort {

	private static final Logger log = LoggerFactory.getLogger(ContaService.class);
	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private ContaRepositoryPort contaRepositoryPort;

	//ABERTURA DE CONTAS
	@Override
	public void addConta(Long clienteId, TipoConta tipoConta)
	{
		Conta conta = new Conta();
		log.info("Verificando se Cliente ID {} existe", clienteId);
		Optional<Cliente> clienteEncontrado = clienteDao.findById(clienteId);
		if(clienteEncontrado.isEmpty())
			throw new RegistroNaoEncontradoException("Registro de Cliente"+clienteId+" não encontrado");
		log.info("Setando informaçoes da Conta para a criacao");
		conta.setSaldo(BigDecimal.ZERO);
		conta.setIdCliente(clienteEncontrado.get().getIdCliente());
		conta.setTipoConta(tipoConta);
		if(conta.getTipoConta().equals(TipoConta.CORRENTE)) {
			BigDecimal taxa = null;
			switch (clienteEncontrado.get().getCategoria()) {

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
			contaRepositoryPort.save(conta);
		} else if (conta.getTipoConta().equals(TipoConta.POUPANCA))
		{
			BigDecimal rendimento = null;
			switch( clienteEncontrado.get().getCategoria()) {

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
			contaRepositoryPort.save(conta);
		}
	}
	
	//DETALHES DE CONTAS
	@Override
	public List<Conta> listarTodos()
	{
		log.info("Buscando a lista de todas as contas");
		return contaRepositoryPort.findAll();
	}

	@Override
	public Conta buscarConta(Long idConta) {
		log.info("Iniciando busca da Conta ID {}", idConta);
		Conta conta = contaRepositoryPort.findById(idConta);
		if(conta == null){
			log.error("Conta ID {} nao encontrada", idConta);
			throw new RegistroNaoEncontradoException("Conta ID "+idConta+" nao encontrada");
		}
		else
		{
			log.info("Retornando conta {} id {}", conta.getTipoConta().name(), conta.getIdConta());
			return conta;
		}
	}

	@Override
	public BigDecimal consultarSaldo(Long idConta)
	{
		Conta conta = buscarConta(idConta);
		return conta.getSaldo();
		
	}
	
	//TRANSFERENCIA ENTRE CONTAS
	@Override
	public void transferirValor(Long idOrigem, BigDecimal valor, Long idDestino)
	{
		log.info("Preparando para buscar Conta de Origem ID {}", idOrigem);
		Conta contaOrigem = buscarConta(idOrigem);
		validaValor(valor);
		if (contaOrigem.getSaldo().compareTo(valor)>=0) {
			log.info("Preparando para buscar Conta de Destino ID {}", idDestino);
			Conta contaDestino = buscarConta(idDestino);
		
			if(contaDestino != null) 
			{
				log.info("Realizando operacoes de Debito e credito");
				contaOrigem.debitar(valor);
				contaDestino.creditar(valor);
				log.info("Preparando para atualizar informacoes no DAO");
				contaRepositoryPort.update(contaOrigem);
				contaRepositoryPort.update(contaDestino);
			}
		}
		else
		{
			log.error("A conta origem ID {} nao possui saldo suficiente", idOrigem);
			throw new SaldoInsuficienteException("Saldo insuficiente");
		}
		
	}
	

	//MOVIMENTACAO DE CONTA
	@Override
	public void pagarPix(Long idConta, BigDecimal valor) 
	{
		validaValor(valor);
		debitarValor(idConta, valor);		
	}

	@Override
	public void depositar(Long idConta, BigDecimal valor) 
	{
		Conta conta = buscarConta(idConta);
		validaValor(valor);
		conta.setSaldo(conta.getSaldo().add(valor));
		contaRepositoryPort.update(conta);
	}

	@Override
	public void sacar(Long idConta, BigDecimal valor) 
	{
		debitarValor(idConta, valor);
	}
	
	//MOVIMENTACAO INTERNA DE CONTA
	@Override
	public void manutencao(Long idConta)
	{
		log.info("Iniciando processo de manutencao de Conta Corrente. Verificando se a conta existe");
		Conta conta = buscarConta(idConta);
		if(conta.getTipoConta().equals(TipoConta.CORRENTE)) {
			log.info("Iniciando o desconto da taxa de manutencao na conta");
			debitarValor(idConta, conta.getManutencao());
		}
		else {
			log.error("Operacao valida somente para conta Corrente");
			throw new CampoInvalidoException("Operacao valida somente para Conta Corrente");
		}
	}

	@Override
	public void rendimento(Long idConta) 
	{
		Conta conta = buscarConta(idConta);
		if(conta.getTipoConta().equals(TipoConta.POUPANCA))
		{
		conta.setSaldo(conta.getSaldo().add(conta.getSaldo().multiply(conta.getRendimento().divide(BigDecimal.valueOf(100.00)))));
		contaRepositoryPort.update(conta);
		} else {
			log.error("Operacao valida somente para conta Poupanca");
			throw new CampoInvalidoException("Operacao valida somente para Conta Poupanca");
		}
	}
	

	//METODOS GENERICOS
	private void debitarValor(Long idConta, BigDecimal valor) 
	{
		log.info("Preparando para buscar Conta ID {}", idConta);
		Conta conta = buscarConta(idConta);
		validaValor(valor);
		if(conta.getSaldo().compareTo(valor)>=0) {
			conta.setSaldo(conta.getSaldo().subtract(valor));
			contaRepositoryPort.update(conta);
		} else{
			log.error("Saldo em conta insuficiente");
			throw new SaldoInsuficienteException("Saldo insuficiente");
		}
	}
	
	/*private void creditarValor(Long idConta, Double valor) 
	{
		Conta conta = obterConta(idConta);
		if(valor > 0)
			conta.setSaldo(conta.getSaldo()+valor);
		else
			throw new RuntimeException("Valor invalido");
	}*/

	private void validaValor(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO)<0)
			throw new ValorNegativoNaoPermitidoException("Não é permitido VALOR < 0");
	}
}
