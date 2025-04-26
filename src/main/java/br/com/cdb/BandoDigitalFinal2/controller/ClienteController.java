package br.com.cdb.BandoDigitalFinal2.controller;

import java.util.List;
import java.util.NoSuchElementException;

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

import br.com.cdb.BandoDigitalFinal2.entity.Cliente;

import br.com.cdb.BandoDigitalFinal2.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	//METODOS
	@PostMapping("/add")
	public ResponseEntity<String> addCliente(@RequestBody Cliente cliente) //RECEBE JSON(CLIENTE) PARA VALIDACAO
	{
		try {
		clienteService.salvarCliente(cliente);
		return new ResponseEntity<>("Cliente salvo com sucesso.", HttpStatus.CREATED);
		}
		catch(IllegalArgumentException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
				
	@GetMapping("/listarTodos")
	public ResponseEntity<List<Cliente>> listarTodos() //RETORNA TODOS OS CLIENTES
	{
		List<Cliente> listaDeClientes = clienteService.listarTodos();
		
		return new ResponseEntity<List<Cliente>>(listaDeClientes, HttpStatus.OK);
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<Cliente> buscarCliente(@PathVariable Long idCliente ) // RETURNA JSON(CLIENTE) UNICO POR ID
	{
	   Cliente clienteBuscado = clienteService.obterCliente(idCliente);
	   if(clienteBuscado!= null)
	            return new ResponseEntity<Cliente>(clienteBuscado, HttpStatus.OK);
	   else
		   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
	}
	
	@PutMapping("/{idCliente}/atualizar")
	public ResponseEntity<String> atualizarCliente(@PathVariable Long idCliente, @RequestBody Cliente cliente) // RECEBE JSON(CLIENTE) PARA ATUALIZAR TODOS OS CAMPOS
	{
		try {
			clienteService.atualizarCliente(idCliente, cliente);
			return new ResponseEntity<String>("Cliente atualizado com sucesso.", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{idCliente}/delete")
	public ResponseEntity<String> deletarCliente(@PathVariable Long idCliente) 
	{
		try {
		clienteService.deletarCliente(idCliente);
		return new ResponseEntity<String>("Cliente deletado.", HttpStatus.OK);
		} catch (NoSuchElementException e) 
		{
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	

}
