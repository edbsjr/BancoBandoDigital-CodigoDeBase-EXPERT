package br.com.cdb.BandoDigitalFinal2.utils;

import br.com.cdb.BandoDigitalFinal2.exceptions.CpfInvalidoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.IdadeInvalidaException;
import br.com.cdb.BandoDigitalFinal2.exceptions.NomeInvalidoException;

import java.time.LocalDate;
import java.time.Period;

public class ValidadorDeCampos {


    //VALIDADOR DE NOME
    public static void validarNome(String nome) //VALIDA SE O NOME NÃO É VAZIO OU SE TEM NUMEROS, VALIDA TAMBEM O TAMANHO
    {
        if(nome == null ||nome.trim().isEmpty() || nome.length()<2 || nome.length()>100)
            throw new NomeInvalidoException("O nome do cliente não pode ser vazio.");

        if(!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$"))
            throw new NomeInvalidoException("O nome deve conter apenas letras e espaços.");
    }

    //VALIDADOR DE CPF
    public static void validarCPF(String cpf) throws CpfInvalidoException //VALIDACAO DO CPF
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

    //FORMATACAO DE CPF
    public static String padronizarCpf(String cpf)//PADRONIZA O CPF PARA SEMPRE SALVAR NO MESMO FORMATO
    {
        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
    private static int[] cpfToArray(String cpf) {

        int[] cpfArray = new int[11];

        for (int i = 0; i < 11; i++) {
            cpfArray[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));

        }

        return cpfArray;
    }

    //VALIDADOR DE CEP
    public static void validarCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP do endereco deve ser preenchido");
        }
        if(!cep.matches("^\\d{5}-?\\d{3}$"))
            throw new IllegalArgumentException("CEP inválido. Formato esperado: XXXXX-XXX.");
    }

    //VALIDADOR DATA DE NASCIMENTO
    public static void validarDataNasc(LocalDate dataNasc) {
        LocalDate hoje = LocalDate.now();
        int idade = Period.between(dataNasc, hoje).getYears();
        if (idade<18)
            throw new IdadeInvalidaException("O cliente deve ter pelo menos 18 anos.");
    }
}
