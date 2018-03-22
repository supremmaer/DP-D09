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

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;
import forms.ActorForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActorTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private ActorService	actorService;

	// Auxiliary methods ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	private final Date		date	= new Date();


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creacion correcta de un usuario
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", "123456789", "Address", "USER", true, this.date, null, null
			}, {	//Creacion correcta de un usuario sin direccion
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", "123456789", null, "USER", true, this.date, null, null
			}, {	//Creacion correcta de un usuario sin telefono
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", null, "Address", "USER", true, this.date, null, null
			}, {	//useraccount con nombre vacio
				"", "password", "password", "Name", "surname", "email@email.email", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con nombre vacio
				"userAccountTest", "password", "password", "", "surname", "email@email.email", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con nombre nulo
				"userAccountTest", "password", "password", null, "surname", "email@email.email", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con apellido vacio
				"userAccountTest", "password", "password", "Name", "", "email@email.email", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con apellido nulo
				"userAccountTest", "password", "password", "Name", null, "email@email.email", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con correo vacio
				"userAccountTest", "password", "password", "Name", "Surname", "", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con correo nulo
				"userAccountTest", "password", "password", "Name", "Surname", null, "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//user con correo incorrecto
				"userAccountTest", "password", "password", "Name", "Surname", "jdkshf", "123456789", "Address", "USER", true, this.date, null, ConstraintViolationException.class
			}, {	//Creacion correcta de un manager
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", "123456789", "Address", "MANAGER", true, this.date, "123-123", null
			}, {	//Creacion correcta de un manager sin direccion
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", "123456789", null, "MANAGER", true, this.date, "123-123", null
			}, {	//Creacion correcta de un manager sin telefono
				"userAccountTest", "password", "password", "Name", "surname", "email@email.email", null, "Address", "MANAGER", true, this.date, "123-123", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (String) testingData[i][8], (Boolean) testingData[i][9], (Date) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String username, final String password, final String password2, final String name, final String surname, final String email, final String phone, final String address, final String authority,
		final Boolean agree, final Date birthDate, final String vatNumber, final Class<?> expected) {
		Class<?> caught;
		ActorForm actorForm;
		Actor actor;
		caught = null;
		try {
			actorForm = new ActorForm();
			actorForm.setUsername(username);
			actorForm.setPassword(password);
			actorForm.setPassword2(password2);
			actorForm.setName(name);
			actorForm.setSurname(surname);
			actorForm.setEmail(email);
			actorForm.setPhone(phone);
			actorForm.setAddress(address);
			actorForm.setAuthority(authority);
			actorForm.setAgree(agree);
			actorForm.setBirthDate(birthDate);
			actorForm.setVatNumber(vatNumber);

			actor = this.actorService.create(actorForm);
			this.actorService.register(actor);
			this.actorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
