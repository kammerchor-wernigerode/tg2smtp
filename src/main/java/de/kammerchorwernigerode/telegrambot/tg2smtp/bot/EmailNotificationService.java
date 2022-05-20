package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
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

/**
 * {@link NotificationService} implementation that forwards {@link Notification}s using the SMTP protocol.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final Tg2SmtpBotProperties properties;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public void send(@NonNull Notification notification) {
        MimeMessagePreparator[] preparators = properties.getTo().stream()
                .map(emailAddress -> prepareMimeMessage(emailAddress, notification))
                .toArray(MimeMessagePreparator[]::new);

        mailSender.send(preparators);
    }

    private MimeMessagePreparator prepareMimeMessage(InternetAddress emailAddress, Notification notification) {
        return message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setTo(emailAddress);
            helper.setSubject(properties.getSubject());
            helper.setText(notification.getMessage());
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
