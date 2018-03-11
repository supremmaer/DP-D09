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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.RendezvousService;
import services.RequestService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.Service;
import domain.User;
import forms.RequestForm;

@Controller
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


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

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public ModelAndView request(@RequestParam final Integer serviceId) {
		Service service;
		RequestForm requestForm;
		ModelAndView result;

		service = this.serviceService.findOne(serviceId);
		requestForm = this.requestService.createForm(service);

		result = this.createEditModelAndView(requestForm);
		return result;
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST, params = "save")
	public ModelAndView request(@Valid final RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				this.requestService.save(requestForm);
				result = new ModelAndView("redirect:/service/user/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(requestForm, "service.commit.error");
			}

		return result;
	}

	// Ancillary Methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final RequestForm requestForm) {
		ModelAndView result;

		result = this.createEditModelAndView(requestForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestForm requestForm, final String messageCode) {
		ModelAndView result;
		User user;
		Collection<Rendezvous> rendezvouses;

		user = (User) this.actorService.findByPrincipal();
		rendezvouses = this.rendezvousService.findByUser(user.getId());

		result = new ModelAndView("service/request");
		result.addObject("requestForm", requestForm);
		result.addObject("message", messageCode);
		result.addObject("rendezvouses", rendezvouses);
		return result;
	}
}
