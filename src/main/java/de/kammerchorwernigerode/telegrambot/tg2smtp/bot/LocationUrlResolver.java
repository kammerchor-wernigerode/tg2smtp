package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.net.URL;

/**
 * Depending on the service a {@link Location} has to be transformed to the service's compatible link.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface LocationUrlResolver {

    @org.springframework.lang.NonNull
    URL resolve(@NonNull Number latitude, @NonNull Number longitude);
}
