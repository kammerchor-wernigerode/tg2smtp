package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.mail.internet.InternetAddress;
import java.util.Set;

/**
 * Configuration properties for the SMTP forwarder bot.
 *
 * @author Vincent Nadoll
 * @see Tg2SmtpBot
 */
@Getter
@Setter
@ConfigurationProperties("bot.smtp")
public class Tg2SmtpBotProperties {

    private String username;
    private String token;
    private Set<Long> chatId;
    private Set<InternetAddress> to;
    private String mimeSubject;
}
