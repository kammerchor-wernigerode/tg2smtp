package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Locale;

@FunctionalInterface
public interface Renderer {

    String render(@NonNull String resourceKey, @Nullable Locale locale, @Nullable Object model) throws IOException;
}
