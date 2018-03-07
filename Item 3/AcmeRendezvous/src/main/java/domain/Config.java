
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Config extends DomainEntity {

	private String	banner;
	private String	bussinessName;
	private String	welcomeEnglish;
	private String	welcomeSpanish;


	@URL
	@NotBlank
	public String getBanner() {
		return this.banner;
	}
	@NotBlank
	public String getBussinessName() {
		return this.bussinessName;
	}
	@NotBlank
	public String getWelcomeEnglish() {
		return this.welcomeEnglish;
	}
	@NotBlank
	public String getWelcomeSpanish() {
		return this.welcomeSpanish;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setBussinessName(final String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public void setWelcomeEnglish(final String welcomeEnglish) {
		this.welcomeEnglish = welcomeEnglish;
	}

	public void setWelcomeSpanish(final String welcomeSpanish) {
		this.welcomeSpanish = welcomeSpanish;
	}

}
