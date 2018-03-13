
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import domain.Answer;



import services.AnswerService;

import services.QuestionService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AnswerTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
  AnswerService answerService;
	@Autowired
	QuestionService questionService;
	// Tests ------------------------------------------------------------------


	@Test
	public void CreateAndSaveDriver() {
		final Object testingData[][] = {
			{
				//un usuario que ya esta joined
				"user1", "question2","RSVP1", IllegalArgumentException.class
			}
			, {
				//un usuario sin rsvp iniciado 
				"user7", "question1","RSVP1", IllegalArgumentException.class
			}
			//Caso positivo (deberia ir)
				, {
					"user7", "question12","RSVP7", null
			}
				, {
					//user en rsvp que no es suyo
				"user2", "question11","RSVP7", IllegalArgumentException.class
			}
				, {
					//una question que no es
				"user7", "question1","RSVP7", IllegalArgumentException.class
			}
				, {
					//un usuario erroneo
				"user2", "question12","RSVP7", IllegalArgumentException.class
			}
				, {
					//un usuario erroneo
				"user4", "question12","RSVP7", IllegalArgumentException.class
			}
				, {
					//rsvp y question erroneo
				"user4", "question1","RSVP4", IllegalArgumentException.class
			}
				, {
					//rsvp y question erroneo
				"user4", "question1","RSVP2", IllegalArgumentException.class
			}
				, {
					//un question no correcta
				"user4", "question11","RSVP7", IllegalArgumentException.class
			}
				
		};

		for (int i = 0; i < testingData.length; i++)
			this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1],(String) testingData[i][2], (Class<?>) testingData[i][3]);
	}


	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String beanName, final String question,final String RSVP, final Class<?> expected) {
		Class<?> caught;
	
		
		caught = null;
		try {
			
			Integer questionId = super.getEntityId(question);
			Integer rsvpId = super.getEntityId(RSVP);
	
			authenticate(beanName);
			Answer answer=answerService.create(questionId);
			answerService.save(answer, rsvpId);
			unauthenticate();
		
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	
	
	

}
