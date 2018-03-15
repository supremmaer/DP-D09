
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.parent=null")
	Collection<Category> findRoots();

	@Query("select c from Category c where c.parent.id=?1")
	Collection<Category> findChilds(int categoryId);

	@Query("select s.category from Request r join r.service s where r.rendezvous.id=?1")
	Collection<Category> findByRendezvousID(int id);
}
