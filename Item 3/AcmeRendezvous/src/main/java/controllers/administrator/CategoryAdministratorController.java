/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CategoryService	categoryService;


	// Constructors -----------------------------------------------------------

	public CategoryAdministratorController() {
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
		result.addObject("requestURI", "category/administrator/list.do");
		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Category category;

		category = this.categoryService.create();
		result = this.createEditModelAndView(category);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category;

		category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);
		result = this.createEditModelAndView(category);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Category category, final BindingResult binding) {
		ModelAndView result;

		try {
			category = this.categoryService.reconstruct(category, binding);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(category, "category.commit.error");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(category);
		else
			try {
				this.categoryService.save(category);
				result = new ModelAndView("redirect:/category/administrator/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(category, "category.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView cancel(final Category category) {
		ModelAndView result;

		this.categoryService.delete(category.getId());
		result = new ModelAndView("redirect:/category/administrator/list.do");

		return result;
	}

	// Ancillary Methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView result;

		result = this.createEditModelAndView(category, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String messageCode) {
		ModelAndView result;
		Collection<Category> categories;

		categories = this.categoryService.findParentable(category);
		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("message", messageCode);
		result.addObject("categories", categories);

		return result;
	}

}
