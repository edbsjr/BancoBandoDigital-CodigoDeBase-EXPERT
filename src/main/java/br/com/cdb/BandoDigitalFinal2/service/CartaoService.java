package br.com.cdb.BandoDigitalFinal2.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.BandoDigitalFinal2.entity.Cartao;
import br.com.cdb.BandoDigitalFinal2.entity.CartaoCredito;
import br.com.cdb.BandoDigitalFinal2.entity.CartaoDebito;
import br.com.cdb.BandoDigitalFinal2.entity.Conta;
import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.repository.CartaoRepository;
import br.com.cdb.BandoDigitalFinal2.repository.ClienteRepository;
import br.com.cdb.BandoDigitalFinal2.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private ContaRepository contaRepository;
	
	//CRIACAO DE CARTOES
	public Cartao addCartaoDebito(Long idConta, String senha) {
		CartaoDebito cartaoDebito = new CartaoDebito();
		Conta contaAchada = contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
				
		cartaoDebito.setConta(contaAchada);
		cartaoDebito.setSenha(senha);
		switch (cartaoDebito.getConta().getCliente().getCategoria()) 
		{
		case COMUM:
			cartaoDebito.setLimiteDiario(BigDecimal.valueOf(200.00));
			break;
		case SUPER:
			cartaoDebito.setLimiteDiario(BigDecimal.valueOf(500.00));
			break;
		case PREMIUM:
			cartaoDebito.setLimiteDiario(BigDecimal.valueOf(1000.00));
		}
		cartaoDebito.setLimiteEmUso(BigDecimal.ZERO);
		cartaoDebito.setSituacao(Situacao.ATIVADO); //SUGERIDO DEIXAR BLOQUEADO, DEIXEI APENAS PARA FACILITAR A EXECUCAO PARA TESTES
		
		return cartaoRepository.save(cartaoDebito);
	}
	public Cartao addCartaoCredito(Long idConta, String senha) {
		CartaoCredito cartaoCredito = new CartaoCredito();
		Conta contaAchada = contaRepository.findById(idConta).orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));
		
		cartaoCredito.setConta(contaAchada);
		cartaoCredito.setSenha(senha);
		switch (cartaoCredito.getConta().getCliente().getCategoria()) 
		{
		case COMUM:
			cartaoCredito.setLimite(BigDecimal.valueOf(1000.00));
			break;
		case SUPER:
			cartaoCredito.setLimite(BigDecimal.valueOf(5000.00));
			break;
		case PREMIUM:
			cartaoCredito.setLimite(BigDecimal.valueOf(10000.00));
		}
		cartaoCredito.setLimiteEmUso(BigDecimal.ZERO);
		cartaoCredito.setSituacao(Situacao.ATIVADO); //SUGERIDO DEIXAR BLOQUEADO, DEIXEI APENAS PARA FACILITAR A EXECUCAO PARA TESTES
		
		return cartaoRepository.save(cartaoCredito);
	}
	
	//EXIBICAO DE CARTOES
	public List<Cartao> listarTodos() {
		return cartaoRepository.findAll();
	}
	public Cartao detalhes(Long idCartao) {
		return cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
	}
	
	@Transactional
	public void pagamentoCartaoDebito(Long idCartao, String senha, BigDecimal valor) {
		
		Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
		if(cartao instanceof CartaoCredito) //VERIFICA SE É CARTAO DE DEBITO MESMO
			throw new RuntimeException ("Operação Permitida apenas para Cartão de Debito");
		
		
		CartaoDebito cartaoDebito = (CartaoDebito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão Debito não encontrado"));
				
		if (cartaoDebito.getSenha().equals(senha)) {
			if (cartaoDebito.getLimiteDiario().subtract(cartaoDebito.getLimiteEmUso()).compareTo(valor)<0) //VERIFICA SE TEM LIMITE DISPONIVEL
					throw new RuntimeException("Transação Ultrapassa o limite diario disponivel");
			if(cartaoDebito.getSituacao() != Situacao.ATIVADO)
				throw new RuntimeException("Cartão "+cartaoDebito.getSituacao());
			if (cartaoDebito.getConta().getSaldo().compareTo(valor) >= 0) {
				cartaoDebito.getConta().setSaldo(cartaoDebito.getConta().getSaldo().subtract(valor));//DEBITA DIRETO DA CONTA O VALOR
				cartaoDebito.getLimiteEmUso().add(valor); //ACRESCENTA O VALOR NO LIMITE EM USO
			}
			else
				throw new RuntimeException("Saldo em conta insuficiente");
		} else
			throw new RuntimeException("Senha incorrenta" + cartaoDebito.getSenha());
	}

	@Transactional
	public void pagamentoCartaoCredito(Long idCartao, String senha, BigDecimal valor) {
		
		Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
		if(cartao instanceof CartaoDebito) //VERIFICA SE É CARTAO DE CREDITO MESMO
			throw new RuntimeException ("Operação Permitida apenas para Cartão de Credito");
		
		CartaoCredito cartaoCredito = (CartaoCredito) cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão Credito não encontrado"));
		
		if (cartaoCredito.getSenha().equals(senha)) {
			if(cartaoCredito.getLimite().subtract(cartaoCredito.getFatura()).compareTo(valor) >= 0)
				{
				cartaoCredito.setFatura(cartaoCredito.getFatura().add(valor));
				cartaoCredito.setLimiteEmUso(cartaoCredito.getLimiteEmUso().add(valor));
				}
			else
				throw new RuntimeException("Limite de credito insuficiente");
		} else
			throw new RuntimeException("Senha incorrenta");
	}
	
	//MANUTENCAO
	@Transactional
	public void situacaoCartao(Long idCartao, String senha, Situacao situacao) 
	{
		Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
				
		if(cartao.getSenha().equals(senha))
		{
			cartao.setSituacao(situacao);
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	
	@Transactional
	public void alterarSenha(Long idCartao, String senha, String senhaNova, String senhaConfirmada) 
	{
		Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
		
		if(cartao.getSenha().equals(senha))
		{
			if(senhaNova.equals(senhaConfirmada))
				cartao.setSenha(senhaConfirmada);
			else
				throw new RuntimeException("Confirme a nova senha corretamente");
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	//TODO ATUALIZAR INFORMACOES E TAXAS AUTOMATIZADAS
	
	//FATURAS E LIMITES
	public BigDecimal consultarFatura (Long idCartao, String senha) 
	{
		CartaoCredito cartaoCredito = (CartaoCredito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));;
		
		if(cartaoCredito.getSenha().equals(senha))
		{
			if(cartaoCredito.getSituacao()!= Situacao.ATIVADO)
				throw new RuntimeException("Cartão deve estar ativado para realizar transações");
			return cartaoCredito.getFatura();
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	
	@Transactional
	public void debitarFatura(Long idCartao, String senha) 
	{
		CartaoCredito cartaoCredito = (CartaoCredito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
		
		if(cartaoCredito.getSenha().equals(senha))
		{
			if(cartaoCredito.getSituacao()!= Situacao.ATIVADO)
				throw new RuntimeException("Cartão deve estar ativado para realizar transações");
			if(cartaoCredito.getConta().getSaldo().compareTo(cartaoCredito.getFatura())>=0)
			{
				cartaoCredito.getConta().debitar(cartaoCredito.getFatura());
				cartaoCredito.setFatura(BigDecimal.ZERO);
				cartaoCredito.setLimiteEmUso(BigDecimal.ZERO);
			}	else 
				throw new RuntimeException ("Valor da fatura supera o valor em conta");
			
		}	else
			throw new RuntimeException("Senha incorrenta");
	}
	
	@Transactional
	public void alterarLimiteDiario(Long idCartao, BigDecimal novoLimite) 
	{
		CartaoDebito cartaoDebito = (CartaoDebito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));;
			//if(cartao.getLimiteEmUso().compareTo(novoLimite)>=0) //INICIALMENTE PENSEI CONDICIONAR A MUDANÇA, MAS NAO VI PORQUE UMA VEZ QUE O DINHEIRO JA SAIU DA CONTA
			cartaoDebito.setLimiteDiario(novoLimite);
	}
	
	@Transactional
	public void alterarLimite(Long idCartao, BigDecimal novoLimite) 
	{
		CartaoCredito cartaoCredito = (CartaoCredito)cartaoRepository.findById(idCartao).orElseThrow(() -> new NoSuchElementException("Cartão não encontrado"));
			if(cartaoCredito.getLimiteEmUso().compareTo(novoLimite)>=0) 
			cartaoCredito.setLimite(novoLimite);
			
	}
}
