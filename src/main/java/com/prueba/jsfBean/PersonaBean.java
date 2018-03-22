package com.prueba.jsfBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.data.Persona;

@ManagedBean(name="per")
@SessionScoped
public class PersonaBean {
	private Persona persona;
	private List<Persona> personas = new ArrayList<>();

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public List<Persona> getPersonas() throws JsonParseException, JsonMappingException, IOException{
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<List<Persona>> mapType = new TypeReference<List<Persona>>() {};
		String consulta;
		consulta = restTemplate.getForObject("http://localhost:8090/verpersonas", String.class);
		personas = objectMapper.readValue(consulta, mapType);
		return personas;
	}
	public String nuevo() {
		persona= new Persona();
		personas= new ArrayList<>();
		return "nuevoper.faces";	
	}
	public String editar(int id){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", id);
		persona  = restTemplate.getForObject("http://localhost:8090/persona/{id}",  Persona.class, params);	    
		return "verpersona.faces";
	}

	public String crear(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject( "http://localhost:8090/add", persona, Persona.class);	
		return "lisper.faces";
	}

	public String eliminar(Integer id){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", id);
		restTemplate.delete( "http://localhost:8090/delete/{id}", id);
		return "lisper.faces";
	}
	
}