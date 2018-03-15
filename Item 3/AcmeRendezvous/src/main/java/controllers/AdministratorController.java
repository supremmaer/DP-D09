/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AnnouncementService;
import services.AnswerService;
import services.CategoryService;
import services.CommentService;
import services.ManagerService;
import services.QuestionService;
import services.RSVPService;
import services.RendezvousService;
import services.RequestService;
import services.ServiceService;
import services.UserService;
import domain.Manager;
import domain.Rendezvous;
import domain.Service;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Services --------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RSVPService			rsvpService;

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private CategoryService		categoryService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping("/action-1")
	public ModelAndView action1() {
		ModelAndView result;

		result = new ModelAndView("administrator/action-1");

		return result;
	}

	// Action-2 ---------------------------------------------------------------

	@RequestMapping("/action-2")
	public ModelAndView action2() {
		ModelAndView result;

		result = new ModelAndView("administrator/action-2");

		return result;
	}

	@RequestMapping("/dashboard")
	public ModelAndView dashboard() {
		ModelAndView result;

		Double avgRendezvousesperUser = 0.0;
		Double sdRendezvousesperUser = 0.0;

		Double ratioUserRendezvousvsUserNoRendezvous = 0.0;

		Double avgRSVPperRendezvous = 0.0;
		Double sdRSVPperRendezvous = 0.0;

		Double avgRSVPperUser = 0.0;
		Double sdRSVPperUser = 0.0;

		Collection<Rendezvous> toptenbyRSVP = new ArrayList<Rendezvous>();

		Double avgAnnoucementsperRendezvous = 0.0;
		Double sdAnnoucementsperRendezvous = 0.0;

		Collection<Rendezvous> RendezaboveAverageAnnoun = new ArrayList<Rendezvous>();

		Collection<Rendezvous> RendezaboveAverageRendez = new ArrayList<Rendezvous>();

		Double avgQuestionsperRendezvous = 0.0;
		Double sdQuestionsperRendezvous = 0.0;

		Double avgAnswersperRendezvous = 0.0;
		Double sdAnswerssperRendezvous = 0.0;

		Double avgReplysperComment = 0.0;
		Double sdReplysperComment = 0.0;

		Collection<Service> mostSellers = new ArrayList<Service>();

		Collection<Manager> managersWithMoreServicesThanAVG = new ArrayList<Manager>();

		Collection<Manager> managersMoreCancelledServices = new ArrayList<Manager>();

		Double avgCategoryPerRendezvous = 0.0;
		Double avgServicesPerCategory = 0.0;

		Double avgServicePerRendezvous = 0.0;
		Double minServicePerRendezvous = 0.0;
		Double maxServicePerRendezvous = 0.0;
		Double sdServicePerRendezvous = 0.0;

		Collection<Service> topSellers = new ArrayList<Service>();

		if (this.rendezvousService.findAll().size() > 0) {

			avgRendezvousesperUser = this.rendezvousService.averageRendezvousesperUser();
			sdRendezvousesperUser = this.rendezvousService.standardDeviationRendezvousesperUser();

			ratioUserRendezvousvsUserNoRendezvous = this.userService.ratioUserRendezvousvsUserNoRendezvous();

			if (this.rsvpService.findAll().size() > 0) {

				avgRSVPperRendezvous = this.rsvpService.averageRSVPperRendezvous();
				sdRSVPperRendezvous = this.rsvpService.standardDeviationRSVPperRendezvous();

				avgRSVPperUser = this.rsvpService.averageRSVPperUser();
				sdRSVPperUser = this.rsvpService.standardDeviationRSVPperUser();

				toptenbyRSVP = this.rendezvousService.toptenbyRSVP();
			}

			avgAnnoucementsperRendezvous = this.announcementService.averageAnnouncementperRendezvous();
			sdAnnoucementsperRendezvous = this.announcementService.standardDeviationAnnouncementperRendezvous();

			RendezaboveAverageAnnoun = this.rendezvousService.findRendezvousWithMoreAnnouncementsThanAverage();

			RendezaboveAverageRendez = this.rendezvousService.findRendezvousWithMoreRendezvousesThanAverage();

			avgQuestionsperRendezvous = this.questionService.averageQuestionsperRendezvous();
			sdQuestionsperRendezvous = this.questionService.standardDeviationQuestionsperRendezvous();

			avgAnswersperRendezvous = this.answerService.averageAnswersperRendezvous();
			sdAnswerssperRendezvous = this.answerService.standardDeviationAnswersperRendezvous();

			if (this.commentService.findAll().size() > 0) {
				avgReplysperComment = this.commentService.averageReplysperComment();
				sdReplysperComment = this.commentService.standardDeviationReplysperComment();
			}
			if (this.serviceService.findAll().size() > 0 && this.requestService.findAll().size() > 0) {
				mostSellers = this.serviceService.findMostSellers();
				avgServicePerRendezvous = this.serviceService.getServicesPerRendezvousData().get("average");
				minServicePerRendezvous = this.serviceService.getServicesPerRendezvousData().get("minimum");
				maxServicePerRendezvous = this.serviceService.getServicesPerRendezvousData().get("maximum");
				sdServicePerRendezvous = this.serviceService.getServicesPerRendezvousData().get("standardDeviation");
				topSellers = this.serviceService.findTopFiveSellingServices();
			}

			if (this.serviceService.findAll().size() > 0 && this.managerService.findAll().size() > 0) {
				managersWithMoreServicesThanAVG = this.managerService.managersWithMoreServicesThanAVG();
				managersMoreCancelledServices = this.managerService.managersMoreCancelledServices();
				avgServicesPerCategory = this.serviceService.avgServicesPerCategory();
			}

			avgCategoryPerRendezvous = this.categoryService.avgCategoriesPerRendezvous();

		}

		result = new ModelAndView("administrator/dashboard");

		result.addObject("avgRendezvousesperUser", avgRendezvousesperUser);
		result.addObject("sdRendezvousesperUser", sdRendezvousesperUser);

		result.addObject("ratioUserRendezvousvsUserNoRendezvous", ratioUserRendezvousvsUserNoRendezvous);

		result.addObject("avgRSVPperRendezvous", avgRSVPperRendezvous);
		result.addObject("sdRSVPperRendezvous", sdRSVPperRendezvous);

		result.addObject("avgRSVPperUser", avgRSVPperUser);
		result.addObject("sdRSVPperUser", sdRSVPperUser);

		result.addObject("toptenbyRSVP", toptenbyRSVP);

		result.addObject("avgAnnoucementsperRendezvous", avgAnnoucementsperRendezvous);
		result.addObject("sdAnnoucementsperRendezvous", sdAnnoucementsperRendezvous);

		result.addObject("RendezaboveAverageAnnoun", RendezaboveAverageAnnoun);

		result.addObject("RendezaboveAverageRendez", RendezaboveAverageRendez);

		result.addObject("avgQuestionsperRendezvous", avgQuestionsperRendezvous);
		result.addObject("sdQuestionsperRendezvous", sdQuestionsperRendezvous);

		result.addObject("avgAnswersperRendezvous", avgAnswersperRendezvous);
		result.addObject("sdAnswersperRendezvous", sdAnswerssperRendezvous);

		result.addObject("avgReplysperComment", avgReplysperComment);
		result.addObject("sdReplysperComment", sdReplysperComment);

		result.addObject("mostSellers", mostSellers);

		result.addObject("managersWithMoreServicesThanAVG", managersWithMoreServicesThanAVG);

		result.addObject("managersMoreCancelledServices", managersMoreCancelledServices);

		result.addObject("avgCategoryPerRendezvous", avgCategoryPerRendezvous);

		result.addObject("avgServicesPerCategory", avgServicesPerCategory);

		result.addObject("avgServicePerRendezvous", avgServicePerRendezvous);
		result.addObject("minServicePerRendezvous", minServicePerRendezvous);
		result.addObject("maxServicePerRendezvous", maxServicePerRendezvous);
		result.addObject("sdServicePerRendezvous", sdServicePerRendezvous);

		result.addObject("topSellers", topSellers);

		result.addObject("requestURI", "administrator/dashboard.do");
		return result;
	}

}
