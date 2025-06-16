package br.com.cdb.BandoDigitalFinal2.adapter.in.web;

import java.math.BigDecimal;
import java.util.List;

import br.com.cdb.BandoDigitalFinal2.domain.model.CartaoEntity;
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
import br.com.cdb.BandoDigitalFinal2.application.service.CartaoService;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@PostMapping("/addCartao")
	private ResponseEntity<String> addCartaoDebito(@RequestBody AdicionarCartaoDto adicionarCartaoDto) {
		cartaoService.addCartaoDebito(adicionarCartaoDto.getContaId(), adicionarCartaoDto.getSenha(),
				adicionarCartaoDto.getTipo());
		return new ResponseEntity<>("Cartao adicionado com sucesso", HttpStatus.CREATED);
	}

	@GetMapping("/listarTodos")
	private ResponseEntity<List<CartaoEntity>> listarTodos() {
		List<CartaoEntity> listaCartao = cartaoService.listarTodos();
		return new ResponseEntity<List<CartaoEntity>>(listaCartao, HttpStatus.OK);
	}

	@GetMapping("/detalhes/{idCartao}")
	private ResponseEntity<CartaoEntity> detalhes(@PathVariable Long idCartao) {
		CartaoEntity cartaoAchado = cartaoService.buscarCartao(idCartao);
		return new ResponseEntity<CartaoEntity>(cartaoAchado, HttpStatus.FOUND);
	}

	@PostMapping("/{idCartao}/pagamento/debito")
	private ResponseEntity<String> pagamentoDebito(@PathVariable Long idCartao,
			@RequestBody CartaoSenhaValorDto cartaoSenhaValorDto) {
		cartaoService.pagamentoCartaoDebito(idCartao, cartaoSenhaValorDto.getSenha(), cartaoSenhaValorDto.getValor());
		return new ResponseEntity<>("Pagamento no debito realizado com sucesso.", HttpStatus.OK);
	}

	@PostMapping("/{idCartao}/pagamento/credito")
	private ResponseEntity<String> pagamentoCredito(@PathVariable Long idCartao,
													@RequestBody CartaoSenhaValorDto cartaoSenhaValorDto) {
		cartaoService.pagamentoCartaoCredito(idCartao, cartaoSenhaValorDto.getSenha(), cartaoSenhaValorDto.getValor());
		return new ResponseEntity<>("Pagamento no credito realizado com sucesso.", HttpStatus.OK);
	}

	@PutMapping("/alterar/situacao")
	private ResponseEntity<String> alterarSituacao(@RequestBody AlterarSituacaoCartaoDto alterarSituacaoCartaoDto) {
		cartaoService.situacaoCartao(alterarSituacaoCartaoDto.getIdCartao(), alterarSituacaoCartaoDto.getSenha(),
					alterarSituacaoCartaoDto.getSituacao());
		return new ResponseEntity<>("Alteração Realizada com sucesso.", HttpStatus.ACCEPTED);
	}

	@PutMapping("/alterar/senha")
	private ResponseEntity<String> alterarSenha(@RequestBody AlterarSenhaCartaoDto alterarSenhaCartaoDto) {
		cartaoService.alterarSenha(alterarSenhaCartaoDto.getIdCartao(), alterarSenhaCartaoDto.getSenhaAntiga(),
					alterarSenhaCartaoDto.getSenhaNova(), alterarSenhaCartaoDto.getSenhaConfirmada());
		return new ResponseEntity<>("Senha alterada com sucesso", HttpStatus.ACCEPTED);
	}

	@GetMapping("/fatura")
	private ResponseEntity<Object> consultarFatura(@RequestBody CartaoSenhaDto cartaoSenhaDto) {
		BigDecimal valorFatura = cartaoService.consultarFatura(cartaoSenhaDto.getIdCartao(),
				cartaoSenhaDto.getSenha());
		return new ResponseEntity<>(valorFatura, HttpStatus.OK);
	}

	@PostMapping("/fatura/pagamento")
	private ResponseEntity<String> pagarFatura(@RequestBody CartaoSenhaDto cartaoSenhaDto) {
		cartaoService.debitarFatura(cartaoSenhaDto.getIdCartao(), cartaoSenhaDto.getSenha());
		return new ResponseEntity<>("Fatura Debitada com sucesso", HttpStatus.OK);
	}

	@PutMapping("/alterar/limite")
	private ResponseEntity<String> alterarLimite(@RequestBody AlterarLimiteDto alterarLimiteDto) {
		cartaoService.alterarLimite(alterarLimiteDto.getIdCartao(), alterarLimiteDto.getLimite());
		return new ResponseEntity<>("Limite alterado com sucesso", HttpStatus.OK);
	}

	@PutMapping("/alterar/limite-diario")
	private ResponseEntity<String> alterarLimiteDiario(@RequestBody AlterarLimiteDto alterarLimiteDto) {
		cartaoService.alterarLimiteDiario(alterarLimiteDto.getIdCartao(), alterarLimiteDto.getLimite());
		return new ResponseEntity<>("Limite alterado com sucesso", HttpStatus.OK);
	}
}
