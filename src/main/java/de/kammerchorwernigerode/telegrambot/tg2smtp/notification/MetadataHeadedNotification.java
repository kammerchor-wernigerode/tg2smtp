package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Printer;
import org.springframework.lang.Nullable;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class MetadataHeadedNotification implements Notification {

    private final @NonNull Metadata metadata;
    private final @NonNull Printer<Metadata> metadataPrinter;

    @Override
    public Optional<String> getSubject(@Nullable Renderer renderer) {
        String print = metadataPrinter.print(metadata, metadata.getLocale());
        return Optional.of(print);
    }
}
