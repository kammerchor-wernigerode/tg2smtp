package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public String print(@NonNull Location location) {
        return locationUrlResolver.resolve(location.getLatitude(), location.getLongitude()).toString();
    }

    @Override
    public String print(@NonNull Location object, @NonNull Locale locale) {
        return Printer.super.print(object, locale);
    }
}
