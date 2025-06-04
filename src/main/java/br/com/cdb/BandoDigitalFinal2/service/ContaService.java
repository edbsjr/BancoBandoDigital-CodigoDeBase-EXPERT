package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.dao.ContaDao;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;
import br.com.cdb.BandoDigitalFinal2.exceptions.CampoInvalidoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.SaldoInsuficienteException;
import br.com.cdb.BandoDigitalFinal2.exceptions.ValorNegativoNaoPermitidoException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContaService {

	private static final Logger log = LoggerFactory.getLogger(ContaService.class);
	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private ContaDao contaDao;

	//ABERTURA DE CONTAS
	public void addConta(Long clienteId, TipoConta tipoConta)
	{
		ContaEntity conta = new ContaEntity();
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
			contaDao.save(conta);
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
			contaDao.save(conta);
		}
	}
	
	//DETALHES DE CONTAS
	public List<ContaEntity> listarTodos()
	{
		log.info("Buscando a lista de todas as contas");
		return contaDao.findAll();
	}

	public ContaEntity buscarConta(Long idConta) {
		log.info("Iniciando busca da Conta ID {}", idConta);
		ContaEntity contaEntity= contaDao.findById(idConta);
		if(contaEntity == null){
			log.error("Conta ID {} nao encontrada", idConta);
			throw new RegistroNaoEncontradoException("Conta ID "+idConta+" nao encontrada");
		}
		else
		{
			log.info("Retornando conta {} id {}", contaEntity.getTipoConta().name(), contaEntity.getIdConta());
			return contaEntity;
		}
	}

	public BigDecimal consultarSaldo(Long idConta)
	{
		ContaEntity conta = buscarConta(idConta);
		return conta.getSaldo();
		
	}
	
	//TRANSFERENCIA ENTRE CONTAS
		public void transferirValor(Long idOrigem, BigDecimal valor, Long idDestino)
	{
		log.info("Preparando para buscar Conta de Origem ID {}", idOrigem);
		ContaEntity contaOrigem = buscarConta(idOrigem);
		validaValor(valor);
		if (contaOrigem.getSaldo().compareTo(valor)>=0) {
			log.info("Preparando para buscar Conta de Destino ID {}", idDestino);
			ContaEntity contaDestino = buscarConta(idDestino);
		
			if(contaDestino != null) 
			{
				log.info("Realizando operacoes de Debito e credito");
				contaOrigem.debitar(valor);
				contaDestino.creditar(valor);
				log.info("Preparando para atualizar informacoes no DAO");
				contaDao.update(contaOrigem);
				contaDao.update(contaDestino);
			}
		}
		else
		{
			log.error("A conta origem ID {} nao possui saldo suficiente", idOrigem);
			throw new SaldoInsuficienteException("Saldo insuficiente");
		}
		
	}
	

	//MOVIMENTACAO DE CONTA
	public void pagarPix(Long idConta, BigDecimal valor) 
	{
		validaValor(valor);
		debitarValor(idConta, valor);		
	}

	public void depositar(Long idConta, BigDecimal valor) 
	{
		ContaEntity conta = buscarConta(idConta);
		validaValor(valor);
		conta.setSaldo(conta.getSaldo().add(valor));
		contaDao.update(conta);
		}

	public void sacar(Long idConta, BigDecimal valor) 
	{
		debitarValor(idConta, valor);
	}
	
	//MOVIMENTACAO INTERNA DE CONTA
	public void manutencao(Long idConta)
	{
		log.info("Iniciando processo de manutencao de Conta Corrente. Verificando se a conta existe");
		ContaEntity conta = buscarConta(idConta);
		if(conta.getTipoConta().equals(TipoConta.CORRENTE)) {
			log.info("Iniciando o desconto da taxa de manutencao na conta");
			debitarValor(idConta, conta.getManutencao());
		}
		else {
			log.error("Operacao valida somente para conta Corrente");
			throw new CampoInvalidoException("Operacao valida somente para Conta Corrente");
		}
	}

	public void rendimento(Long idConta) 
	{
		ContaEntity conta = buscarConta(idConta);
		if(conta.getTipoConta().equals(TipoConta.POUPANCA))
		{
		conta.setSaldo(conta.getSaldo().add(conta.getSaldo().multiply(conta.getRendimento().divide(BigDecimal.valueOf(100.00)))));
		contaDao.update(conta);
		} else {
			log.error("Operacao valida somente para conta Poupanca");
			throw new CampoInvalidoException("Operacao valida somente para Conta Poupanca");
		}
	}
	

	//METODOS GENERICOS
	private void debitarValor(Long idConta, BigDecimal valor) 
	{
		log.info("Preparando para buscar Conta ID {}", idConta);
		ContaEntity conta = buscarConta(idConta);
		validaValor(valor);
		if(conta.getSaldo().compareTo(valor)>=0) {
			conta.setSaldo(conta.getSaldo().subtract(valor));
			contaDao.update(conta);
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
