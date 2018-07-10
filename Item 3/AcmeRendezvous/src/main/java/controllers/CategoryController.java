/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ServiceService;
import domain.Category;
import domain.Service;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private ServiceService	serviceService;


	// Constructors -----------------------------------------------------------

	public CategoryController() {
		super();
	}

	// Listing ----------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId) {
		ModelAndView result;
		Collection<Category> categories;

		if (categoryId == null)
			categories = this.categoryService.findRoots();
		else
			categories = this.categoryService.findChilds(categoryId);

		result = new ModelAndView("category/list");
		result.addObject("categories", categories);
		result.addObject("requestURI", "category/list.do");
		return result;
	}

	// Creation ---------------------------------------------------------------

	// Ancillary Methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView result;

		result = this.createEditModelAndView(category, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String messageCode) {
		ModelAndView result;
		Collection<Category> categories;
		Collection<Service> services;
		boolean deleteable;

		deleteable = false;
		if (category.getId() != 0) {
			services = this.serviceService.findByCategoryId(category.getId());
			deleteable = services.isEmpty();
		}
		categories = this.categoryService.findParentable(category);
		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("message", messageCode);
		result.addObject("categories", categories);
		result.addObject("deleteable", deleteable);

		return result;
	}

}
