package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.location.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class GoogleMapsLocationUrlResolverTests {

    private GoogleMapsLocationUrlResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new GoogleMapsLocationUrlResolver();
    }

    @Test
    void resolvingNullLatitude_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(null, 0));
    }

    @Test
    void resolvingNullLongitude_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(0, null));
    }

    @Test
    void resolvingLocation_shouldResolveUrl() {
        URL resolve = resolver.resolve(13.37, 42.0);

        assertEquals("https://www.google.com/maps?q=13.37,42.0", resolve.toString());
    }
}
