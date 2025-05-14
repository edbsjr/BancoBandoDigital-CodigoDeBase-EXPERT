package br.com.cdb.BandoDigitalFinal2.service;

import br.com.cdb.BandoDigitalFinal2.dao.ClienteDao;
import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.CpfInvalidoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.IdadeInvalidaException;
import br.com.cdb.BandoDigitalFinal2.exceptions.NomeInvalidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClienteService {

	@Autowired
	private ClienteDao clienteDao;
	
	
	public void salvarCliente(Cliente cliente) {
	validarCliente(cliente);
		clienteDao.save(cliente);
	}

	public void atualizarCliente(Long idCliente, Cliente cliente)
	{
		validarCliente(cliente);
		
		Cliente clienteAntigo = buscarCliente(idCliente);
		
		clienteAntigo.setNome(cliente.getNome());
		clienteAntigo.setCpf(cliente.getCpf());
		clienteAntigo.setDataNasc(cliente.getDataNasc());
		clienteAntigo.setCategoria(cliente.getCategoria());
		clienteDao.update(clienteAntigo);
	}

	public void deletarCliente(Long idCliente) 
	{
		if(buscarCliente(idCliente) != null)
			clienteDao.deleteById(idCliente); // AO INVES DE DELETAR O CLIENTE DA BASE DE DADOS E PERDER INFORMACOES UM SISTEMA DE STATUS SERIA MAIS VIAVEL
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
			
	private void validarNome(String nome) //VALIDA SE O NOME NÃO É VAZIO OU SE TEM NUMEROS, VALIDA TAMBEM O TAMANHO
	{
		if(nome == null ||nome.trim().isEmpty() || nome.length()<2 || nome.length()>100)
			 throw new NomeInvalidoException("O nome do cliente não pode ser vazio.");

		if(!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$"))
			throw new NomeInvalidoException("O nome deve conter apenas letras e espaços.");
	}
	
	//VALIDACAO E PADRONIZACAO DE CPF
	private void validarCPF(String cpf) throws CpfInvalidoException //VALIDACAO DO CPF
	{

		if (cpf.trim().isEmpty())
			throw new CpfInvalidoException("CPF do cliente não pode ser vazio.");

		if (cpf.contains(".") || cpf.contains("-")) {
			cpf = cpf.replace(".", "");
			cpf = cpf.replace("-", "");
		}

		if (cpf.length() != 11)
			throw new CpfInvalidoException("CPF deve conter 11 números.");

		
		if (cpf.chars().distinct().count() == 1) // CPFs com todos os dígitos iguais são inválidos
		{ 
			throw new CpfInvalidoException("CPF com todos os dígitos iguais são inválidos");
		}
		 

		int[] cpfArray = cpfToArray(cpf);

		int j = 10; // VARIAVEIS USADAS PARA O CALCULO DO DIGITO VERIFICADOR DE CPF
		int acumulador = 0;

		for (int i = 0; i < 9; i++) {
			acumulador += cpfArray[i] * j;
			j--;
		}
		int digitoPrimeiro = 11 - (acumulador % 11);
		if (digitoPrimeiro >= 10 && cpfArray[9] != 0 || digitoPrimeiro < 10 && cpfArray[9] != digitoPrimeiro)
			throw new CpfInvalidoException("CPF inválido.");
/* // MOVIDO IF PARA O ACIMA, ASSIM FAZENDO UM COGIDO MAIS LIMPO, CONFERIR A APLICACAO
		else if (digitoPrimeiro < 10 && cpfArray[9] != digitoPrimeiro)
			throw new CpfInvalidoException("CPF inválido.");
*/
		acumulador = 0;
		j = 11;

		for (int i = 0; i < 10; i++) {
			acumulador += cpfArray[i] * j;
			j--;
		}
		int digitoSegundo = 11 - (acumulador % 11);
		if (digitoSegundo >= 10 && cpfArray[10] != 0 || digitoSegundo < 10 && cpfArray[10] != digitoSegundo)
			throw new CpfInvalidoException("CPF inválido.");
	/*	else if (digitoSegundo < 10 && cpfArray[10] != digitoSegundo) // MOVIDO PARA O IF ACIMA
			throw new CpfInvalidoException("CPF inválido.");*/
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
	    	throw new IllegalArgumentException("CEP inválido. Formato esperado: XXXXX-XXX.");
		
	}
	private void validarDataNasc(LocalDate dataNasc) {
		
		LocalDate hoje = LocalDate.now();
		int idade = Period.between(dataNasc, hoje).getYears();
		if (idade<18)
			throw new IdadeInvalidaException("O cliente deve ter pelo menos 18 anos.");
	}

	//METODO QUE BUSCA CLIENTE
	public Cliente buscarCliente(Long idCliente) {
		try
		{
			return clienteDao.findById(idCliente);
		}
		catch (ClienteNaoEncontradoException e) {
			String mensagemEnriquecida = e.getMessage()+"Favor informar outro ID e tentar novamente.";
			throw new ClienteNaoEncontradoException(mensagemEnriquecida);
		}
	}
}
