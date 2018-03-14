
package services;

import javax.transaction.Transactional;

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
				"user7", "question12", "RSVP7", "text", null
			}, {
				//un usuario sin rsvp iniciado 
				"user7", "question1", "RSVP1", "text", IllegalArgumentException.class
			}

			, {

				//un usuario que ya esta joined
				"user1", "question2", "RSVP1", "text", IllegalArgumentException.class
			}, {
				//user en rsvp que no es suyo
				"user2", "question11", "RSVP7", "text", IllegalArgumentException.class
			}, {
				//una question que no es
				"user7", "question1", "RSVP7", "text", IllegalArgumentException.class
			}, {
				//un usuario erroneo
				"user2", "question12", "RSVP7", "text", IllegalArgumentException.class
			}, {
				//un usuario erroneo
				"user4", "question12", "RSVP7", "text", IllegalArgumentException.class
			}, {
				//rsvp y question erroneo
				"user4", "question1", "RSVP4", "text", IllegalArgumentException.class
			}, {
				//rsvp y question erroneo
				"user4", "question1", "RSVP2", "text", IllegalArgumentException.class
			}, {
				//text en blanco
				"user7", "question12", "RSVP7", "", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
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
