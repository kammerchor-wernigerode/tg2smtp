package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.mail.internet.InternetAddress;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties("tg2smtp.notification")
public class Tg2SmtpNotificationProperties {

    private String subject;
    private Set<InternetAddress> to = new HashSet<>();
}
