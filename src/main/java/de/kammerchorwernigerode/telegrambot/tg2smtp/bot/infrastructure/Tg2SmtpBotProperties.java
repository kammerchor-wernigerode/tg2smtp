package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.Tg2SmtpBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * Configuration properties for the Tg2SMTP bot.
 *
 * @author Vincent Nadoll
 * @see Tg2SmtpBot
 */
@Getter
@Setter
@ConfigurationProperties("tg2smtp.bot")
public class Tg2SmtpBotProperties {

    private Set<Long> chatId;
    private String username;
    private String token;
}
