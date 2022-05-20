package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;

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
        return locationUrlResolver.resolve(location).toString();
    }
}
