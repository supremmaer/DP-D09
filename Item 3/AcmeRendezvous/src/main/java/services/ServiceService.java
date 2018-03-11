
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ServiceRepository;
import domain.Actor;
import domain.Administrator;
import domain.Manager;
import domain.Rendezvous;
import domain.Request;

@Service
@Transactional
public class ServiceService {

	@Autowired
	private ServiceRepository	serviceRepository;

	@Autowired
	private ManagerService		managerService;
	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;


	//Constructors
	public ServiceService() {
		super();
	}

	public domain.Service create() {
		domain.Service result;

		result = new domain.Service();
		result.setCancelled(false);
		result.setManager(this.managerService.findByPrincipal());

		return result;
	}
	public Collection<domain.Service> findAll() {
		Collection<domain.Service> result;

		result = this.serviceRepository.findAll();

		return result;
	}

	public domain.Service findOne(final int serviceId) {
		domain.Service result;

		result = this.serviceRepository.findOne(serviceId);
		Assert.notNull(result);

		return result;
	}
	public Collection<domain.Service> findByManager() {
		Collection<domain.Service> result;
		Manager manager;

		manager = this.managerService.findByPrincipal();
		result = this.serviceRepository.findByManager(manager.getId());
		Assert.notNull(result);

		return result;
	}

	public domain.Service cancel(final int serviceId) {
		Actor actor;
		domain.Service result, service;

		actor = this.actorService.findByPrincipal();
		service = this.findOne(serviceId);
		Assert.notNull(service);
		Assert.isTrue(service.getId() != 0);
		Assert.isTrue(actor instanceof Administrator);

		service.setCancelled(true);
		result = this.serviceRepository.save(service);

		return result;
	}

	//DONE: Incluir Assert
	public void delete(final int id) {
		Collection<Rendezvous> rendezvouses;
		Manager principal;
		domain.Service service;

		service = this.serviceRepository.findOne(id);
		Assert.notNull(service);
		rendezvouses = this.rendezvousService.findByService(service);
		principal = this.managerService.findByPrincipal();
		Assert.isTrue(rendezvouses.isEmpty() && principal.getId() == service.getManager().getId());

		this.serviceRepository.delete(service);

	}
	//DONE: Incluir Assert
	public domain.Service save(final domain.Service service) {
		domain.Service result;
		Manager principal;

		principal = this.managerService.findByPrincipal();
		Assert.isTrue(principal.getId() == service.getManager().getId());
		result = this.serviceRepository.saveAndFlush(service);
		return result;
	}

	public Collection<domain.Service> findMostSellersOrder() {
		final Collection<domain.Service> result = this.serviceRepository.findMostSellersOrder();
		return result;
	}

	public Collection<domain.Service> findMostSellers() {
		final List<domain.Service> aux = new ArrayList<domain.Service>(this.findMostSellersOrder());
		final Collection<Request> auxr = new ArrayList<Request>();
		final Collection<domain.Service> result = new ArrayList<domain.Service>();
		result.add(aux.get(0));
		for (int i = 1; i < aux.size(); i++)
			if (this.requestService.findByServiceID(aux.get(0).getId()).size() != this.requestService.findByServiceID(aux.get(i).getId()).size())
				break;
			else
				result.add(aux.get(i));
		return result;
	}

	public Collection<domain.Service> findByCategoryId(final int categoryId) {
		Collection<domain.Service> result;

		result = this.serviceRepository.findByCategoryId(categoryId);

		return result;
	}

	public domain.Service reconstruct(final domain.Service service, final BindingResult binding) {
		domain.Service result;
		Manager principal;

		if (service.getId() == 0) {
			result = service;
			principal = this.managerService.findByPrincipal();
			result.setManager(principal);
		} else {

			result = this.findOne(service.getId());

			//			result.setDescription(service.getDescription());
			//			result.setName(service.getName());
			//			result.setCategory(service.getCategory());
			//			result.setPicture(service.getPicture());

			service.setCancelled(result.isCancelled());
			service.setManager(result.getManager());
			service.setVersion(result.getVersion());

		}
		this.validator.validate(service, binding);
		return service;
	}
}
