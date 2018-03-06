/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Comment;

import services.CommentService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
  CommentService commentService;
	// Tests ------------------------------------------------------------------


	@Test
	public void CreateAndSaveDriver() {
		final Object testingData1[][] = {
			{
				//Un admin no puede crear
				"admin", "rendezvous1", IllegalArgumentException.class
			},{
				//Un admin no puede crear
				"admin", "rendezvous2", IllegalArgumentException.class
			}, {
				//un usuario que no tiene rsvp
				"user7", "rendezvous1", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", null
			}, {
				"user2", "rendezvous1", null
			}
		};

		for (int i = 0; i < testingData1.length; i++)
			this.createAndSaveTemplate((String) testingData1[i][0], (String) testingData1[i][1], (Class<?>) testingData1[i][2]);
	}
	@Test
	public void DeleteDriver() {
		final Object testingData2[][] = {
			{
				//Un user no puede borrar
				"user1", "comment1", IllegalArgumentException.class
			}, {
				//un usuario no puede borrar
				"user1", "comment2", IllegalArgumentException.class
			}, {
				//un usuario no puede borrar
				"user1", "comment2", IllegalArgumentException.class
			
			}, {
				"admin", "comment2", null
			}, {
				"admin", "comment1", null
			}
		};

		for (int i2 = 0; i2 < testingData2.length; i2++)
			this.deleteTemplate((String) testingData2[i2][0], (String) testingData2[i2][1], (Class<?>) testingData2[i2][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String rendezvous, final Class<?> expected) {
		Class<?> caught;
		int dbId;
		
		caught = null;
		try {
			dbId = super.getEntityId(beanName);
			Integer rendezvousId = super.getEntityId(rendezvous);
	
			authenticate(beanName);
			Comment comment=commentService.create(rendezvousId, null);
			commentService.save(comment);
			unauthenticate();
		
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	
	
	protected void deleteTemplate(final String beanName, final String commentString, final Class<?> expected) {
		Class<?> caught;
		int dbId;
		
		caught = null;
		try {
			dbId = super.getEntityId(beanName);
			Integer commentId = super.getEntityId(commentString);
			Comment comment = this.commentService.findOne(commentId);
	
			authenticate(beanName);
			commentService.delete(comment);
		
			unauthenticate();
		
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
