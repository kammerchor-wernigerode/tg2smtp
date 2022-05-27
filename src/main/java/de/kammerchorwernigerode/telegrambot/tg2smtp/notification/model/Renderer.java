package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Locale;

public interface Renderer {

    String render(@NonNull String templateName, @Nullable Locale locale, @Nullable Object model) throws IOException;
}
