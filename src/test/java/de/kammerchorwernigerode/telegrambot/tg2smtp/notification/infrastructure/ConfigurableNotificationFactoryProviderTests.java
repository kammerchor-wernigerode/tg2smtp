package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.MessageType;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.Photos;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notifications.just;
import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.MessageType.ALL;
import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.MessageType.LOCATION;
import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.MessageType.TEXT;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class ConfigurableNotificationFactoryProviderTests {

    private ConfigurableNotificationFactoryProvider provider;

    @Mock
    private EnumSet<MessageType> active;

    @BeforeEach
    void setUp() {
        provider = new ConfigurableNotificationFactoryProvider(active);
    }

    @Test
    void initializingNullMessageTypes_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new ConfigurableNotificationFactoryProvider(null));
    }

    @Test
    void addingNullFactory_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.addNotificationFactory(null));
    }

    @Test
    void findingByNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.findBy(null));
    }

    @Test
    void findingUnregistered_shouldReturnEmpty() {
        Optional<NotificationFactory<Object>> factory = provider.findBy(new Object());

        assertFalse(factory.isPresent());
    }

    @Test
    void findingRegistered_shouldReturnFactory() {
        NotificationFactory<Object> factory = new TestNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.of(ALL));
        provider.addNotificationFactory(factory);

        NotificationFactory<Object> found = provider.findBy(new Object()).get();

        assertEquals(factory, found);
    }

    @Test
    void configuredLocation_shouldNotFindStringFactory() {
        NotificationFactory<String> factory = new StringNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.of(LOCATION));
        provider.addNotificationFactory(factory);

        Optional<NotificationFactory<String>> registered = provider.findBy("");

        assertFalse(registered.isPresent());
    }

    @Test
    void configuredNone_shouldNotFindStringFactory() {
        NotificationFactory<String> factory = new StringNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.empty());
        provider.addNotificationFactory(factory);

        Optional<NotificationFactory<String>> stringNotificationFactory = provider.findBy("");

        assertFalse(stringNotificationFactory.isPresent());
    }

    @Test
    void configuredNone_shouldNotFindLocationFactory() {
        NotificationFactory<String> factory = new StringNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.empty());
        provider.addNotificationFactory(factory);

        Optional<NotificationFactory<Location>> stringNotificationFactory = provider.findBy(new Location());

        assertFalse(stringNotificationFactory.isPresent());
    }

    @Test
    void configuredNone_shouldNotFindPhotosFactory() {
        NotificationFactory<String> factory = new StringNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.empty());
        provider.addNotificationFactory(factory);

        Optional<NotificationFactory<Photos>> stringNotificationFactory = provider.findBy(new Photos(emptyList()));

        assertFalse(stringNotificationFactory.isPresent());
    }

    @Test
    void configuredNone_shouldNotFindStickerFactory() {
        NotificationFactory<String> factory = new StringNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.empty());
        provider.addNotificationFactory(factory);

        Optional<NotificationFactory<Sticker>> stringNotificationFactory = provider.findBy(new Sticker());

        assertFalse(stringNotificationFactory.isPresent());
    }

    // And so on


    @Test
    void configuredTextAndLocation_shouldFindBoth() {
        NotificationFactory<String> stringFactory = new StringNotificationFactory();
        NotificationFactory<Location> locationFactory = new LocationNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.of(TEXT, LOCATION));
        provider.addNotificationFactory(stringFactory);
        provider.addNotificationFactory(locationFactory);

        assertTrue(provider.findBy("").isPresent());
        assertTrue(provider.findBy(new Location()).isPresent());
    }

    @Test
    void configuredLocation_shouldNotFindStickerFactory() {
        NotificationFactory<Location> locationFactory = new LocationNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.of(LOCATION));
        provider.addNotificationFactory(locationFactory);

        assertFalse(provider.findBy(new Sticker()).isPresent());
    }

    @Test
    void configuredAll_shouldFindAny() {
        NotificationFactory<String> textFactory = new StringNotificationFactory();
        NotificationFactory<Location> locationFactory = new LocationNotificationFactory();
        NotificationFactory<Object> objectFactory = new TestNotificationFactory();
        when(active.stream()).thenAnswer(i -> Stream.of(ALL));
        provider.addNotificationFactory(textFactory);
        provider.addNotificationFactory(locationFactory);
        provider.addNotificationFactory(objectFactory);

        assertTrue(provider.findBy("").isPresent());
        assertTrue(provider.findBy(new Object()).isPresent());
    }


    private static final class TestNotificationFactory implements NotificationFactory<Object> {

        @Override
        public Notification create(@NonNull Object message, @NonNull Metadata metadata) {
            return just("");
        }
    }

    private static final class StringNotificationFactory implements NotificationFactory<String> {

        @Override
        public Notification create(@NonNull String message, @NonNull Metadata metadata) {
            return just(message);
        }
    }

    private static final class LocationNotificationFactory implements NotificationFactory<Location> {

        @Override
        public Notification create(@NonNull Location location, @NonNull Metadata metadata) {
            return just(location.toString());
        }
    }
}
