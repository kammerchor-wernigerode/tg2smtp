package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

@UtilityClass
public final class Metadatas {

    public static Metadata createDefault() {
        return new Metadata(0, new User(1L, "John", false, "Doe", "johndoe", "en", null, null, null));
    }

    public static Metadata createDefault(Locale locale) {
        return new Metadata(0, new User(1L, "John", false, "Doe", "johndoe", locale.getLanguage(), null, null, null));
    }
}
