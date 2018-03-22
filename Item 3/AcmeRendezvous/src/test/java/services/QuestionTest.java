
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
				"user1", "rendezvous1", "text", IllegalArgumentException.class
			}, {
				//Un user no puede crear en un rendezvous que no es suyo
				"user1", "rendezvous7", "text", IllegalArgumentException.class
			}, {
				//Un admin no puede crear preguntas
				"admin", "rendezvous6", "text", IllegalArgumentException.class
			}, {
				//Un manager no puede crear preguntas
				"manager1", "rendezvous6", "text", IllegalArgumentException.class
			}, {
				//este deberia ir bien
				"user1", "rendezvous6", "el siguiente va a ser nulo", null
			}, {
				//este es nulo
				"user1", "rendezvous6", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData1.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData1[i][0], (String) testingData1[i][1], (String) testingData1[i][2], (Class<?>) testingData1[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{
				//Un manager no puede borrar una pregunta
				"manager1", "question10", IllegalArgumentException.class
			}, {//Un usuario no puede borrar una pregunta de un rendezvous que no es suyo
				"user2", "question10", IllegalArgumentException.class
			}, {
				//Un usuario no puede borrar una pregunta de un rendezvoous final
				"user1", "question1", IllegalArgumentException.class
			}, {
				//Un usuario puede borrar una pregunta de un rendezvous no final
				"user1", "question10", null
			}, {
				//Un admin puede borrar una pregunta
				"admin", "question9", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String rendezvous, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			final Integer rendezvousId = super.getEntityId(rendezvous);

			this.authenticate(beanName);
			final Question question = this.questionService.create(rendezvousId);
			question.setText(text);
			this.questionService.save(question);
			this.questionService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String beanName, final String questionBeanName, final Class<?> expected) {
		Class<?> caught;
		int questionId;
		Question question;

		caught = null;
		try {
			this.authenticate(beanName);
			questionId = super.getEntityId(questionBeanName);
			question = this.questionService.findOne(questionId);
			this.questionService.delete(question);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
