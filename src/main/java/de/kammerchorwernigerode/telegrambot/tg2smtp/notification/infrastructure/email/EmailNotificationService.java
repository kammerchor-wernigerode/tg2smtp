package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.email;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.Tg2SmtpNotificationProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.util.function.UnaryOperator;

/**
 * {@link NotificationService} implementation that forwards {@link Notification}s using the SMTP protocol.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final Tg2SmtpNotificationProperties properties;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final Renderer renderer;

    @Override
    public void send(@NonNull Notification notification) {
        MimeMessagePreparator[] preparators = properties.getTo().stream()
                .map(this::prepareMimeMessage)
                .map(this::compose)
                .map(add(notification))
                .toArray(MimeMessagePreparator[]::new);

        mailSender.send(preparators);
    }

    private MimeMessagePreparatorComposite compose(MimeMessagePreparator preparator) {
        MimeMessagePreparatorComposite composite = new MimeMessagePreparatorComposite();
        composite.add(preparator);
        return composite;
    }

    private UnaryOperator<MimeMessagePreparatorComposite> add(Notification notification) {
        return preparator -> {
            MimeMessagePreparatorAdapter adapter = new MimeMessagePreparatorAdapter(notification, renderer);
            preparator.add(adapter);
            return preparator;
        };
    }

    private MimeMessagePreparator prepareMimeMessage(InternetAddress emailAddress) {
        return message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setTo(emailAddress);
            helper.setSubject(properties.getSubject());
            applyReplyTo(helper);
            message.setFrom();
        };
    }

    private void applyReplyTo(MimeMessageHelper helper) throws MessagingException {
        String value = mailProperties.getProperties().get("mail.reply.to");
        if (StringUtils.hasText(value)) {
            helper.setReplyTo(value);
        }
    }
}
