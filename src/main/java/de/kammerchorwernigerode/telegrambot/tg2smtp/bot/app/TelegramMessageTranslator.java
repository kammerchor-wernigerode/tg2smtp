package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledAudio;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledPhotos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledVideo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.Locale;
import java.util.Optional;

/**
 * Component that translates a Telegram {@link Message}'s content to a notification.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramMessageTranslator {

    private final @NonNull NotificationFactoryProvider notificationFactoryProvider;
    private final @NonNull LocaleResolver localeResolver;

    public Optional<Notification> translate(@NonNull Message message) {
        Locale locale = localeResolver.resolve(message);

        if (message.hasText()) {
            String text = message.getText();
            return createNotification(text, locale);
        } else if (message.hasLocation()) {
            Location location = message.getLocation();
            return createNotification(location, locale);
        } else if (message.hasPoll()) {
            Poll poll = message.getPoll();
            return createNotification(poll, locale);
        } else if (message.hasPhoto()) {
            Photos candidates = new Photos(message.getPhoto());
            TitledPhotos photos = new TitledPhotos(message.getCaption(), candidates);
            return createNotification(photos, locale);
        } else if (message.hasDocument()) {
            TitledDocument document = new TitledDocument(message.getCaption(), message.getDocument());
            return createNotification(document, locale);
        } else if (message.hasAudio()) {
            TitledAudio audio = new TitledAudio(message.getCaption(), message.getAudio());
            return createNotification(audio, locale);
        } else if (message.hasVoice()) {
            Voice voice = message.getVoice();
            return createNotification(voice, locale);
        } else if (message.hasVideo()) {
            TitledVideo video = new TitledVideo(message.getCaption(), message.getVideo());
            return createNotification(video, locale);
        }

        log.warn("Missing translation for {}", message);
        return Optional.empty();
    }

    private <T> Optional<Notification> createNotification(T message, Locale locale) {
        return notificationFactoryProvider.findBy(message)
                .map(factory -> factory.create(message, locale));
    }
}
