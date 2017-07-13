package com.softbistro.order.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.BookForOrder;
import com.softbistro.order.component.CatalogItem;
import com.softbistro.order.service.OrderService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@RestController
public class Controller {

	@Autowired
	private OrderService service;

	@RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getCatalog() {
		return service.getCatalog();
	}

	@RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = "application/json")
	public Integer createOrder(@RequestBody BookForOrder book) throws JsonProcessingException {
		return service.createOrder(book);
	}

	@RequestMapping(value = "/prices", method = RequestMethod.GET, produces = "application/json")
	public CatalogItem getPrices() {
		return service.getCatalogItem();
	}

	@RequestMapping(value = "/template", method = RequestMethod.GET, produces = "application/json")
	public GenericTemplate getTemplate() {
		return service.createTemplate(getCatalog());
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public List<Book> template() throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		WebResource wr = client.resource("http://80.91.191.79:19200/catalog");
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructCollectionType(List.class, Book.class));
	}

}
