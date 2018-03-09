
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ServiceRepository;
import domain.Request;

@Service
@Transactional
public class ServiceService {

	@Autowired
	private ServiceRepository	serviceRepository;

	@Autowired
	private RequestService		requestService;


	//Constructors
	public ServiceService() {
		super();
	}

	public domain.Service create() {
		domain.Service result;

		result = new domain.Service();

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

	//TODO: Incluir Assert
	public void delete(final domain.Service s) {
		this.serviceRepository.delete(s);

	}
	//TODO: Incluir Assert
	public domain.Service save(final domain.Service s) {
		domain.Service result;
		result = this.serviceRepository.save(s);
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

}
