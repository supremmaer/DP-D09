
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RendezvousService	rendezvousService;

	Calendar					calendario	= new GregorianCalendar(2020, 12, 14);
	Date						fechaValida	= this.calendario.getTime();


	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creacion correcta de un rendezvous
				"user1", "RendezvousPrueba", "description", this.fechaValida, "http://www.imagen.com.mx/assets/img/imagen_share.png", null
			}, {
				//Titulo Vacio
				"user1", "", "description", this.fechaValida, "http://www.imagen.com.mx/assets/img/imagen_share.png", ConstraintViolationException.class
			}, {
				//Descripcion Vacia
				"user1", "NoTengoDescripcion", "", this.fechaValida, "http://www.imagen.com.mx/assets/img/imagen_share.png", ConstraintViolationException.class
			},
			//			{
			//				//Fecha Vacia
			//				"user1", "No tengo Fecha", "Mi fecha no Existe", null, "http://www.imagen.com.mx/assets/img/imagen_share.png", ConstraintViolationException.class
			//			},
			{	//Imagen No Valida
				"user1", "No Tengo Imagen", "No me han Puesto Imagen", this.fechaValida, "nosoyunaimagen", ConstraintViolationException.class
			}, { //Un manager no puede crear un rendezvous
				"manager1", "RendezvousPrueba2", "description", this.fechaValida, "http://www.imagen.com.mx/assets/img/imagen_share.png", IllegalArgumentException.class
			}, { //Un admin no puede crear un rendezvous
				"admin", "RendezvousPrueba2", "description", this.fechaValida, "http://www.imagen.com.mx/assets/img/imagen_share.png", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Un usuario no puede poner en deleted un rendezvous que no es suyo
				"user2", "rendezvous1", IllegalArgumentException.class
			}, {
				//Un manager no puede poner en deleted un rendezvous
				"manager1", "rendezvous1", IllegalArgumentException.class
			}, {
				//Un usuario puede poner en deleted un rendezvous suyo
				"user1", "rendezvous1", null
			}, {
				//Un Admin no puede poner en deleted un rendezvous, sino que lo borra con remove
				"admin", "rendezvous2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void removeDriver() {
		final Object testingData[][] = {
			{	//Un manager no puede borrar un rendezvous
				"manager1", "rendezvous1", IllegalArgumentException.class
			}, {
				//Un usuario no puede borrar un rendezvous suyo, solo podria ponerlo en deleted, que es el metodo de antes
				"user1", "rendezvous1", null
			}, {
				//Un Admin puede borrar un rendezvous
				"admin", "rendezvous3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String name, final String description, final Date moment, final String picture, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;

		caught = null;
		try {
			this.authenticate(userName);
			rendezvous = this.rendezvousService.create();
			rendezvous.setName(name);
			rendezvous.setDescription(description);
			rendezvous.setPicture(picture);
			rendezvous.setMoment(this.fechaValida);
			this.rendezvousService.save(rendezvous);
			this.rendezvousService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String rendezvousBeanName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;

		caught = null;
		try {
			this.authenticate(userName);
			rendezvousId = super.getEntityId(rendezvousBeanName);
			this.rendezvousService.delete(rendezvousId);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void removeTemplate(final String userName, final String rendezvousBeanName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		Rendezvous rendezvous;

		caught = null;
		try {
			this.authenticate(userName);
			rendezvousId = super.getEntityId(rendezvousBeanName);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			this.rendezvousService.remove(rendezvous);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
