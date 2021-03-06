
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.CreditCard;
import domain.Request;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	private CreditCardRepository	creditCardRepository;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private UserService				userService;


	//Constructors
	public CreditCardService() {
		super();
	}

	public CreditCard create() {
		CreditCard result;

		result = new CreditCard();

		return result;
	}

	public Collection<CreditCard> findAll() {
		Collection<CreditCard> result;

		result = this.creditCardRepository.findAll();

		return result;
	}

	public void delete(final CreditCard creditCard) {
		this.creditCardRepository.delete(creditCard);

	}

	@SuppressWarnings("deprecation")
	public CreditCard save(final CreditCard creditCard) {
		CreditCard result;
		final Date dateActual = new Date();
		if (dateActual.getYear() + 1900 == creditCard.getExpirationYear())
			Assert.isTrue(dateActual.getMonth() + 1 <= creditCard.getExpirationMonth());
		Assert.isTrue(dateActual.getYear() + 1900 <= creditCard.getExpirationYear());
		result = this.creditCardRepository.save(creditCard);
		return result;
	}

	public CreditCard findOne(final int creditCardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(result);

		final Collection<Request> requests = this.requestService.findByCreditCard(result);

		for (final Request r : requests)
			Assert.isTrue(r.getRendezvous().getUser().equals(this.userService.findByPrincipal()));

		return result;
	}
	public void flush() {
		this.creditCardRepository.flush();
	}

}
