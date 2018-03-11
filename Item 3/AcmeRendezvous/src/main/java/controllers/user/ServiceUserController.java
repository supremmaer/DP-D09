/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.Service;

@Controller
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;


	// Constructors -----------------------------------------------------------

	public ServiceUserController() {
		super();
	}

	// Deleting ----------------------------------------------------------------	

	// Listing ----------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId) {
		ModelAndView result;
		Collection<Service> services;

		result = new ModelAndView("service/list");
		services = this.serviceService.findAll();

		result.addObject("requestURI", "service/user/list.do");
		result.addObject("services", services);
		return result;
	}
}
