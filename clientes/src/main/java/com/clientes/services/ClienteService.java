
package com.clientes.services;

import java.util.List;

import com.clientes.entity.Cliente;

public interface ClienteService {
	
	List<Cliente> listarClientes(); 
	 Cliente añadirCliente(Cliente c); 
	 Cliente actualizarCliente(Cliente c); 
	 Cliente buscarCliente(int id); 
	 String eliminarCliente(int id);
	
}
