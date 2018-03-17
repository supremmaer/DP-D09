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
import domain.Config;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ConfigTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private ConfigService	configService;


	// Tests ------------------------------------------------------------------

	@Test
	public void EditAndSaveDriver() {
		final Object testingData[][] = {

			//un Admin edita config correctamente
			{
				"admin", "https://url.com", "Nombre negocio", "Hello there", "Buenas", null

			},
			//un Admin edita config sin banner
			{
				"admin", null, "Nombre negocio", "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config con banner vacío
			{
				"admin", "", "Nombre negocio", "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config que no es url
			{
				"admin", "hola", "Nombre negocio", "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config sin nombre
			{
				"admin", "https://url.com", null, "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config con nombre vacío
			{
				"admin", "https://url.com", "", "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config sin saludo en inglés
			{
				"admin", "https://url.com", "Nombre negocio", null, "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config con saludó en inglés vacío
			{
				"admin", "https://url.com", "Nombre negocio", "", "Buenas", ConstraintViolationException.class

			},
			//un Admin edita config sin saludo en español
			{
				"admin", "https://url.com", "Nombre negocio", "Hello there", null, ConstraintViolationException.class

			},
			//un Admin edita config con saludó en español vacío
			{
				"admin", "https://url.com", "Nombre negocio", "Hello there", "", ConstraintViolationException.class

			},
			//un usuario edita config
			{
				"user1", "https://url.com", "Nombre negocio", "Hello there", "Buenas", ConstraintViolationException.class

			},
			//un manager edita config
			{
				"manager1", "https://url.com", "Nombre negocio", "Hello there", "Buenas", ConstraintViolationException.class

			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.editAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void editAndSaveTemplate(final String beanName, final String url, final String bussinessName, final String espMessage, final String engMessage, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			Config config;

			this.authenticate(beanName);
			config = this.configService.findConfiguration();
			config.setBanner(url);
			config.setBussinessName(bussinessName);
			config.setWelcomeEnglish(engMessage);
			config.setWelcomeSpanish(espMessage);
			this.configService.save(config);
			this.configService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
