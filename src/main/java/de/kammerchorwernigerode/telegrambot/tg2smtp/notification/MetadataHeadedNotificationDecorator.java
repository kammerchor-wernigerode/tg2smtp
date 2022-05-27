package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.format.Printer;

import java.io.IOException;
import java.util.stream.Stream;

public class MetadataHeadedNotificationDecorator extends MetadataHeadedNotification {

    private final Notification subject;

    public MetadataHeadedNotificationDecorator(@NonNull Metadata metadata,
                                               @NonNull Printer<Metadata> metadataPrinter,
                                               @NonNull Notification subject) {
        super(metadata, metadataPrinter);
        this.subject = subject;
    }

    @Override
    public String getMessage(@NonNull Renderer renderer) throws IOException {
        return subject.getMessage(renderer);
    }

    @Override
    public Stream<Resource> listAttachments() {
        return subject.listAttachments();
    }
}
