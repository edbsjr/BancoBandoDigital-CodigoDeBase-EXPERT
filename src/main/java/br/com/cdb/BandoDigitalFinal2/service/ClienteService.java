package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.CpfInvalidoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.IdadeInvalidaException;
import br.com.cdb.BandoDigitalFinal2.exceptions.NomeInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static br.com.cdb.BandoDigitalFinal2.utils.ValidadorDeCampos.*;

@Slf4j
@Service
public class ClienteService {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
	@Autowired
	private ClienteDao clienteDao;
	
	
	public void salvarCliente(Cliente cliente) {
		log.info("Iniciando a validacao para salvar o cliente {} com o cpf {}", cliente.getNome(), cliente.getCpf());
		validarCliente(cliente);
		log.info("Iniciando o save do cliente {} com o cpf {}", cliente.getNome(), cliente.getCpf());
		clienteDao.save(cliente);
	}

	public void atualizarCliente(Long idCliente, Cliente cliente)
	{
		log.info("Iniciando a validacao do cliente ID {} com o cpf {}", cliente.getIdCliente(), cliente.getCpf());
		validarCliente(cliente);
		Cliente clienteAntigo = buscarCliente(idCliente);

		log.info("Atualizando as informações do cliente ID {}", clienteAntigo.getIdCliente());
		clienteAntigo.setNome(cliente.getNome());
		clienteAntigo.setCpf(cliente.getCpf());
		clienteAntigo.setDataNasc(cliente.getDataNasc());
		clienteAntigo.setCategoria(cliente.getCategoria());
		log.info("Iniciando a atualizacao das informações no banco de dados");
		clienteDao.update(clienteAntigo);
	}

	public void deletarCliente(Long idCliente) 
	{
		log.info("Iniciando a busca do cliente ID {}", idCliente);
		if(buscarCliente(idCliente) != null)
			clienteDao.deleteById(idCliente);
		// AO INVES DE DELETAR O CLIENTE DA BASE DE DADOS E PERDER INFORMACOES UM SISTEMA DE STATUS SERIA MAIS VIAVEL
		log.info("Cliente deletado da base de dados");
	}
	
	public List<Cliente> listarTodos(){
		return clienteDao.findAll();
	}
	
	public Cliente obterCliente(Long idCliente) {
		return buscarCliente(idCliente);
	}
		
	//METODOS DE VALIDAÇÕES
	private void validarCliente(Cliente cliente) {
		validarNome(cliente.getNome());
		validarCPF(cliente.getCpf());
		if(cliente.getCpf().length() == 11)
			cliente.setCpf(padronizarCpf(cliente.getCpf()));
		//validarCep(cliente.getEndereco().getCep());
		validarDataNasc(cliente.getDataNasc());
	}

	//METODO QUE BUSCA CLIENTE
	public Cliente buscarCliente(Long idCliente) {
		log.info("Iniciando a busca do cliente com ID: {}", idCliente); // Log INFO no início
		try
		{
			log.debug("Cliente encontrado com ID: {}", idCliente); // Adicionando um log DEBUG em caso de sucesso
			return clienteDao.findById(idCliente);
		}
		catch (ClienteNaoEncontradoException e) {
			log.error("Cliente não encontrado para o ID: {}. Erro original: {}", idCliente, e.getMessage());
			// Log ERROR no catch
			String mensagemEnriquecida = e.getMessage()+"Favor informar outro ID e tentar novamente.";
			throw new ClienteNaoEncontradoException(mensagemEnriquecida);
		}
	}
}
