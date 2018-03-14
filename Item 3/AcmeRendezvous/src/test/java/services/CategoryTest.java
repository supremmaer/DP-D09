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

import java.util.ArrayList;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	CategoryService	categoryService;


	// Tests ------------------------------------------------------------------

	@Test
	public void CreateAndSaveDriver() {
		final Object testingData1[][] = {
			{
				"admin", "alfonso", "description", null

			}, {
				//	Un user no puede crear
				"user1", "nameX", "descriptionX", IllegalArgumentException.class
			}, {
				//Un user no puede crear
				"user2", "nameX", "descriptionX", IllegalArgumentException.class
			}, {
				//un manager no puede crear
				"manager1", "nameX", "descriptionX", IllegalArgumentException.class
			}, {
				"admin", "", "descriptionX", ConstraintViolationException.class
			}, {
				"admin", "nameX", "", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData1.length; i++)
			this.createAndSaveTemplate((String) testingData1[i][0], (String) testingData1[i][1], (String) testingData1[i][2], (Class<?>) testingData1[i][3]);
	}
	@Test
	public void DeleteDriver() {
		final Object testingData2[][] = {
			{

				"admin", "category8", null
			}, {
				"admin", "category7", null
			}, {
				//Un user no puede borrar
				"user1", "category4", IllegalArgumentException.class
			}, {
				//un usuario no puede borrar
				"admin", "category2", IllegalArgumentException.class
			}, {
				//si tiene hijos no se puede borrar
				"admin", "category1", IllegalArgumentException.class

			}
		};

		for (int i2 = 0; i2 < testingData2.length; i2++)
			this.deleteTemplate((String) testingData2[i2][0], (String) testingData2[i2][1], (Class<?>) testingData2[i2][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String name, final String description, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(beanName);
			final Category category = this.categoryService.create();
			category.setCategories(new ArrayList<Category>());
			category.setParent(null);
			category.setName(name);
			category.setDescription(description);
			this.categoryService.save(category);
			this.categoryService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String beanName, final String categoryString, final Class<?> expected) {
		Class<?> caught;
		int dbId;

		caught = null;
		try {
			dbId = super.getEntityId(categoryString);
			//Category category = this.categoryService.findOne(dbId);

			this.authenticate(beanName);
			this.categoryService.delete(dbId);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
