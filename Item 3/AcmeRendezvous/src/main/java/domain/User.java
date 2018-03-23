
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDate;
import org.joda.time.Years;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	//Relationships
	private Collection<Rendezvous>	rendezvouses;


	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<Rendezvous> getRendezvouses() {
		return this.rendezvouses;
	}

	public void setRendezvouses(final Collection<Rendezvous> rendezvouses) {
		this.rendezvouses = rendezvouses;
	}

	@Transient
	public boolean isAdult() {
		final LocalDate birth = new LocalDate(this.getBirthDate());
		final LocalDate now = new LocalDate();
		final Years age = Years.yearsBetween(birth, now);

		return (age.getYears() >= 18);
	}

}
