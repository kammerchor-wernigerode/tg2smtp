package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * Adapter pattern implementation that translates an arbitrary {@link Notification}. MIME headers won't be altered.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class MimeMessagePreparatorAdapter implements MimeMessagePreparator {

    @NonNull
    private final Notification notification;

    @Override
    public void prepare(@NonNull MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
        helper.setText(notification.getMessage());
    }
}
