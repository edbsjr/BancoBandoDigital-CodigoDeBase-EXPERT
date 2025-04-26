package br.com.cdb.BandoDigitalFinal2.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.Endereco;
import br.com.cdb.BandoDigitalFinal2.enums.Estado;
import br.com.cdb.BandoDigitalFinal2.repository.ClienteRepository;
import jakarta.transaction.Transactional;

//TODO TRATAMENTO DE ERROS
//TODO AO DELETAR O CLIENTE FAZER EFEITO CASCATA PARA DELETAR CONTA E CARTOES

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	
	public Cliente salvarCliente(Cliente cliente) {
		
	validarCliente(cliente);
	
	/*//CASO O ENUM PASSADO SEJA A DESCRICAO AO INVES DA SIGLA //TODO Tratar erros com enum digitado errado.
	if(verificarEstado(cliente.getEndereco().getEstado())) 
	{
		String estadoString = cliente.getEndereco().getEstado().toString();
		Estado novoEstado = Estado.getDescricao(estadoString);
		Endereco novoEndereco = cliente.getEndereco();
		novoEndereco.setEstado(novoEstado);
		cliente.setEndereco(novoEndereco);
		}*/
	return clienteRepository.save(cliente);
	
	}
	
	/*private boolean verificarEstado(Estado estado) { //PERTENCE AO TRATAMENTO DE ERROS DO ENUM DIGITADO ERRADO, AINDA INCOMPLETO
		
		if(estado.toString().length()>2 && estado != null) 
		{
			return false;
		}
		else
			return true;
	}*/

	@Transactional
	public void atualizarCliente(Long idCliente, Cliente cliente) 
	{
		validarCliente(cliente);
		
		Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
		Cliente clienteAntigo = clienteOptional.get();
		
		clienteAntigo.setNome(cliente.getNome());
		clienteAntigo.setCpf(cliente.getCpf());
		clienteAntigo.setEndereco(cliente.getEndereco());
		clienteAntigo.setDataNasc(cliente.getDataNasc());
		clienteAntigo.setCategoria(cliente.getCategoria());
	}

	public void deletarCliente(Long idCliente) 
	{
		if(clienteRepository.findById(idCliente) != null)
			clienteRepository.deleteById(idCliente); // AO INVES DE DELETAR O CLIENTE DA BASE DE DADOS E PERDER INFORMACOES UM SISTEMA DE STATUS SERIA MAIS VIAVEL
		else 
			throw new NoSuchElementException("Cliente não encontrado");
	}
	
	public List<Cliente> listarTodos(){
	
	return clienteRepository.findAll();
	}
	
	public Cliente obterCliente(Long idCliente) {
		
		Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
		return cliente; //TODO BUSCAR DETALHES TALVEZ COM O ID VERIFICAR USO DO OPTIONAL
	}
		
	//METODOS DE VALIDAÇÕES
	private void validarCliente(Cliente cliente) {
		validarNome(cliente.getNome());
		validarCPF(cliente.getCpf());
		if(cliente.getCpf().length() == 11)
			cliente.setCpf(padronizarCpf(cliente.getCpf()));
		
		validarCep(cliente.getEndereco().getCep());
		validarDataNasc(cliente.getDataNasc());
	}
			
	private void validarNome(String nome) //VALIDA SE O NOME NÃO É VAZIO OU SE TEM NUMEROS, VALIDA TAMBEM O TAMANHO
	{
		if(nome == null ||nome.trim().isEmpty() || nome.length()<2 || nome.length()>100)
			 throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");

		if(!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$"))
			throw new IllegalArgumentException("O nome deve conter apenas letras e espaços.");
	}
	
	//VALIDACAO E PADRONIZACAO DE CPF
	private void validarCPF(String cpf) //VALIDACAO DO CPF
	{

		if (cpf.trim().isEmpty())
			throw new IllegalArgumentException("O cpf do cliente não pode ser vazio.");

		if (cpf.contains(".") || cpf.contains("-")) {
			cpf = cpf.replace(".", "");
			cpf = cpf.replace("-", "");
		}

		if (cpf.length() != 11)
			throw new IllegalArgumentException("O cpf deve conter 11 números.");

		
		if (cpf.chars().distinct().count() == 1) // CPFs com todos os dígitos iguais são inválidos
		{ 
			throw new IllegalArgumentException("CPF inválido. CPFs com todos os dígitos iguais são inválidos"); 
		}
		 

		int[] cpfArray = cpfToArray(cpf);

		int j = 10; // VARIAVEIS USADAS PARA O CALCULO DO DIGITO VERIFICADOR DE CPF
		int acumulador = 0;

		for (int i = 0; i < 9; i++) {
			acumulador += cpfArray[i] * j;
			j--;
		}
		int digitoPrimeiro = 11 - (acumulador % 11);
		if (digitoPrimeiro >= 10 && cpfArray[9] != 0)
			throw new IllegalArgumentException("CPF inválido.");

		else if (digitoPrimeiro < 10 && cpfArray[9] != digitoPrimeiro)
			throw new IllegalArgumentException("CPF inválido.");

		acumulador = 0;
		j = 11;

		for (int i = 0; i < 10; i++) {
			acumulador += cpfArray[i] * j;
			j--;
		}
		int digitoSegundo = 11 - (acumulador % 11);
		if (digitoSegundo >= 10 && cpfArray[10] != 0)
			throw new IllegalArgumentException("CPF inválido.");
		else if (digitoSegundo < 10 && cpfArray[10] != digitoSegundo)
			throw new IllegalArgumentException("CPF inválido.");
	}
	private int[] cpfToArray(String cpf) {
		
		int[] cpfArray = new int[11];

		for (int i = 0; i < 11; i++) {
			cpfArray[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));

		}

		return cpfArray;
	}
	private String padronizarCpf(String cpf)//PADRONIZA O CPF PARA SEMPRE SALVAR NO MESMO FORMATO 
	{
		
        return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9, 11);
	}
	
	private void validarCep(String cep) {
		
		if (cep == null || cep.trim().isEmpty()) {
	        throw new IllegalArgumentException("CEP do endereco deve ser preenchido");
	    }
	    if(!cep.matches("^\\d{5}-?\\d{3}$"))
	    	throw new IllegalArgumentException("CEP inválido. Formato esperado: 00000-000.");
		
	}
	private void validarDataNasc(LocalDate dataNasc) {
		
		LocalDate hoje = LocalDate.now();
		int idade = Period.between(dataNasc, hoje).getYears();
		if (idade<18)
			throw new IllegalArgumentException("O cliente deve ter pelo menos 18 anos.");
	}
}
