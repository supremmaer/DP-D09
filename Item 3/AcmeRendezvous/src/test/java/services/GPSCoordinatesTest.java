
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.GPSCoordinates;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class GPSCoordinatesTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private GPSCoordinatesService	gpsCoordinatesService;

	@Autowired
	private RendezvousService		rendezvousService;


	// Tests --------------------------------------------------------

	@Test
	public void CreateAnSaveDriver() {
		final Object testingData[][] = {
			//Creacion correcta de un rendezvous
			{
				"user1", "rendezvous2", 1.0, 2.0, null
			}, {
				//Error por coordenada con valor null
				"user1", "rendezvous2", 1.0, null, NullPointerException.class
			}, {
				"user1", "rendezvous2", null, 1.0, NullPointerException.class
			}, {
				//Un usuario no puede crear coordenadas en un rendezvous ajeno
				"user2", "rendezvous2", 1.0, 2.0, IllegalArgumentException.class
			}, {
				"manager1", "rendezvous2", 1.0, 2.0, IllegalArgumentException.class
			}, {
				//No se pueden crear dos coordenadas en el mismo rendezvous
				"user1", "rendezvous1", 1.0, 2.0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (Double) testingData[i][2], (Double) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	@Test
	public void DeleteDriver() {
		final Object testingData[][] = {
			//Borrado correcto de un rendezvous
			{
				"admin", "gpscoordinates1", null
			//Un usuario no puede borrar unas coordenadas
			}, {
				"user2", "gpscoordinates1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String rendezvous, final Double latitude, final Double longitude, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Integer rendezvousId = super.getEntityId(rendezvous);
			final Rendezvous rendezvousEntity = this.rendezvousService.findOne(rendezvousId);

			this.authenticate(beanName);
			final GPSCoordinates coordenadas = new GPSCoordinates();
			coordenadas.setLatitude(latitude);
			coordenadas.setLongitude(longitude);
			this.gpsCoordinatesService.save(coordenadas, rendezvousEntity.getId());
			this.gpsCoordinatesService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String user, final String beanId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Integer gpsId = super.getEntityId(beanId);
			final GPSCoordinates gps = this.gpsCoordinatesService.findOne(gpsId);

			this.authenticate(user);
			this.gpsCoordinatesService.delete(gps);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
