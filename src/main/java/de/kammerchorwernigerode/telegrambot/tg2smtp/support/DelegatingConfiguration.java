package de.kammerchorwernigerode.telegrambot.tg2smtp.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * A subclass of {@link ConfigurationSupport} that detects and delegates to all beans of type {@link Configurer}
 * allowing them to customize the configuration provided by {@link ConfigurationSupport}.
 *
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
public class DelegatingConfiguration extends ConfigurationSupport {

    private final ConfigurerComposite configurers = new ConfigurerComposite();

    @Autowired(required = false)
    public void setConfigurers(List<Configurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addConfigurers(configurers);
        }
    }

    @Override
    protected void addPrinters(PrinterRegistry registry) {
        configurers.addPrinters(registry);
    }
}
