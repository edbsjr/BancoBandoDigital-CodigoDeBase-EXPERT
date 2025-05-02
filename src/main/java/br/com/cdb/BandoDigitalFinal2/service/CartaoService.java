package br.com.cdb.BandoDigitalFinal2.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.cdb.BandoDigitalFinal2.dao.CartaoDao;
import br.com.cdb.BandoDigitalFinal2.entity.*;
import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.repository.CartaoRepository;
import br.com.cdb.BandoDigitalFinal2.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private CartaoDao cartaoDao;

	@Autowired
	private ContaRepository contaRepository;
	
	//CRIACAO DE CARTOES
	public boolean addCartaoDebito(Long idConta, String senha) {
		CartaoEntity cartao = new CartaoEntity();
		Conta contaAchada = contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
				
		cartao.setIdConta(idConta);
		cartao.setSenha(senha);
		cartao.setTipo(TipoCartao.DEBITO);
		switch (contaAchada.getCliente().getCategoria())
		{
		case COMUM:
			cartao.setLimite(BigDecimal.valueOf(200.00));
			break;
		case SUPER:
			cartao.setLimite(BigDecimal.valueOf(500.00));
			break;
		case PREMIUM:
			cartao.setLimite(BigDecimal.valueOf(1000.00));
		}
		cartao.setLimiteUsado(BigDecimal.ZERO);
		cartao.setSituacao(Situacao.ATIVADO); //SUGERIDO DEIXAR BLOQUEADO, DEIXEI APENAS PARA FACILITAR A EXECUCAO PARA TESTES
		
		return cartaoDao.save(cartao);
	}
	public boolean addCartaoCredito(Long idConta, String senha) {
		CartaoEntity cartao = new CartaoEntity();
		Conta contaAchada = contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
		
		cartao.setIdConta(idConta);
		cartao.setSenha(senha);
		cartao.setTipo(TipoCartao.CREDITO);
		switch (contaAchada.getCliente().getCategoria())
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
		cartao.setLimiteUsado(BigDecimal.ZERO);

		cartao.setSituacao(Situacao.ATIVADO); //SUGERIDO DEIXAR BLOQUEADO, DEIXEI APENAS PARA FACILITAR A EXECUCAO PARA TESTES
		
		return cartaoDao.save(cartao);
	}
	
	//EXIBICAO DE CARTOES
	public List<CartaoEntity> listarTodos() {
		return cartaoDao.findAll();
	}

	public CartaoEntity detalhes(Long idCartao) {
		return cartaoDao.findById(idCartao); //.orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
	}
	

	public void pagamentoCartaoDebito(Long idCartao, String senha, BigDecimal valor) {

		CartaoEntity cartao = detalhes(idCartao);
		Conta conta = contaRepository.findById(cartao.getIdConta()).orElseThrow(() -> new NoSuchElementException("Conta não encontrado"));
		if(cartao.getTipo().equals(TipoCartao.DEBITO)) //VERIFICA SE É CARTAO DE DEBITO MESMO
			throw new RuntimeException ("Operação Permitida apenas para Cartão de Debito");

		if (cartao.getSenha().equals(senha)) {
			if (cartao.getLimite().subtract(cartao.getLimiteUsado()).compareTo(valor)<0) //VERIFICA SE TEM LIMITE DISPONIVEL
					throw new RuntimeException("Transação Ultrapassa o limite diario disponivel");
			if(cartao.getSituacao() != Situacao.ATIVADO)
				throw new RuntimeException("Cartão "+cartao.getSituacao());
			if (conta.getSaldo().compareTo(valor) >= 0) {
				conta.setSaldo(conta.getSaldo().subtract(valor));//DEBITA DIRETO DA CONTA O VALOR
				cartao.setLimiteUsado(cartao.getLimiteUsado().add(valor));//ACRESCENTA O VALOR NO LIMITE EM USO
				cartaoDao.update(cartao);
			}
			else
				throw new RuntimeException("Saldo em conta insuficiente");
		} else
			throw new RuntimeException("Senha incorrenta" + cartao.getSenha());
	}


	public void pagamentoCartaoCredito(Long idCartao, String senha, BigDecimal valor) {
		
		CartaoEntity cartao = detalhes(idCartao);
		if(cartao.getTipo().equals(TipoCartao.CREDITO)) //VERIFICA SE É CARTAO DE CREDITO MESMO
			throw new RuntimeException ("Operação Permitida apenas para Cartão de Credito");

		if (cartao.getSenha().equals(senha)) {
			if(cartao.getLimite().subtract(cartao.getLimiteUsado()).compareTo(valor) >= 0)
				{
				cartao.setValorFatura(cartao.getValorFatura().add(valor));
				cartao.setLimiteUsado(cartao.getLimiteUsado().add(valor));
				cartaoDao.update(cartao);
				}
			else
				throw new RuntimeException("Limite de credito insuficiente");
		} else
			throw new RuntimeException("Senha incorrenta");
	}
	
	//MANUTENCAO
	public void situacaoCartao(Long idCartao, String senha, Situacao situacao) 
	{
		CartaoEntity cartao = detalhes(idCartao);
				
		if(cartao.getSenha().equals(senha))
		{
			if (cartao.getSituacao().equals(Situacao.CANCELADO) || cartao.getSituacao().equals(Situacao.BLOQUEADO))
			{
				throw new RuntimeException("Cartão CANCELADO/BLOQUEADO");
			} else {
				cartao.setSituacao(situacao);
				cartaoDao.update(cartao);}
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	

	public void alterarSenha(Long idCartao, String senha, String senhaNova, String senhaConfirmada) 
	{
		CartaoEntity cartao = detalhes(idCartao);
		
		if(cartao.getSenha().equals(senha))
		{
			if(senhaNova.equals(senhaConfirmada)) {
				cartao.setSenha(senhaConfirmada);
				cartaoDao.update(cartao);
			} else {
				throw new RuntimeException("Confirme a nova senha corretamente");
			}
		} else {throw new RuntimeException("Senha incorrenta");}
	}
	//TODO ATUALIZAR INFORMACOES E TAXAS AUTOMATIZADAS
	
	//FATURAS E LIMITES
	public BigDecimal consultarFatura (Long idCartao, String senha) 
	{
		CartaoEntity cartao = detalhes(idCartao);
		
		if(cartao.getSenha().equals(senha))
		{
			if(cartao.getSituacao()!= Situacao.ATIVADO)
				throw new RuntimeException("Cartão deve estar ativado para realizar transações");
			return cartao.getValorFatura();
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	
	@Transactional
	public void debitarFatura(Long idCartao, String senha) 
	{
		CartaoEntity cartao = detalhes(idCartao);
		Conta conta = contaRepository.findById(cartao.getIdConta()).orElseThrow(()-> new RuntimeException("Conta nao encontrada"));
		if(!cartao.getTipo().equals(TipoCartao.CREDITO))
			throw new RuntimeException("Transação permitida apenas para Cartao de Credito");

		if(cartao.getSenha().equals(senha))
		{
			if(cartao.getSituacao()!= Situacao.ATIVADO)
				throw new RuntimeException("Cartão deve estar ativado para realizar transações");
			if(conta.getSaldo().compareTo(cartao.getValorFatura())>=0)
			{
				conta.debitar(cartao.getValorFatura());
				cartao.setValorFatura(BigDecimal.ZERO);
				cartao.setLimiteUsado(BigDecimal.ZERO);
				cartaoDao.update(cartao);
			}	else 
				throw new RuntimeException ("Valor da fatura supera o valor em conta");
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	
	@Transactional
	public void alterarLimiteDiario(Long idCartao, BigDecimal novoLimite) //TODO DELETAR DEPOIS
	{
		CartaoDebito cartaoDebito = (CartaoDebito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));;
			//if(cartao.getLimiteEmUso().compareTo(novoLimite)>=0) //INICIALMENTE PENSEI CONDICIONAR A MUDANÇA, MAS NAO VI PORQUE UMA VEZ QUE O DINHEIRO JA SAIU DA CONTA
			cartaoDebito.setLimiteDiario(novoLimite);
	}

	public void alterarLimite(Long idCartao, BigDecimal novoLimite)  //TANTO PARA CREDITO QUANTO DEBITO.
	{
		CartaoEntity cartao = detalhes(idCartao);
			if(cartao.getLimiteUsado().compareTo(novoLimite)>=0) {
				cartao.setLimite(novoLimite);
				cartaoDao.update(cartao);
			}
	}
}
