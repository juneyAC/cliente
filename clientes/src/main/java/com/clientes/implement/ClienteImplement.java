package com.clientes.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clientes.entity.Cliente;
import com.clientes.services.ClienteService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ClienteImplement implements ClienteService {

	File file = new File("C:\\Users\\Yun\\Documents\\workspace\\clientes\\src\\main\\resources\\clientes.json");
	HashMap<Integer, Cliente> clientes = new HashMap<Integer, Cliente>();
	// ObjectMapper
	ObjectMapper mapper = new ObjectMapper();
	// atributo para el Id
	int id = 0;

	@Override
	public List<Cliente> listarClientes() {

		List<Cliente> list = new ArrayList<Cliente>();
		for (Integer key : this.leerJson().keySet()) {
			list.add(this.leerJson().get(key));
		}
		return list;
	}

	@Override
	public Cliente añadirCliente(Cliente c) {

		// Incremento el id
		id++;

		// Seteo manualmente el valor que se incrementó automaticamente
		c.setClienteId(id);

		clientes.put(id, c);
		this.persistir(file, clientes);
		return new Cliente(id, c.getNombre(), c.getCorreo());
	}

	@Override
	public Cliente actualizarCliente(Cliente c) {

		clientes.clear();
		clientes = this.leerJson();
		// Inserto el nuevo elemento en el hash
		clientes.put(c.getClienteId(), c);
		
		System.out.println("tamaño: "+clientes.size());

		// persisto el estado actual del hash incluyendo el nuevo elemento
		this.persistir(file, clientes);
		return new Cliente(c.getClienteId(), c.getNombre(), c.getCorreo());
	}

	@Override
	public Cliente buscarCliente(int id) {
		// Recupero el elemento buscado del estado actual del json
		return this.leerJson().get(id);
	}

	@Override
	public String eliminarCliente(int id) {
		// Hash temporal con los registros leidos del json
		HashMap<Integer, Cliente> c = this.leerJson();

		// Elimino el elemento primero en el hash
		c.remove(id);

		// Persisto el ultimo estado del hash en el json, ya sin el elemento eliminado
		this.persistir(file, c);
		return "Eliminado";
	}

	public HashMap<Integer, Cliente> leerJson() {
		HashMap<Integer, Cliente> clientesExistentes = new HashMap<Integer, Cliente>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream inStr = new FileInputStream(file);
			TypeReference<HashMap<Integer, Cliente>> typeReference = new TypeReference<HashMap<Integer, Cliente>>() {
			};
			clientesExistentes = mapper.readValue(inStr, typeReference);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clientesExistentes;
	}

	// Metodo para actualizar el estado del json
	public void persistir(File file, HashMap<Integer, Cliente> hash) {
		try {
			mapper.writeValue(file, hash);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
