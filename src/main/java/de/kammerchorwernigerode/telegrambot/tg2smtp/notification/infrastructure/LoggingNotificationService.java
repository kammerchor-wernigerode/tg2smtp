package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.SYNC;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * {@link NotificationService} that prints {@link Notification} to <em>stdout</em>.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@Service
@Profile("debug")
@RequiredArgsConstructor
public class LoggingNotificationService implements NotificationService, InitializingBean {

    private static final String OUTPUT_DIRECTORY = "data/downloads";

    private final Renderer renderer;

    @SneakyThrows
    @Override
    public void send(@NonNull Notification notification) {
        System.out.println(notification.getMessage(renderer));

        Iterator<Resource> attachments = notification.listAttachments().iterator();
        if (attachments.hasNext()) {
            System.out.println("-----");
            System.out.println("The attachment(s) were placed in the " + OUTPUT_DIRECTORY + " directory:");
        }

        while (attachments.hasNext()) {
            Resource attachment = attachments.next();
            Path path = Paths.get(OUTPUT_DIRECTORY, nameFrom(attachment));
            try (OutputStream outputStream = Files.newOutputStream(path, WRITE, CREATE, SYNC)) {
                attachment.getInputStream().transferTo(outputStream);
                System.out.println("  * " + path);
            }
        }
    }

    private String nameFrom(Resource attachment) {
        return Optional.ofNullable(attachment.getFilename())
                .orElseGet(UUID.randomUUID()::toString);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Files.createDirectories(Paths.get(OUTPUT_DIRECTORY));
    }
}
