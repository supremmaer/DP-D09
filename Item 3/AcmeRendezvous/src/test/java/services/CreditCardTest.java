
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreditCardTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Tests --------------------------------------------------------

	@Test
	public void CreateAndSaveDriver() {
		final Object testingData[][] = {
			//Creacion correcta de una creditCard
			{
				"user1", 123, "visa", 11, 21, "Sr con Tarjeta", "1111222233334444", null
			}, { // CVV Incorrecto
				"user1", 12123, "visa", 11, 20, "Sr con Tarjeta", "1111222233334444", ConstraintViolationException.class
			}, { // Número incorrecto
				"user1", 344, "visa", 10, 21, "Sr con Tarjeta", "1234532154", ConstraintViolationException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			this.createAndSaveTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	@Test
	public void findOneDriver() {
		// findOne correcto
		this.findOneTemplate("user1", "creditCard1", null);
		// findOne de otro usuario
		this.findOneTemplate("user2", "creditCard1", IllegalArgumentException.class);
		// findOne de un manager
		this.findOneTemplate("manager1", "creditCard1", IllegalArgumentException.class);
	}

	@Test
	public void deleteDriver() {
		// delete correcto
		this.deleteTemplate("user1", "creditCard1", null);
		// delete de otro usuario
		this.deleteTemplate("user2", "creditCard1", IllegalArgumentException.class);
		// delete de un manager
		this.deleteTemplate("manager1", "creditCard1", IllegalArgumentException.class);
	}

	// Ancillary methods ------------------------------------------------------
	protected CreditCard createAndSaveTemplate(final String beanName, final Integer cvv, final String brand, final Integer month, final Integer year, final String name, final String number, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		CreditCard result = null;
		try {
			this.authenticate(beanName);
			final CreditCard creditCard = this.creditCardService.create();
			creditCard.setCVV(cvv);
			creditCard.setBrandName(brand);
			creditCard.setExpirationMonth(month);
			creditCard.setExpirationYear(year);
			creditCard.setHolderName(name);
			creditCard.setNumber(number);
			result = this.creditCardService.save(creditCard);
			this.creditCardService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		return result;
	}

	protected void findOneTemplate(final String beanName, final String creditCardBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(beanName);
			final int creditCardId = super.getEntityId(creditCardBean);
			final CreditCard creditCard = this.creditCardService.findOne(creditCardId);
			Assert.notNull(creditCard);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	protected void deleteTemplate(final String beanName, final String creditCardBean, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final int creditCardId = super.getEntityId(creditCardBean);
			this.authenticate("user1");
			final CreditCard creditCard = this.creditCardService.findOne(creditCardId);
			this.unauthenticate();

			this.authenticate(beanName);
			this.creditCardService.delete(creditCard);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
