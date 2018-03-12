
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.QuestionService;
import controllers.AbstractController;
import domain.Question;

@Controller
@RequestMapping("/question/administrator")
public class QuestionAdministratorController extends AbstractController {

	//Supporting Services
	@Autowired
	private QuestionService	questionService;


	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int questionId) {
		ModelAndView result;
		Question question;

		question = this.questionService.findOne(questionId);
		this.questionService.delete(question);
		result = new ModelAndView("redirect:/question/list.do?rendezvousId=" + question.getRendezvous().getId());

		return result;
	}
}
