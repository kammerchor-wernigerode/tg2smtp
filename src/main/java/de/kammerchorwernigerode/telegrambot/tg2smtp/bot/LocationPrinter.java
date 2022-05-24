package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.Locale;

/**
 * {@link Printer} implementation to transforms {@link Location}s to string.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class LocationPrinter implements Printer<Location> {

    private final LocationUrlResolver locationUrlResolver;

    @org.springframework.lang.NonNull
    @Override
    public String print(@NonNull Location location, @NonNull Locale locale) {
        return locationUrlResolver.resolve(location.getLatitude(), location.getLongitude()).toString();
    }
}
