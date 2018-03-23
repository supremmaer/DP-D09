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
import domain.Rendezvous;
import domain.Request;
import domain.Service;
import forms.RequestForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RequestService		requestService;

	// Auxiliary methods ------------------------------------------------------
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creacion correcta de un request
				"user1", "service2", "rendezvous3", "one comment", "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Creacion correcta de un request sin comentario
				"user1", "service2", "rendezvous3", null, "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Creacion correcta de un request con comentario blanco
				"user1", "service2", "rendezvous3", "", "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Request sin user logueado
				null, "service2", "rendezvous3", "", "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Request sin servicio
				"user1", null, "rendezvous3", "", "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Request sin Rendezvous
				"user1", "service2", "rendezvous3", "", "holder", "brand", "4539433728995809", 10, 20, 150, null
			}, {	//Request con holdername vacio
				"user1", "service2", "rendezvous3", "one comment", "", "brand", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
			}, {	//Request con holdername nulo
				"user1", "service2", "rendezvous3", "one comment", null, "brand", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
			}, {	//Request con brandname vacio
				"user1", "service2", "rendezvous3", "one comment", "holder", "", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
			}, {	//Request con brandname nulo
				"user1", "service2", "rendezvous3", "one comment", "holder", null, "4539433728995809", 10, 20, 150, ConstraintViolationException.class
			}
		//, {	//Request con creditnumber vacio
		//	"user1", "service2", "rendezvous3", "one comment", "holder", "brand", "", 10, 20, 150, IllegalArgumentException.class
		//}
		//	, {	//Request con creditnumber nulo
		//		"user1", "service2", "rendezvous3", "one comment", "holder", "brand", null, 10, 20, 150, IllegalArgumentException.class
		//	}
		//	, {	//Request con ExpirationYear nulo
		//		"user1", "service2", "rendezvous3", "one comment", "holder", "brand", "4539433728995809", null, 20, 150, java.lang.NullPointerException.class
		//	}
		//, {	//Request con ExpirationMonth nulo
		//		"user1", "service2", "rendezvous3", "one comment", "holder", "brand", "4539433728995809", 10, null, 150, ConstraintViolationException.class
		//	}, {	//Request con CVV nulo
		//		"user1", "service2", "rendezvous3", "one comment", "holder", "brand", "4539433728995809", 10, 20, null, ConstraintViolationException.class
		//}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(int) testingData[i][7], (int) testingData[i][8], (int) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String serviceBeanName, final String rendezvousBeanName, final String comment, final String holdername, final String brandname, final String number, final int expirationMonth,
		final int expirationYear, final int CVV, final Class<?> expected) {
		Class<?> caught;
		RequestForm requestForm;
		Service service;
		int serviceID;
		int rendezvousId;
		Rendezvous rendezvous;
		final Request request;
		caught = null;
		try {

			this.authenticate(userName);
			serviceID = super.getEntityId(serviceBeanName);
			service = this.serviceService.findOne(serviceID);
			rendezvousId = super.getEntityId(rendezvousBeanName);
			rendezvous = this.rendezvousService.findOne(rendezvousId);

			requestForm = new RequestForm();
			requestForm.setService(service);
			requestForm.setRendezvous(rendezvous);
			requestForm.setComment(comment);
			requestForm.setHolderName(holdername);
			requestForm.setBrandName(brandname);
			requestForm.setNumber(number);
			requestForm.setExpirationMonth(expirationMonth);
			requestForm.setExpirationYear(expirationYear);
			requestForm.setCVV(CVV);

			request = this.requestService.save(requestForm);
			this.requestService.save(request);
			this.requestService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
