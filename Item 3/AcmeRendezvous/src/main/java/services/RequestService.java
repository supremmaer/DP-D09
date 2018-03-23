
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.CreditCard;
import domain.Request;
import domain.User;
import forms.RequestForm;

@Service
@Transactional
public class RequestService {

	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private UserService			userService;
	@Autowired
	private CreditCardService	creditCardService;


	//Constructors
	public RequestService() {
		super();
	}

	public Request create() {
		Request result;

		result = new Request();

		return result;
	}

	public RequestForm createForm(final domain.Service service) {
		RequestForm result;

		result = new RequestForm();
		result.setService(service);

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
	public Collection<Request> findByRendezvousID(final int id) {
		return this.requestRepository.findByRendezvousID(id);

	}

	public Request save(final RequestForm requestForm) {
		final User user;
		final Request request;
		final Request result;
		final CreditCard creditCard;
		final CreditCard savedCreditCard;

		user = this.userService.findByPrincipal();
		Assert.isTrue(requestForm.getRendezvous().getUser().equals(user));
		Assert.isTrue(!requestForm.getService().isCancelled());

		creditCard = this.creditCardService.create();
		creditCard.setHolderName(requestForm.getHolderName());
		creditCard.setBrandName(requestForm.getBrandName());
		creditCard.setNumber(requestForm.getNumber());
		creditCard.setExpirationMonth(requestForm.getExpirationMonth());
		creditCard.setExpirationYear(requestForm.getExpirationYear());
		creditCard.setCVV(requestForm.getCVV());

		savedCreditCard = this.creditCardService.save(creditCard);
		Assert.notNull(savedCreditCard);

		request = this.create();
		request.setService(requestForm.getService());
		request.setRendezvous(requestForm.getRendezvous());
		request.setComment(requestForm.getComment());
		request.setCreditCard(savedCreditCard);

		result = this.save(request);

		return result;
	}

	public void flush() {

		this.requestRepository.flush();
	}

}
