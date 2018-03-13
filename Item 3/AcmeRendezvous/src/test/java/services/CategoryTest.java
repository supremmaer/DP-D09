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

import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Category;
import domain.Comment;

import services.CommentService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
  CategoryService categoryService;
	// Tests ------------------------------------------------------------------


	@Test
	public void CreateAndSaveDriver() {
		final Object testingData1[][] = {
			{
				//Un user no puede crear
				"user1",  IllegalArgumentException.class
			},{
				//Un user no puede crear
				"user2",  IllegalArgumentException.class
			}, {
				//un user no puede crear
				"user3",  IllegalArgumentException.class
			}, {
				"admin",  null
			}, {
				"admin",  null
			}
		};

		for (int i = 0; i < testingData1.length; i++)
			this.createAndSaveTemplate((String) testingData1[i][0], (Class<?>) testingData1[i][1]);
	}
	@Test
	public void DeleteDriver() {
		final Object testingData2[][] = {
			{
				//Un user no puede borrar
				"user1", "category4", IllegalArgumentException.class
			}, {
				//un usuario no puede borrar
				"user3", "category2", IllegalArgumentException.class
			}, {
				//si tiene hijos no se puede borrar
				"admin", "category1", IllegalArgumentException.class
			
			}, {
				"admin", "category8", null
			}, {
				"admin", "category7", null
			}
		};

		for (int i2 = 0; i2 < testingData2.length; i2++)
			this.deleteTemplate((String) testingData2[i2][0], (String) testingData2[i2][1], (Class<?>) testingData2[i2][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final Class<?> expected) {
		Class<?> caught;
	
		
		caught = null;
		try {
		
			
	
			authenticate(beanName);
			Category category=categoryService.create();
			category.setCategories(new HashSet<Category>());
			category.setParent(null);
			categoryService.save(category);
			unauthenticate();
		
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
	
			authenticate(beanName);
			categoryService.delete(dbId);
			unauthenticate();
		
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
