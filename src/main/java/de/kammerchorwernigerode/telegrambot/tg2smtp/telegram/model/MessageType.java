package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.audio.model.TitledAudio;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.document.model.TitledDocument;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.video.model.TitledVideo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

/**
 * Enumeration of all compatible message types.
 *
 * @author Vincent Nadoll
 */
@Getter
@RequiredArgsConstructor
public enum MessageType {
    ALL(Object.class),
    TEXT(String.class),
    LOCATION(Location.class),
    POLL(Poll.class),
    PHOTO(Photos.class),
    DOCUMENT(TitledDocument.class),
    AUDIO(TitledAudio.class),
    VOICE(Voice.class),
    VIDEO(TitledVideo.class),
    VIDEO_NOTE(VideoNote.class),
    STICKER(Sticker.class),
    ;

    private final Class<?> telegramType;
}
