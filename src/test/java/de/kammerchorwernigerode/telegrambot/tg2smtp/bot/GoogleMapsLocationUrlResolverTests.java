package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
    void resolvingNullLocation_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(null));
    }

    @Test
    void resolvingLocation_shouldResolveUrl() {
        Location location = spy(new Location());
        when(location.getLatitude()).thenReturn(13.37);
        when(location.getLongitude()).thenReturn(42.0);

        URL resolve = resolver.resolve(location);

        assertEquals("https://www.google.com/maps?q=13.37,42.0", resolve.toString());
    }
}
