
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Service extends DomainEntity {

	private String	name;
	private String	description;
	private String	picture;
	private boolean	cancelled;


	@NotBlank
	public String getName() {
		return this.name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	//Relationships

	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}


	private Category	category;
	private Manager		manager;


	@Valid
	@ManyToOne(optional = false)
	public Category getCategory() {
		return this.category;
	}
	public void setCategory(final Category category) {
		this.category = category;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}
