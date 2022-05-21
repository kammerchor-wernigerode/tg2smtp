package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.net.URI;
import java.net.URL;

/**
 * Resolver that transforms information from Telegram {@link Location location} messages to a Google Maps URL.
 *
 * @author Vincent Nadoll
 */
@Component
public class GoogleMapsLocationUrlResolver implements LocationUrlResolver {

    private static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps";

    @SneakyThrows
    @org.springframework.lang.NonNull
    @Override
    public URL resolve(@NonNull Number latitude, @NonNull Number longitude) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_BASE_URL)
                .query("q={latitude},{longitude}")
                .buildAndExpand(latitude, longitude)
                .encode().toUri();
        return uri.toURL();
    }
}
