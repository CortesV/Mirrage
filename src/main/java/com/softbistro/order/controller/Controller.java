package com.softbistro.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.BookForOrder;
import com.softbistro.order.component.CatalogItem;
import com.softbistro.order.service.OrderService;

@RestController
public class Controller {

	@Autowired
	private OrderService service;

	@RequestMapping(value = "/catalog")
	public List<Book> getCatalog() {
		return service.getCatalog();
	}
	
	
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = "application/json")
	public Integer createOrder(@RequestBody BookForOrder book) throws JsonProcessingException {
		return service.createOrder(book);
	}
	@RequestMapping(value = "/prices",method = RequestMethod.GET, produces = "application/json")
	public CatalogItem getPrices() {
		return service.getCatalogItem();
	}
	@RequestMapping(value = "/template", method = RequestMethod.GET, produces = "application/json")
	public GenericTemplate getTemplate() {
		return service.createTemplate(getCatalog());
	}

}
