package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.email;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.Tg2SmtpNotificationProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.InternetAddress;
import java.util.Collections;
import java.util.Set;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notifications.just;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTests {

    private @Mock Tg2SmtpNotificationProperties botProperties;
    private @Mock JavaMailSender sender;
    private @Mock(answer = Answers.RETURNS_DEEP_STUBS) MailProperties mailProperties;
    private @Mock Renderer renderer;

    private EmailNotificationService service;

    @BeforeEach
    void setUp() {
        service = new EmailNotificationService(botProperties, sender, mailProperties, renderer);
    }

    @Test
    void sendingNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.send((Notification) null));
    }

    @Test
    @SneakyThrows
    void sendingToOneRecipient_shouldDelegateToSender() {
        when(botProperties.getTo()).thenReturn(Collections.singleton(new InternetAddress("foo@example.com")));

        service.send(just("foo"));

        verify(sender).send(new MimeMessagePreparator[]{any()});
    }

    @Test
    @SneakyThrows
    void sendingToMultipleRecipients_shouldDelegateToSender() {
        when(botProperties.getTo())
                .thenReturn(Set.of(new InternetAddress("foo@example.com"), new InternetAddress("bar@example.com")));

        service.send(just("foo"));

        verify(sender).send(new MimeMessagePreparator[]{any(), any()});
    }
}
