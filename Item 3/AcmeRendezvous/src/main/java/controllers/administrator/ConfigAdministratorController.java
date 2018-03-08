
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigService;
import controllers.AbstractController;
import domain.Config;

@Controller
@RequestMapping("/config/administrator")
public class ConfigAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ConfigService			configService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public ConfigAdministratorController() {
		super();
	}

	// Edition ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Config config;

		config = this.configService.findConfiguration();
		result = this.createEditModelAndView(config);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Config config, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(config);
		else
			try {
				this.configService.save(config);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(config, "config.commit.error");
			}
		return result;
	}

	// Ancillary Methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Config config) {
		ModelAndView result;

		result = this.createEditModelAndView(config, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Config config, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("config/edit");
		result.addObject("config", config);
		result.addObject("message", messageCode);

		return result;
	}
}
