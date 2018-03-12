
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccount(int id);

	@Query("select 1.0*count(s)/(select count(m) from Manager m) from Service s")
	Double avgServicePerManager();

	@Query("select s.manager from Service s group by s.manager having count(s)>?1*1.0 order by count(s) DESC")
	Collection<Manager> managersWithMoreServicesThanAVG(Double avg);

	@Query("select s.manager from Service s where s.cancelled=true group by s.manager order by count(s) DESC")
	Collection<Manager> managersMoreCancelledServicesOrder();

}
