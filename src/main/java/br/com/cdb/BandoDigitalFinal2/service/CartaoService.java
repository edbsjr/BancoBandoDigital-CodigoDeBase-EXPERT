package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.CartaoDao;
import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.dao.ContaDao;
import br.com.cdb.BandoDigitalFinal2.entity.CartaoEntity;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;
import br.com.cdb.BandoDigitalFinal2.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartaoService {

	private static final Logger log = LoggerFactory.getLogger(ClienteDao.class);
	@Autowired
	private CartaoDao cartaoDao;
	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private ContaDao contaDao;
	
	//CRIACAO DE CARTOES
	public boolean addCartaoDebito(Long idConta, String senha, TipoCartao tipo) {
		CartaoEntity cartao = new CartaoEntity();
		log.info("Verificando se a Conta ID {} existe na base de dados", idConta);
		ContaEntity conta = contaDao.findById(idConta);
		if(conta == null)
			throw new RegistroNaoEncontradoException("Conta "+idConta+" nao encontrada");
		Optional<Cliente> cliente = clienteDao.findById(conta.getIdCliente());
		if(cliente.isEmpty())
			throw new RegistroNaoEncontradoException("Cliente "+conta.getIdCliente()+" nao encontrada");
		cartao.setIdConta(idConta);
		cartao.setSenha(senha);
		if(tipo.equals(TipoCartao.DEBITO)) {
			cartao.setTipo(TipoCartao.DEBITO);
			switch (cliente.get().getCategoria()) {
				case COMUM:
					cartao.setLimite(BigDecimal.valueOf(200.00));
					break;
				case SUPER:
					cartao.setLimite(BigDecimal.valueOf(500.00));
					break;
				case PREMIUM:
					cartao.setLimite(BigDecimal.valueOf(1000.00));
			}
		} else if (tipo.equals(TipoCartao.CREDITO))
		{
			cartao.setTipo(TipoCartao.CREDITO);
			cartao.setValorFatura(BigDecimal.ZERO);
			switch (cliente.get().getCategoria())
			{
				case COMUM:
					cartao.setLimite(BigDecimal.valueOf(1000.00));
					break;
				case SUPER:
					cartao.setLimite(BigDecimal.valueOf(5000.00));
					break;
				case PREMIUM:
					cartao.setLimite(BigDecimal.valueOf(10000.00));
			}
		}
		cartao.setLimiteUsado(BigDecimal.ZERO);
		cartao.setSituacao(Situacao.ATIVADO); //SUGERIDO DEIXAR BLOQUEADO, DEIXEI APENAS PARA FACILITAR A EXECUCAO PARA TESTES
		log.info("Passando os parametros para a camada dao");
		return cartaoDao.save(cartao);
	}

	public List<CartaoEntity> listarTodos()
	{
		log.info("Buscando a lista de todos os cartoes");
		return cartaoDao.findAll();
	}
	public CartaoEntity buscarCartao(Long idCartao) {
		log.info("Iniciando busca do cartao ID {}", idCartao);
		CartaoEntity cartao = cartaoDao.findById(idCartao);
		if(cartao == null){
			log.error("Cartao ID {} nao encontrado", idCartao);
			throw new RegistroNaoEncontradoException("Cartao ID "+idCartao+" nao encontrado");
		}
		else{
			log.info("Retornando cartao ID {}", idCartao);
			return cartao;
		}
	}

	public void pagamentoCartaoDebito(Long idCartao, String senha, BigDecimal valor) {

		CartaoEntity cartao = buscarCartao(idCartao);
		ContaEntity conta = contaDao.findById(cartao.getIdConta());
		if(!cartao.getTipo().equals(TipoCartao.DEBITO)) //VERIFICA SE É CARTAO DE DEBITO MESMO
			throw new TipoCartaoInvalidoException("Operação Permitida apenas para Cartão de Debito");

		if (cartao.getSenha().equals(senha)) {
			if (cartao.getLimite().subtract(cartao.getLimiteUsado()).compareTo(valor)<0) //VERIFICA SE TEM LIMITE DISPONIVEL
					throw new LimiteInvalidoException("Transação Ultrapassa o limite disponivel");
			if(cartao.getSituacao() != Situacao.ATIVADO)
				throw new CartaoSituacaoInvalidaException("Cartão "+cartao.getSituacao().name());
			if (conta.getSaldo().compareTo(valor) >= 0) {
				conta.setSaldo(conta.getSaldo().subtract(valor));//DEBITA DIRETO DA CONTA O VALOR
				cartao.setLimiteUsado(cartao.getLimiteUsado().add(valor));//ACRESCENTA O VALOR NO LIMITE EM USO
				cartaoDao.update(cartao);
				contaDao.update(conta);
			}
			else
				throw new SaldoInsuficienteException("Saldo em conta insuficiente");
		} else
			throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
	}


	public void pagamentoCartaoCredito(Long idCartao, String senha, BigDecimal valor) {
		
		CartaoEntity cartao = buscarCartao(idCartao);
		if(!cartao.getTipo().equals(TipoCartao.CREDITO)) //VERIFICA SE É CARTAO DE CREDITO MESMO
			throw new TipoCartaoInvalidoException ("Operação Permitida apenas para Cartão de Credito");
		if(cartao.getSituacao() != Situacao.ATIVADO)
			throw new CartaoSituacaoInvalidaException("Cartão "+cartao.getSituacao().name());
		if (cartao.getSenha().equals(senha)) {
			if(cartao.getLimite().subtract(cartao.getLimiteUsado()).compareTo(valor) >= 0)
				{
				cartao.setValorFatura(cartao.getValorFatura().add(valor));
				cartao.setLimiteUsado(cartao.getLimiteUsado().add(valor));
				cartaoDao.update(cartao);
				}
			else
				throw new LimiteInvalidoException("Limite de credito insuficiente");
		} else
			throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
	}
	
	//MANUTENCAO
	public void situacaoCartao(Long idCartao, String senha, Situacao situacao) 
	{
		CartaoEntity cartao = buscarCartao(idCartao);
				
		if(cartao.getSenha().equals(senha))
		{
			if (cartao.getSituacao().equals(Situacao.CANCELADO) || cartao.getSituacao().equals(Situacao.BLOQUEADO))
			{
				throw new CartaoSituacaoInvalidaException("Cartão "+ cartao.getSituacao().name());
			} else {
				cartao.setSituacao(situacao);
				cartaoDao.update(cartao);}
			
		}	else
			throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
	}
	

	public void alterarSenha(Long idCartao, String senha, String senhaNova, String senhaConfirmada) 
	{
		CartaoEntity cartao = buscarCartao(idCartao);
		
		if(cartao.getSenha().equals(senha))
		{
			if(senhaNova.equals(senhaConfirmada)) {
				cartao.setSenha(senhaConfirmada);
				cartaoDao.update(cartao);
			} else {
				throw new SenhaInvalidaException("Erro. Verifique a nova senha e tente novamente");
			}
		} else {throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
		}
	}
	//TODO ATUALIZAR INFORMACOES E TAXAS AUTOMATIZADAS
	
	//FATURAS E LIMITES
	public BigDecimal consultarFatura (Long idCartao, String senha)
	{
		CartaoEntity cartao = buscarCartao(idCartao);
		
		if(cartao.getSenha().equals(senha))
		{
			if(cartao.getSituacao()!= Situacao.ATIVADO)
				throw new CartaoSituacaoInvalidaException("Cartão deve estar ativado para realizar transações");
			return cartao.getValorFatura();
			
		}	else
			throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
	}
	

	public void debitarFatura(Long idCartao, String senha) 
	{
		CartaoEntity cartao = buscarCartao(idCartao);
		ContaEntity conta = contaDao.findById(cartao.getIdConta());
		if(!cartao.getTipo().equals(TipoCartao.CREDITO))
			throw new TipoCartaoInvalidoException("Transação permitida apenas para Cartao de Credito");

		if(cartao.getSenha().equals(senha))
		{
			if(cartao.getSituacao()!= Situacao.ATIVADO)
				throw new CartaoSituacaoInvalidaException("Cartão deve estar ativado para realizar transações");
			if(conta.getSaldo().compareTo(cartao.getValorFatura())>=0)
			{
				conta.debitar(cartao.getValorFatura());
				cartao.setValorFatura(BigDecimal.ZERO);
				cartao.setLimiteUsado(BigDecimal.ZERO);
				cartaoDao.update(cartao);
				contaDao.update(conta);
			}	else 
				throw new SaldoInsuficienteException("Valor da fatura supera o saldo em conta");
			
		}	else
			throw new SenhaInvalidaException("Senha incorrenta. Verifique e tente novamente");
	}

	public void alterarLimiteDiario(Long idCartao, BigDecimal novoLimite) //TODO DELETAR DEPOIS
	{
		CartaoEntity cartao = buscarCartao(idCartao);
			if(cartao.getLimiteUsado().compareTo(novoLimite)<=0) //INICIALMENTE PENSEI CONDICIONAR A MUDANÇA, MAS NAO VI PORQUE UMA VEZ QUE O DINHEIRO JA SAIU DA CONTA
			{
				cartao.setLimite(novoLimite);
				cartaoDao.update(cartao);
			} else
				throw new LimiteInvalidoException("O limite usado atualmente, não pode ser maior que o novo limite");
	}

	public void alterarLimite(Long idCartao, BigDecimal novoLimite)  //TANTO PARA CREDITO QUANTO DEBITO.
	{
		CartaoEntity cartao = buscarCartao(idCartao);
			if(cartao.getLimiteUsado().compareTo(novoLimite)<=0) {
				cartao.setLimite(novoLimite);
				cartaoDao.update(cartao);
			}
	}


}
