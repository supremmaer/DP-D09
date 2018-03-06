
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Config;

@Component
@Transactional
public class ConfigToStringConverter implements Converter<Config, String> {

	@Override
	public String convert(final Config config) {
		String result;

		if (config == null)
			result = null;
		else
			result = String.valueOf(config.getId());

		return result;
	}

}
