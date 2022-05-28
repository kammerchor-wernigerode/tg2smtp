package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.Tg2SmtpBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * Configuration properties for the Tg2SMTP bot.
 *
 * @author Vincent Nadoll
 * @see Tg2SmtpBot
 */
@Getter
@Setter
@ConfigurationProperties("tg2smtp")
public class Tg2SmtpBotProperties {

    private Set<Long> chatId;
    private Bot bot = new Bot();


    @Getter
    @Setter
    public static class Bot {

        private String username;
        private String token;
    }
}
