package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledAudio;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledVideo;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.TitledPhotos;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

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

    public Optional<Notification> translate(@NonNull Message message) {
        Metadata metadata = new Metadata(message.getDate(), message.getFrom());

        if (message.hasText()) {
            String text = message.getText();
            return createNotification(text, metadata);
        } else if (message.hasLocation()) {
            Location location = message.getLocation();
            return createNotification(location, metadata);
        } else if (message.hasPoll()) {
            Poll poll = message.getPoll();
            return createNotification(poll, metadata);
        } else if (message.hasPhoto()) {
            Photos candidates = new Photos(message.getPhoto());
            TitledPhotos photos = new TitledPhotos(message.getCaption(), candidates);
            return createNotification(photos, metadata);
        } else if (message.hasDocument()) {
            TitledDocument document = new TitledDocument(message.getCaption(), message.getDocument());
            return createNotification(document, metadata);
        } else if (message.hasAudio()) {
            TitledAudio audio = new TitledAudio(message.getCaption(), message.getAudio());
            return createNotification(audio, metadata);
        } else if (message.hasVoice()) {
            Voice voice = message.getVoice();
            return createNotification(voice, metadata);
        } else if (message.hasVideo()) {
            TitledVideo video = new TitledVideo(message.getCaption(), message.getVideo());
            return createNotification(video, metadata);
        } else if (message.hasVideoNote()) {
            VideoNote videoNote = message.getVideoNote();
            return createNotification(videoNote, metadata);
        } else if (message.hasSticker()) {
            Sticker sticker = message.getSticker();
            return createNotification(sticker, metadata);
        }

        log.warn("Missing translation for {}", message);
        return Optional.empty();
    }

    private <T> Optional<Notification> createNotification(T message, Metadata metadata) {
        return notificationFactoryProvider.findBy(message)
                .map(factory -> factory.create(message, metadata));
    }
}
