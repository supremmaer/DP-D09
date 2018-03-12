
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.QuestionService;
import services.RendezvousService;
import domain.Actor;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/question")
public class QuestionController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------

	public QuestionController() {
		super();
	}

	// Listing ----------------------------------------------------------------	

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Question> questions;
		Actor actor;
		User user;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		questions = this.questionService.findByRendezvous(rendezvous);

		result = new ModelAndView("question/list");
		result.addObject("questions", questions);
		result.addObject("requestURI", "question/list.do");
		if (this.actorService.isLogged()) {
			actor = this.actorService.findByPrincipal();
			if (actor instanceof User) {
				user = (User) actor;
				if (user.getId() == rendezvous.getUser().getId()) {
					result.addObject("rendezvous", rendezvous);
					result.addObject("userId", user.getId());
				}
			}
			result.addObject("rendezvous", rendezvous);
			result.addObject("userId", actor.getId());
		}
		return result;
	}

}
