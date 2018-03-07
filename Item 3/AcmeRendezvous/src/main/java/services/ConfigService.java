
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ConfigRepository;
import domain.Config;

@Service
@Transactional
public class ConfigService {

	@Autowired
	private ConfigRepository	configRepository;


	//Constructors
	public ConfigService() {
		super();
	}

	public Config create() {
		Config result;

		result = new Config();

		return result;
	}

	public Collection<Config> findAll() {
		Collection<Config> result;

		result = this.configRepository.findAll();

		return result;
	}
	//TODO: Incluir Assert
	public void delete(final Config config) {
		this.configRepository.delete(config);

	}
	//TODO: Incluir Assert
	public Config save(final Config config) {
		Config result;
		result = this.configRepository.save(config);
		return result;
	}

	public Config findOne(final int configId) {
		Config result;

		result = this.configRepository.findOne(configId);
		Assert.notNull(result);

		return result;
	}

}
