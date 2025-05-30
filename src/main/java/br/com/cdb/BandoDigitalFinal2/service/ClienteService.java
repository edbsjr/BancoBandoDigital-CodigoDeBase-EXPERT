package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		log.info("Verifica se o cpf {} ja esta na base de dados", cliente.getCpf());

		Cliente clienteExistente = clienteDao.findByCpf(cliente.getCpf());//VERIFICA DUPLICIDADE DE CPF
		if(clienteExistente != null) {
			log.error("O cpf {} ja esta em nossa base de dados no ID {}", cliente.getCpf(),
					clienteExistente.getIdCliente());
			throw new RegistroEmDuplicidadeException("O cpf " + cliente.getCpf() + " ja esta em nossa base " +
					"de dados no ID " + clienteExistente.getIdCliente());
		}
		log.info("Iniciando o save do cliente {} com o cpf {}", cliente.getNome(), cliente.getCpf());
		try {
			if (clienteDao.save(cliente))
				log.info("Cliente {} com o cpf {} salvo com sucesso", cliente.getNome(), cliente.getCpf());
		} catch (RegistroNaoSalvoException ex) {
			throw new RegistroNaoSalvoException("Erro ao inserir o cliente na base de dados");
		}
	}

	public void atualizarCliente(Long idCliente, Cliente cliente)
	{
		log.info("Iniciando a validacao do cliente ID {} com o cpf {}", cliente.getIdCliente(), cliente.getCpf());
		validarCliente(cliente);
		Cliente clienteAntigo = buscarCliente(idCliente);
		log.info("Verificando se o CPF {} ja existe em nossa base em outro ID", cliente.getCpf());
		Cliente clienteExistente = clienteDao.findByCpf(cliente.getCpf());
		if(clienteExistente != null && clienteAntigo.getCpf() != clienteExistente.getCpf()) {
			log.error("O cpf {} ja esta em nossa base de dados no ID {}", cliente.getCpf(),
					clienteExistente.getIdCliente());
			throw new RegistroEmDuplicidadeException("O cpf " + cliente.getCpf() + " ja esta em nossa base " +
					"de dados no ID " + clienteExistente.getIdCliente());
		}
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
		log.info("Buscando a lista de Todos os clientes");
		return clienteDao.findAll();
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
	public Cliente buscarCliente(Long idCliente)
	{
		log.info("Iniciando a busca do cliente com ID: {}", idCliente); // Log INFO no início
		log.debug("Buscando cliente com ID: {}", idCliente); // Adicionando um log DEBUG em caso de sucesso
		Cliente cliente = clienteDao.findById(idCliente);
		if(cliente == null)
			throw new RegistroNaoEncontradoException("Cliente ID "+idCliente+" não encontrado");
		else {
			log.info("Retornando cliente ID {}, nome {}", cliente.getIdCliente(), cliente.getNome());
			return cliente;
		}
	}
}
