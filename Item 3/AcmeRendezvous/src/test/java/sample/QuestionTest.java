
package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.QuestionService;
import utilities.AbstractTest;
import domain.Question;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class QuestionTest extends AbstractTest {

	//The SUT --------------------------
	@Autowired
	QuestionService	questionService;


	//Tests ----------------------------

	@Test
	public void CreateAndSaveDriver() {
		final Object testingData1[][] = {
			{
				//Un usuario no puede crear cuando un rendezvous esta en final
				"user1", "rendezvous1", IllegalArgumentException.class
			}, {
				//Un user no puede crear en un rendezvous que no es suyo
				"user1", "rendezvous7", IllegalArgumentException.class
			}, {
				//Un admin no puede crear preguntas
				"admin", "rendezvous6", IllegalArgumentException.class
			}, {
				//Un manager no puede crear preguntas
				"manager1", "rendezvous6", IllegalArgumentException.class
			}, {
				//este deberia ir bien
				"user1", "rendezvous6", null
			}
		};

		for (int i = 0; i < testingData1.length; i++)
			this.createAndSaveTemplate((String) testingData1[i][0], (String) testingData1[i][1], (Class<?>) testingData1[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String rendezvous, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			final Integer rendezvousId = super.getEntityId(rendezvous);

			this.authenticate(beanName);
			final Question question = this.questionService.create(rendezvousId);
			this.questionService.save(question);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
