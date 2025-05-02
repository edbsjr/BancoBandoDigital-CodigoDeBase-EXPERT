package br.com.cdb.BandoDigitalFinal2.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.cdb.BandoDigitalFinal2.entity.CartaoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.cdb.BandoDigitalFinal2.dto.AdicionarCartaoDto;
import br.com.cdb.BandoDigitalFinal2.dto.AlterarLimiteDto;
import br.com.cdb.BandoDigitalFinal2.dto.AlterarSenhaCartaoDto;
import br.com.cdb.BandoDigitalFinal2.dto.AlterarSituacaoCartaoDto;
import br.com.cdb.BandoDigitalFinal2.dto.CartaoSenhaDto;
import br.com.cdb.BandoDigitalFinal2.dto.CartaoSenhaValorDto;
import br.com.cdb.BandoDigitalFinal2.entity.Cartao;
import br.com.cdb.BandoDigitalFinal2.entity.Conta;
import br.com.cdb.BandoDigitalFinal2.service.CartaoService;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@PostMapping("/addCartaoDebito")
	private ResponseEntity<String> addCartaoDebito(@RequestBody AdicionarCartaoDto adicionarCartaoDto) {
		try {
			cartaoService.addCartaoDebito(adicionarCartaoDto.getContaId(), adicionarCartaoDto.getSenha());
			return new ResponseEntity<>("Cartao Debito adicionado com sucesso", HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addCartaoCredito")
	private ResponseEntity<String> addCartaoCredito(@RequestBody AdicionarCartaoDto adicionarCartaoDto) {
		try {
			cartaoService.addCartaoCredito(adicionarCartaoDto.getContaId(), adicionarCartaoDto.getSenha());
			return new ResponseEntity<>("Cartao Credito adicionado com sucesso", HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/listarTodos")
	private ResponseEntity<List<CartaoEntity>> listarTodos() {
		List<CartaoEntity> listaCartao = cartaoService.listarTodos();
		return new ResponseEntity<List<CartaoEntity>>(listaCartao, HttpStatus.OK);
	}

	@GetMapping("/detalhes/{idCartao}")
	private ResponseEntity<CartaoEntity> detalhes(@PathVariable Long idCartao) {
		try {
			CartaoEntity cartaoAchado = cartaoService.detalhes(idCartao);
			return new ResponseEntity<CartaoEntity>(cartaoAchado, HttpStatus.FOUND);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@PostMapping("/{idCartao}/pagamento/debito")
	private ResponseEntity<String> pagamentoDebito(@PathVariable Long idCartao,
			@RequestBody CartaoSenhaValorDto cartaoSenhaValorDto) {
		try {
			cartaoService.pagamentoCartaoDebito(idCartao, cartaoSenhaValorDto.getSenha(),
					cartaoSenhaValorDto.getValor());
			return new ResponseEntity<>("Pagamento no debito realizado com sucesso.", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO ENTENDER MELHOR E MUDAR ISSO
			// TALVEZ
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Erro ao realizar transferência: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{idCartao}/pagamento/credito")
	private ResponseEntity<String> pagamentoCredito(@PathVariable Long idCartao,
			@RequestBody CartaoSenhaValorDto cartaoSenhaValorDto) {
		try {
			cartaoService.pagamentoCartaoCredito(idCartao, cartaoSenhaValorDto.getSenha(),
					cartaoSenhaValorDto.getValor());
			return new ResponseEntity<>("Pagamento no credito realizado com sucesso.", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO ENTENDER MELHOR E MUDAR ISSO
			// TALVEZ
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Erro ao realizar transferência: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/alterar/situacao")
	private ResponseEntity<String> alterarSituacao(@RequestBody AlterarSituacaoCartaoDto alterarSituacaoCartaoDto) {
		try {
			cartaoService.situacaoCartao(alterarSituacaoCartaoDto.getIdCartao(), alterarSituacaoCartaoDto.getSenha(),
					alterarSituacaoCartaoDto.getSituacao());
			return new ResponseEntity<>("Alteração Realizada com sucesso.", HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/alterar/senha")
	private ResponseEntity<String> alterarSenha(@RequestBody AlterarSenhaCartaoDto alterarSenhaCartaoDto) {
		try {
			cartaoService.alterarSenha(alterarSenhaCartaoDto.getIdCartao(), alterarSenhaCartaoDto.getSenhaAntiga(),
					alterarSenhaCartaoDto.getSenhaNova(), alterarSenhaCartaoDto.getSenhaConfirmada());
			return new ResponseEntity<>("Senha alterada com sucesso", HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/fatura")
	private ResponseEntity<Object> consultarFatura(@RequestBody CartaoSenhaDto cartaoSenhaDto) {
		try {
			BigDecimal valorFatura = cartaoService.consultarFatura(cartaoSenhaDto.getIdCartao(),
					cartaoSenhaDto.getSenha());
			return new ResponseEntity<>(valorFatura, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/fatura/pagamento")
	private ResponseEntity<String> pagarFatura(@RequestBody CartaoSenhaDto cartaoSenhaDto) {
		try {
			cartaoService.debitarFatura(cartaoSenhaDto.getIdCartao(), cartaoSenhaDto.getSenha());
			return new ResponseEntity<>("Fatura Debitada com sucesso", HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/alterar/limite")
	private ResponseEntity<String> alterarLimite(@RequestBody AlterarLimiteDto alterarLimiteDto) {
		try {
			cartaoService.alterarLimite(alterarLimiteDto.getIdCartao(), alterarLimiteDto.getLimite());
			return new ResponseEntity<>("Limite alterado com sucesso", HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/alterar/limite-diario")
	private ResponseEntity<String> alterarLimiteDiario(@RequestBody AlterarLimiteDto alterarLimiteDto) {
		try {
			cartaoService.alterarLimiteDiario(alterarLimiteDto.getIdCartao(), alterarLimiteDto.getLimite());
			return new ResponseEntity<>("Limite alterado com sucesso", HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
