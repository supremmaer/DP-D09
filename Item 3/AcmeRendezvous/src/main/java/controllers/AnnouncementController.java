/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AnnouncementService;
import services.RendezvousService;
import domain.Announcement;
import domain.Rendezvous;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------

	public AnnouncementController() {
		super();
	}

	// Listing ----------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Announcement> announcements;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		announcements = this.announcementService.findByRendezvous(rendezvous);
		result = new ModelAndView("announcement/list");
		result.addObject("announcements", announcements);
		result.addObject("requestURI", "announcement/list.do");
		return result;
	}

}
