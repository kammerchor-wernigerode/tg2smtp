package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class LocationNotificationFactoryTests {

    private LocationNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock LocationPrinter printer;

    @BeforeEach
    void setUp() {
        factory = new LocationNotificationFactory(configuration, printer);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new LocationNotificationFactory(null, printer));
        assertThrows(IllegalArgumentException.class, () -> new LocationNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Location location = mock(Location.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(location, null));
    }
}
