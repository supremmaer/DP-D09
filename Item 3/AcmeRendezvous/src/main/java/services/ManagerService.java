
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Manager;
import forms.ActorForm;

@Service
@Transactional
public class ManagerService {

	@Autowired
	private ManagerRepository	managerRepository;

	@Autowired
	private ServiceService		serviceService;


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

	public Manager save(final Manager manager) {
		Manager result;
		final String vatNumber = manager.getVatNumber();
		Assert.notNull(vatNumber);

		Assert.isTrue(this.PatronOk(vatNumber));

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
	
	public boolean ComprobadorDeVatNumber(final ActorForm actorForm) {
		boolean result =true;
		if(actorForm.getAuthority().equals("MANAGER")){
			result= PatronOk(actorForm.getVatNumber());
		}
		return result;
	}

	
	public boolean PatronOk(final String string) {
		boolean isok = true;
		boolean anteriorFueGuion = false;
		for (int i = 0; i <= string.length() - 1; i++) {
			
			final char a = string.charAt(i);
			if (!(Character.isAlphabetic(a) || Character.isDigit(a) || a == '-')||(string.length()<4)||(string.length()>30)){
				isok = false;
				break;
			}
			if((i==0)&&(a=='-')){
				isok = false;
				break;	
			}
			if((i==string.length()-1)&&(a=='-')){
				isok = false;
				break;
			}
			if(a=='-'){
				if(anteriorFueGuion==true){
					isok = false;
					break;
				}
				if(anteriorFueGuion==false)
					anteriorFueGuion=true;
			}else{
					anteriorFueGuion=false;
				
			}
				
		}
		return isok;
	}

	public Double avgServicePerManager() {
		final Double result = this.managerRepository.avgServicePerManager();
		return result;
	}

	public Collection<Manager> managersWithMoreServicesThanAVG() {
		final Double avg = this.avgServicePerManager();
		final Collection<Manager> result = this.managerRepository.managersWithMoreServicesThanAVG(avg);
		return result;
	}

	public Collection<Manager> managersMoreCancelledServicesOrder() {
		final Collection<Manager> result = this.managerRepository.managersMoreCancelledServicesOrder();
		return result;
	}

	public Collection<Manager> managersMoreCancelledServices() {
		final List<Manager> aux = new ArrayList<Manager>(this.managersMoreCancelledServicesOrder());
		final Collection<Manager> result = new ArrayList<Manager>();
		result.add(aux.get(0));
		for (int i = 1; i < aux.size(); i++)
			if (this.serviceService.findByManagerAndCancelledTrue(aux.get(0).getId()).size() != this.serviceService.findByManagerAndCancelledTrue(aux.get(i).getId()).size())
				break;
			else
				result.add(aux.get(i));
		return result;
	}
}
