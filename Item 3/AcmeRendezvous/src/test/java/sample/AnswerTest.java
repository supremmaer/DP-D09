/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Answer;

import domain.Question;

import services.AnswerService;
import services.CommentService;
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
		int dbId;
		
		caught = null;
		try {
			dbId = super.getEntityId(beanName);
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
