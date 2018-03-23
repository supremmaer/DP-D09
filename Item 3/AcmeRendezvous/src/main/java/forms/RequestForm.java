
package forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import domain.Rendezvous;

public class RequestForm {

	private String			comment;
	private Rendezvous		rendezvous;
	private domain.Service	service;
	private String			holderName;
	private String			brandName;
	private String			number;
	private int				expirationMonth;
	private int				expirationYear;
	private int				CVV;


	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@NotNull
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}

	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

	@NotNull
	public domain.Service getService() {
		return this.service;
	}

	public void setService(final domain.Service service) {
		this.service = service;
	}

	@NotBlank
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	@NotBlank
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(final String brandName) {
		this.brandName = brandName;
	}

	@CreditCardNumber
	@NotBlank
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public int getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	public int getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final int expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 100, max = 999)
	public int getCVV() {
		return this.CVV;
	}

	public void setCVV(final int cvv) {
		this.CVV = cvv;
	}

}
