
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RendezvousRepository;
import domain.Actor;
import domain.Administrator;
import domain.Announcement;
import domain.Comment;
import domain.GPSCoordinates;
import domain.Question;
import domain.RSVP;
import domain.Rendezvous;
import domain.Request;
import domain.User;
import forms.SimilarForm;

@Service
@Transactional
public class RendezvousService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private RendezvousRepository	rendezvousRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService			actorService;
	@Autowired
	private UserService				userService;
	@Autowired
	private AnnouncementService		announcementService;
	@Autowired
	private RSVPService				RSVPService;
	@Autowired
	private CommentService			commentService;
	@Autowired
	private QuestionService			questionService;
	@Autowired
	private GPSCoordinatesService	gpscoordinatesService;
	@Autowired
	private RequestService			requestService;


	// Constructors -----------------------------------------------------------

	public RendezvousService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Rendezvous create() {
		Rendezvous result;
		User user;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		result = new Rendezvous();
		Assert.isTrue(actor instanceof User);
		user = (User) actor;
		result.setUser(user);
		result.setQuestions(new HashSet<Question>());
		result.setAnnouncements(new HashSet<Announcement>());
		result.setRendezvouses(new HashSet<Rendezvous>());

		return result;
	}

	public Collection<Rendezvous> findAll() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Rendezvous delete(final int rendezvousId) {
		Actor actor;
		Rendezvous result, rendezvous;

		actor = this.actorService.findByPrincipal();
		rendezvous = this.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getId() != 0);
		Assert.isTrue(actor.getId() == rendezvous.getUser().getId());

		rendezvous.setDeleted(true);
		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}
	public void remove(final Rendezvous rendezvous) {
		Actor actor;

		final Collection<Announcement> announcements = rendezvous.getAnnouncements();
		final Collection<Rendezvous> allRendezvous = this.findAll();
		final Collection<RSVP> RSVPs = this.RSVPService.findbyRendezvous(rendezvous.getId());
		final Collection<Comment> comments = this.commentService.findByRendezvousIdRoot(rendezvous.getId());
		final Collection<Question> questions = rendezvous.getQuestions();
		final Collection<Request> requests = this.requestService.findByRendezvousID(rendezvous.getId());
		actor = this.actorService.findByPrincipal();
		for (final Announcement a : announcements)
			this.announcementService.delete(a.getId());
		final GPSCoordinates gps = rendezvous.getGPSCoordinates();

		for (final RSVP r : RSVPs)
			this.RSVPService.delete(r);
		for (final Comment c : comments)
			this.commentService.delete(c);
		for (final Question q : questions)
			this.questionService.delete(q);
		for (final Request r : requests)
			this.requestService.delete(r);
		//		eliminar rendezvouses similares
		for (final Rendezvous r : allRendezvous)
			if (r.getRendezvouses().contains(rendezvous)) {
				final Collection<Rendezvous> aux = r.getRendezvouses();
				aux.remove(rendezvous);
				r.setRendezvouses(aux);
				this.rendezvousRepository.save(r);
			}

		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getId() != 0);

		Assert.isTrue(actor instanceof Administrator);

		this.rendezvousRepository.delete(rendezvous.getId());
		if (gps != null)
			this.gpscoordinatesService.delete(gps);

	}

	public Rendezvous save(final Rendezvous rendezvous) {
		Rendezvous result, db;
		Actor actor;
		Date today;

		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor != null);
		Assert.isTrue(actor instanceof User);
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getUser().getId() == actor.getId());
		today = new Date();
		if (rendezvous.getId() != 0) {
			db = this.findOne(rendezvous.getId());
			Assert.isTrue(!db.isFinalVersion());
			Assert.isTrue(db.isDeleted() == false);
			Assert.isTrue(db.getMoment().after(today));
		}
		Assert.isTrue(rendezvous.getMoment().after(today));

		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	public Rendezvous setFinal(final int rendezvousId) {
		Rendezvous result, rendezvous;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor != null);
		Assert.isTrue(actor instanceof User);

		rendezvous = this.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getUser().getId() == actor.getId());
		Assert.isTrue(!rendezvous.isFinalVersion());
		Assert.isTrue(rendezvous.isDeleted() == false);
		rendezvous.setFinalVersion(true);

		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	//Other business methods

	public Rendezvous findOne(final int rendezvousId) {
		Rendezvous result;

		result = this.rendezvousRepository.findOne(rendezvousId);

		return result;
	}

	public Rendezvous findByAnnouncement(final Announcement announcement) {
		Rendezvous result;

		Assert.notNull(announcement);
		Assert.isTrue(announcement.getId() != 0);

		result = this.rendezvousRepository.findByAnnouncementId(announcement.getId());

		return result;
	}

	public Collection<Rendezvous> findRendevousWithRSVPbyUserId(final int id) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findRendevousWithRSVPbyUserId(id);

		return result;
	}

	public Collection<Rendezvous> findByUser(final int userId) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findByUser(userId);

		return result;
	}

	public Collection<Rendezvous> findSimilar(final int rendezvousId) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findSimilar(rendezvousId);

		return result;
	}

	public void save(final SimilarForm similarForm) {
		Rendezvous rendezvous;
		Rendezvous similar;
		User user;

		user = this.userService.findByPrincipal();

		rendezvous = this.rendezvousRepository.findOne(similarForm.getRendezvous());
		Assert.isTrue(rendezvous.getUser().equals(user));
		similar = this.rendezvousRepository.findOne(similarForm.getSimilar());
		if (!rendezvous.getRendezvouses().contains(similar))
			rendezvous.getRendezvouses().add(similar);
		this.rendezvousRepository.save(rendezvous);
	}

	public Double averageRendezvousesperUser() {
		final Double result = this.rendezvousRepository.averageRendezvousesperUser();
		return result;
	}

	public Double standardDeviationRendezvousesperUser() {
		final Double result = this.rendezvousRepository.standardDeviationRendezvousesperUser();
		return result;
	}

	public Collection<Rendezvous> toptenbyRSVP() {
		final Map<Rendezvous, Integer> aux = new HashMap<>();
		final List<Rendezvous> auxr = this.rendezvousRepository.findAll();
		Collection<RSVP> auxrv = new ArrayList<RSVP>();
		for (final Rendezvous r : auxr) {
			auxrv = this.RSVPService.findbyRendezvous(r.getId());
			aux.put(r, auxrv.size());
		}

		final List<Rendezvous> a = new ArrayList<>(aux.keySet());
		final List<Integer> b = new ArrayList<>(aux.values());
		//reordeno ambas listas
		for (int i = 0; i < b.size(); i++)
			for (int j = i + 1; j < b.size(); j++)
				if (b.get(i) < b.get(j)) {
					final Integer ix = b.get(i);
					b.set(i, b.get(j));
					b.set(j, ix);
					final Rendezvous ir = a.get(i);
					a.set(i, a.get(j));
					a.set(j, ir);
				}
		final List<Rendezvous> res = new ArrayList<Rendezvous>();
		int n = 10;
		if (a.size() < 10)
			n = a.size();
		for (int i = 0; res.size() <= n - 1; i++)
			res.add(a.get(i));

		final Collection<Rendezvous> result = res;
		return result;
	}
	public Collection<Rendezvous> findRendezvousWithMoreAnnouncementsThanAverage() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findRendezvousWithMoreAnnouncementsThanAverage();

		return result;
	}

	public Collection<Rendezvous> findRendezvousWithMoreRendezvousesThanAverage() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findRendezvousWithMoreRendezvousesThanAverage();

		return result;
	}

	public Collection<Rendezvous> findAllFinal() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findAllFinal();
		Assert.notNull(result);

		return result;
	}

	public Collection<Rendezvous> findByService(final domain.Service service) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findByServiceId(service.getId());
		Assert.notNull(result);

		return result;
	}
	public Collection<Rendezvous> findByCategoryId(final int categoryId) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findByCategoryId(categoryId);
		Assert.notNull(result);

		return result;
	}
	public Collection<Rendezvous> findByCategoryName(final String categoryName) {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findByCategoryName(categoryName);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.rendezvousRepository.flush();
	}

	public Collection<Rendezvous> findByUserNotRequestedForService(final int userId, final int serviceId) {
		Collection<Rendezvous> rendezvouses;
		Collection<Rendezvous> result;
		Collection<Request> requests;

		requests = this.requestService.findByServiceIdandUser(serviceId, userId);
		rendezvouses = this.findByUser(userId);

		result = new HashSet<>();
		result.addAll(rendezvouses);
		for (final Request r : requests)
			result.remove(r.getRendezvous());

		return result;
	}
}
