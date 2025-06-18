package br.com.cdb.BandoDigitalFinal2.application.service;

import br.com.cdb.BandoDigitalFinal2.adapter.out.redis.ClienteCacheService;
import br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.application.port.in.ClienteInputPort;
import br.com.cdb.BandoDigitalFinal2.application.port.out.ClienteCacheRepositoryPort;
import br.com.cdb.BandoDigitalFinal2.application.port.out.ClienteRepositoryPort;
import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import br.com.cdb.BandoDigitalFinal2.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.cdb.BandoDigitalFinal2.utils.ValidadorDeCampos.*;

@Slf4j
@Service
@CacheConfig(cacheNames = "clientes")
public class ClienteService implements ClienteInputPort {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);


	private final ClienteRepositoryPort clienteRepositoryPort;
	private final ClienteCacheRepositoryPort clienteCacheRepositoryPort;

	public ClienteService(ClienteRepositoryPort clienteRepositoryPort,
						  ClienteCacheRepositoryPort clienteCacheRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
		this.clienteCacheRepositoryPort = clienteCacheRepositoryPort;
    }

	@Override
    public void salvarCliente(Cliente cliente) {
		log.info("Iniciando a validacao para salvar o cliente {} com o cpf {}", cliente.getNome(), cliente.getCpf());
		validarCliente(cliente);
		log.info("Verifica se o cpf {} ja esta na base de dados", cliente.getCpf());

		Optional<Cliente> clienteExistente = clienteRepositoryPort.findByCpf(cliente.getCpf());//VERIFICA DUPLICIDADE DE CPF
		if(clienteExistente.isPresent()) {
			log.error("O cpf {} ja esta em nossa base de dados no ID {}", cliente.getCpf(),
					clienteExistente.get().getIdCliente());
			throw new RegistroEmDuplicidadeException("O cpf " + cliente.getCpf() + " ja esta em nossa base " +
					"de dados no ID " + clienteExistente.get().getIdCliente());
		}
		log.info("Iniciando o save do cliente {} com o cpf {}", cliente.getNome(), cliente.getCpf());
		try {
			if (clienteRepositoryPort.save(cliente))
				log.info("Cliente {} com o cpf {} salvo com sucesso", cliente.getNome(), cliente.getCpf());
		} catch (RegistroNaoSalvoException ex) {
			throw new RegistroNaoSalvoException("Erro ao inserir o cliente na base de dados");
		}
	}

	@Override
	public void atualizarCliente(Cliente cliente)
	{
		log.info("Iniciando a validacao do cliente ID {} com o cpf {}", cliente.getIdCliente(), cliente.getCpf());
		validarCliente(cliente);
		Cliente clienteAntigo = buscarCliente(cliente.getIdCliente());
		log.info("Verificando se o CPF {} ja existe em nossa base em outro ID", cliente.getCpf());
		Optional<Cliente> clienteExistente = clienteRepositoryPort.findByCpf(cliente.getCpf());
		if(clienteExistente.isPresent() && clienteAntigo.getCpf() != clienteExistente.get().getCpf()) {
			log.error("O cpf {} ja esta em nossa base de dados no ID {}", cliente.getCpf(),
					clienteExistente.get().getIdCliente());
			throw new RegistroEmDuplicidadeException("O cpf " + cliente.getCpf() + " ja esta em nossa base " +
					"de dados no ID " + clienteExistente.get().getIdCliente());
		}
		log.info("Atualizando as informações do cliente ID {}", clienteAntigo.getIdCliente());
		clienteAntigo.setNome(cliente.getNome());
		clienteAntigo.setCpf(cliente.getCpf());
		clienteAntigo.setDataNasc(cliente.getDataNasc());
		clienteAntigo.setCategoria(cliente.getCategoria());
		log.info("Iniciando a atualizacao das informações no banco de dados");
		clienteRepositoryPort.update(clienteAntigo);
		clienteCacheRepositoryPort.evictById(cliente.getIdCliente());
	}

	@Override
	public void deletarCliente(Long idCliente) 
	{
		log.info("Iniciando a busca do cliente ID {}", idCliente);
		if(buscarCliente(idCliente) != null && clienteRepositoryPort.deleteById(idCliente)) {
				// AO INVES DE DELETAR O CLIENTE DA BASE DE DADOS E PERDER INFORMACOES UM SISTEMA DE STATUS SERIA MAIS VIAVEL
				log.info("Cliente deletado da base de dados");
				clienteCacheRepositoryPort.evictById(idCliente);
			}

	}

	@Override
	public List<Cliente> listarTodos(){
		log.info("Buscando a lista de Todos os clientes");
		return clienteRepositoryPort.findAll();
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
	@Override
	public Cliente buscarCliente(Long idCliente)
	{
		log.info("Iniciando a busca do cliente com ID: {}", idCliente); // Log INFO no início
		Optional<Cliente> clienteEncontrado = clienteCacheRepositoryPort.getById(idCliente);
		if(clienteEncontrado.isEmpty()){
				log.warn("Cliente nao encontrado no Redis. Iniciando busca no banco de dados");
				clienteEncontrado = clienteRepositoryPort.findById(idCliente)
						.map(cliente -> { // Usa map para fazer log e put ANTES de retornar o Optional
							log.info("Cliente ID {} encontrado no banco de dados. Armazenando no cache Redis."
									, cliente.getIdCliente());
							clienteCacheRepositoryPort.putById(cliente.getIdCliente(), cliente); // Coloca no cache
							return cliente;})
				;}
		else
			log.info("Cliente ID {} encontrado no cache Redis. Retornando do cache.", idCliente);

		if(clienteEncontrado.isPresent()) {
			log.info("Retornando cliente ID {}, nome {}.", clienteEncontrado.get().getIdCliente(),
					clienteEncontrado.get().getNome());
			return clienteEncontrado.get(); // Retorna o próprio objeto Cliente
		} else {
			log.warn("Cliente ID {} não encontrado.", idCliente); // Use WARN para "não encontrado"
			throw new RegistroNaoEncontradoException("Cliente ID " + idCliente + " não encontrado.");
		}
	}

}
