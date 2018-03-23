
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.service.id=?1")
	Collection<Request> findByServiceID(int id);

	@Query("select r from Request r where r.rendezvous.id=?1")
	Collection<Request> findByRendezvousID(int id);

	@Query("select r from Request r where r.creditCard.id=?1")
	Collection<Request> findByCreditCardId(int id);

	@Query("select r from Request r where r.service.id=?1 and r.rendezvous.user.id=?2")
	Collection<Request> findByServiceIdandUser(int serviceId, int userId);
}
