package br.com.cdb.BandoDigitalFinal2.adapter.in.web;

import java.util.List;

import br.com.cdb.BandoDigitalFinal2.adapter.in.web.mapper.ClienteMapper;
import br.com.cdb.BandoDigitalFinal2.application.port.in.ClienteInputPort;
import br.com.cdb.BandoDigitalFinal2.dto.in.ClienteRequestDto;
import br.com.cdb.BandoDigitalFinal2.dto.out.ClienteResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;

import br.com.cdb.BandoDigitalFinal2.application.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteInputPort clienteInputPort;
	@Autowired
	private ClienteMapper clienteMapper;

    //METODOS
	@PostMapping("/add")
	public ResponseEntity<String> addCliente(@RequestBody ClienteRequestDto clienteRequestDto) //RECEBE JSON(CLIENTE) PARA VALIDACAO
	{
		Cliente cliente = clienteMapper.toModel(clienteRequestDto);
		clienteInputPort.salvarCliente(cliente);
		return new ResponseEntity<>("Cliente salvo com sucesso.", HttpStatus.CREATED);
	}
				
	@GetMapping("/listarTodos")
	public ResponseEntity<List<ClienteResponseDto>> listarTodos() //RETORNA TODOS OS CLIENTES
	{
		List<Cliente> listaDeCliente = clienteInputPort.listarTodos();
		List<ClienteResponseDto> listaDeClienteResponseDto = clienteMapper.toResponseDtoList(listaDeCliente);
		return new ResponseEntity<>(listaDeClienteResponseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<ClienteResponseDto> buscarCliente(@PathVariable Long idCliente ) // RETURNA JSON(CLIENTE) UNICO POR ID
	{
	   Cliente clienteModel = clienteInputPort.buscarCliente(idCliente);
		ClienteResponseDto clienteResponseDto = clienteMapper.toResponseDto(clienteModel);
	   return new ResponseEntity<>(clienteResponseDto, HttpStatus.OK);
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<String> atualizarCliente(@RequestBody ClienteRequestDto clienteRequestDto) // RECEBE JSON(CLIENTE) PARA ATUALIZAR TODOS OS CAMPOS
	{
		Cliente cliente = clienteMapper.toModel(clienteRequestDto);
		clienteInputPort.atualizarCliente(cliente);
		return new ResponseEntity<>("Cliente atualizado com sucesso.", HttpStatus.OK);
	}
	
	@DeleteMapping("/{idCliente}/delete")
	public ResponseEntity<String> deletarCliente(@PathVariable Long idCliente) 
	{
		clienteInputPort.deletarCliente(idCliente);
		return new ResponseEntity<>("Cliente deletado.", HttpStatus.OK);

	}
}
