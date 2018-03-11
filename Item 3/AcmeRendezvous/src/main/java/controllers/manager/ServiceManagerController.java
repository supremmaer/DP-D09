/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Category;
import domain.Service;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private CategoryService	categoryService;


	// Constructors -----------------------------------------------------------

	public ServiceManagerController() {
		super();
	}

	// Deleting ----------------------------------------------------------------	

	// Listing ----------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId) {
		ModelAndView result;
		Collection<Service> services;

		result = new ModelAndView("service/list");
		services = this.serviceService.findByManager();

		result.addObject("requestURI", "service/manager/list.do");
		result.addObject("services", services);
		return result;
	}

	// Editing ----------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Service service;

		service = this.serviceService.create();
		result = this.createEditModelAndView(service);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		final ModelAndView result;
		Service service;

		service = this.serviceService.findOne(serviceId);
		result = this.createEditModelAndView(service);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Service service, final BindingResult binding) {
		ModelAndView result;

		//		try {
		service = this.serviceService.reconstruct(service, binding);
		//		} catch (final Throwable oops) {
		//			result = this.createEditModelAndView(service, "service.commit.error");
		//		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(service);
		else
			try {
				this.serviceService.save(service);
				result = new ModelAndView("redirect:/service/manager/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(service, "service.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView cancel(final Service service) {
		ModelAndView result;

		try {
			this.serviceService.delete(service.getId());
			result = new ModelAndView("redirect:/service/manager/list.do");
		} catch (final IllegalArgumentException e) {
			result = this.createEditModelAndView(service, "service.delete.error");
		}

		return result;
	}

	// Ancillary Methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Service service) {
		ModelAndView result;

		result = this.createEditModelAndView(service, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Service service, final String messageCode) {
		ModelAndView result;
		Collection<Category> categories;

		categories = this.categoryService.findAll();
		result = new ModelAndView("service/edit");
		result.addObject("service", service);
		result.addObject("message", messageCode);
		result.addObject("categories", categories);

		return result;
	}

}
