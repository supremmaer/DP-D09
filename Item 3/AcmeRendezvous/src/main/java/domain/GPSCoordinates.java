
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class GPSCoordinates extends DomainEntity {

	private double	latitude;
	private double	longitude;


	@NotNull
	public double getLatitude() {
		return this.latitude;
	}
	@NotNull
	public double getLongitude() {
		return this.longitude;
	}
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	//Relationships

}
