
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("select s from Service s where s.manager.id=?1")
	Collection<Service> findByManager(int id);

	@Query("select r.service from Request r group by r.service order by count(r) DESC")
	Collection<Service> findMostSellersOrder();

	@Query("select s from Service s where s.category.id=?1")
	Collection<Service> findByCategoryId(int categoryId);

	@Query("select s.id from Service s where s.manager.id=?1 and s.cancelled=true")
	Collection<Service> findByManagerAndCancelledTrue(int id);

	@Query("select 1.0*count(s)/(select count(c) from Category c) from Service s")
	Double avgServicesPerCategory();

	@Query("select r.service from Request r where r.rendezvous.id=?1")
	Collection<Service> findByRendezvousID(int id);
}
