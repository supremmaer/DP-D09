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
import domain.Announcement;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AnnouncementTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private AnnouncementService	announcementService;

	// Auxiliary methods ------------------------------------------------------
	@Autowired
	private RendezvousService	rendezvousService;


	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Anuncio para un rendezvous en modo final
				"user1", "rendezvous1", "title", "description", null
			}, {
				//Anuncio para un rendezvous en modo no final
				"user1", "rendezvous2", "title", "description", null
			}, {
				//Anuncio con titulo null
				"user1", "rendezvous2", null, "description", ConstraintViolationException.class
			}, {
				//Anuncio con titulo blank
				"user1", "rendezvous2", "", "description", ConstraintViolationException.class
			}, {
				//Anuncio con descripcion null
				"user1", "rendezvous2", "title", null, ConstraintViolationException.class
			}, {
				//Anuncio con descripcion blank
				"user1", "rendezvous2", "title", "", ConstraintViolationException.class
			}, {
				//Anuncio para un rendezvous en modo final
				"user1", "rendezvous1", "title123", "description123", null
			}, {
				//Usuario no creador del rendezvous
				"user2", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				//Actor no autenticado
				null, "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				//Un manager no puede editar
				"manager1", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				//Un admin no puede editar
				"admin", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				//No se puede crear un anuncio para un rendezvous cancelado
				"user2", "rendezvous8", "title", "description", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			//			this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void delelteDriver() {
		final Object testingData[][] = {
			{	//Un usuario no puede borrar un anuncio
				"user1", "announcement1", IllegalArgumentException.class
			}, {
				//Un managerr puede borrar un anuncio
				"manager1", "announcement1", IllegalArgumentException.class
			}, {
				//Un admin puede borrar un anuncio
				"admin", "announcement1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String rendezvousBeanName, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		Rendezvous rendezvous;
		Announcement announcement;

		caught = null;
		try {
			this.authenticate(userName);
			rendezvousId = super.getEntityId(rendezvousBeanName);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			announcement = this.announcementService.create();
			announcement.setTitle(title);
			announcement.setDescription(description);
			announcement.setRendezvous(rendezvous);
			this.announcementService.save(announcement);
			this.announcementService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String announcementBeanName, final Class<?> expected) {
		Class<?> caught;
		int announcementId;

		caught = null;
		try {
			this.authenticate(userName);
			announcementId = super.getEntityId(announcementBeanName);
			this.announcementService.delete(announcementId);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
