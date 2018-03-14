/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Category;
import domain.Manager;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private CategoryService	categoryService;
	@Autowired
	private ManagerService	managerService;


	// Tests ------------------------------------------------------------------

	@Test
	public void CreateAndSaveDriver() {
		final Object testingData[][] = {

			//un Manager crea un servicio correctamente
			{
				"manager1", "category1", "manager1", "Nombre", "Descripción", "http://url.com", null
			},
			//un Manager crea un servicio para otro manager
			{
				"manager1", "category1", "manager2", "Nombre", "Descripción", "http://url.com", IllegalArgumentException.class
			},
			//un Manager crea un servicio con nombre en blanco
			{
				"manager1", "category1", "manager1", "", "Descripción", "http://url.com", ConstraintViolationException.class
			},
			//un Manager crea un servicio con nombre nulo
			{
				"manager1", "category1", "manager1", null, "Descripción", "http://url.com", ConstraintViolationException.class
			},
			//un Manager crea un servicio con descripción en blanco
			{
				"manager1", "category1", "manager1", "Nombre", "", "http://url.com", ConstraintViolationException.class
			},
			//un Manager crea un servicio con descripción nula
			{
				"manager1", "category1", "manager1", "Nombre", null, "http://url.com", ConstraintViolationException.class
			},
			//un Manager crea un servicio con picture en blanco
			{
				"manager1", "category1", "manager1", "Nombre", "Descripción", "", ConstraintViolationException.class
			},
			//un Manager crea un servicio con picture nula
			{
				"manager1", "category1", "manager1", "Nombre", "Descripción", null, ConstraintViolationException.class
			},
			//un Manager crea un servicio con picture mal formateada
			{
				"manager1", "category1", "manager1", "Nombre", "Descripción", "url.com", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String category, final String manager, final String name, final String description, final String picture, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Integer categoryId = super.getEntityId(category);
			final Category categoryEntity = this.categoryService.findOne(categoryId);
			final Integer managerId = super.getEntityId(manager);
			final Manager managerEntity = this.managerService.findOne(managerId);

			this.authenticate(beanName);
			domain.Service service;
			service = this.serviceService.create();
			service.setCategory(categoryEntity);
			service.setDescription(description);
			service.setManager(managerEntity);
			service.setName(name);
			service.setPicture(picture);
			this.serviceService.save(service);
			this.serviceService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
