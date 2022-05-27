package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class LocationNotificationFactoryTests {

    private LocationNotificationFactory factory;

    private @Mock Configuration configuration;

    @BeforeEach
    void setUp() {
        factory = new LocationNotificationFactory(configuration);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new LocationNotificationFactory(null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Location location = mock(Location.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(location, null));
    }
}
