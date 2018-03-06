
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Manager;

@Service
@Transactional
public class ManagerService {

	@Autowired
	private ManagerRepository	managerRepository;


	//Constructors
	public ManagerService() {
		super();
	}

	public Manager create() {
		Manager result;

		result = new Manager();

		return result;
	}

	public Collection<Manager> findAll() {
		Collection<Manager> result;

		result = this.managerRepository.findAll();

		return result;
	}

	public Manager findOne(final int managerId) {
		Manager result;

		result = this.managerRepository.findOne(managerId);
		Assert.notNull(result);

		return result;
	}
	//TODO: Incluir Assert
	public void delete(final Manager manager) {
		this.managerRepository.delete(manager);

	}
	//TODO: Incluir Assert
	public Manager save(final Manager manager) {
		Manager result;
		result = this.managerRepository.save(manager);
		return result;
	}

	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		return result;
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		Manager result;
		result = this.managerRepository.findByUserAccount(userAccount.getId());

		return result;
	}

}
