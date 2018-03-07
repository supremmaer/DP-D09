
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ServiceRepository;

@Service
@Transactional
public class ServiceService {

	@Autowired
	private ServiceRepository	serviceRepository;


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

}
