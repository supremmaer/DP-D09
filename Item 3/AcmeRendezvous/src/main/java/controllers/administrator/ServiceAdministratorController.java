
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;

@Controller
@RequestMapping("/service/administrator")
public class ServiceAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;


	// Constructors -----------------------------------------------------------

	public ServiceAdministratorController() {
		super();
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int serviceId) {
		ModelAndView result;

		this.serviceService.cancel(serviceId);
		result = new ModelAndView("redirect:/service/user/list.do");

		return result;
	}
}
