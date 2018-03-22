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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.RSVP;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RSVPTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RSVPService	rsvpService;


	// Tests ------------------------------------------------------------------

	@Test
	public void createDriver() {
		final Object testingData[][] = {
			{	//RSVP para un rendezvous no unido final
				"user4", "rendezvous7", null
			}, {
				//RSVP para un rendezvous no unido no final
				"user4", "rendezvous2", IllegalArgumentException.class
			}, {
				//RSVP para un rendezvous unido
				"user4", "rendezvous1", IllegalArgumentException.class
			}, {
				//RSVP para rendezvous creado por si mismo
				"user1", "rendezvous1", IllegalArgumentException.class
			}, {
				//RSVP con actor no autentificado
				null, "rendezvous1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void saveDriver() {
		final Object testingData[][] = {
			{	//Joined to cancelled
				"user7", "RSVP1", true, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveTemplate((String) testingData[i][0], (String) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	@Test
	public void cancelDriver() {
		final Object testingData[][] = {
			{	//Cancel el RSVP
				"user7", "RSVP1", null
			}, {
				//No propietario del user intenta cancelar RSVP de otro user
				"user4", "RSVP1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.cancelTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createTemplate(final String userName, final String rendezvousBeanName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;

		caught = null;
		try {
			this.authenticate(userName);
			rendezvousId = super.getEntityId(rendezvousBeanName);
			this.rsvpService.create(rendezvousId);
			this.rsvpService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void saveTemplate(final String userName, final String rsvpBeanName, final boolean joined, final Class<?> expected) {
		Class<?> caught;
		int rsvpId;
		RSVP rsvp;

		caught = null;
		try {
			this.authenticate(userName);
			rsvpId = super.getEntityId(rsvpBeanName);
			rsvp = this.rsvpService.findOne(rsvpId);
			rsvp.setJoined(joined);
			this.rsvpService.save(rsvp);
			this.rsvpService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void cancelTemplate(final String userName, final String rsvpBeanName, final Class<?> expected) {
		Class<?> caught;
		int rsvpId;
		RSVP rsvp;

		caught = null;
		try {
			this.authenticate(userName);
			rsvpId = super.getEntityId(rsvpBeanName);
			rsvp = this.rsvpService.findOne(rsvpId);
			this.rsvpService.cancel(rsvp);

			this.rsvpService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
