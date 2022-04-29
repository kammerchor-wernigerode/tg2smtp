package de.kammerchorwernigerode.telegram.spi;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Configurer} that delegates to one or more others.
 *
 * @author Vincent Nadoll
 */
class ConfigurerComposite implements Configurer {

    private final List<Configurer> delegates = new ArrayList<>();

    public void addConfigurers(List<Configurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }
}
