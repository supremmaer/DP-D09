
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Request;

@Service
@Transactional
public class RequestService {

	@Autowired
	private RequestRepository	requestRepository;


	//Constructors
	public RequestService() {
		super();
	}

	public Request create() {
		Request result;

		result = new Request();

		return result;
	}

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();

		return result;
	}

	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);

		return result;
	}

	//TODO: Incluir Assert
	public void delete(final Request request) {
		this.requestRepository.delete(request);

	}
	//TODO: Incluir Assert
	public Request save(final Request request) {
		Request result;
		result = this.requestRepository.save(request);
		return result;
	}

	public Collection<Request> findByServiceID(final int id) {
		final Collection<Request> result = this.requestRepository.findByServiceID(id);
		return result;
	}

}
