
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Answer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AnswerTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	AnswerService	answerService;
	@Autowired
	QuestionService	questionService;


	// Tests ------------------------------------------------------------------

	@Test
	public void CreateAndSaveDriver() {
		final Object testingData[][] = {
			{
				//Caso positivo (deberia ir)
				"user5", "question6", "RSVP5", "text", null
			
			}, {
				//un usuario que ya esta joined
				"user1", "question2", "RSVP1", "text", IllegalArgumentException.class
			}, {
				//user en rsvp que no es suyo
				"user2", "question11", "RSVP7", "text", IllegalArgumentException.class
			}, {
				//una question que no es
				"user5", "question11", "RSVP5", "text", IllegalArgumentException.class
			}, {
				//text en blanco
				"user7", "question12", "RSVP7", "", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++){
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	}
	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String question, final String RSVP, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			final Integer questionId = super.getEntityId(question);
			final Integer rsvpId = super.getEntityId(RSVP);

			this.authenticate(beanName);
			final Answer answer = this.answerService.create(questionId);
			answer.setText(text);
			this.answerService.save(answer, rsvpId);
			this.answerService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
