package de.kammerchorwernigerode.telegrambot.tg2smtp.spi;

import de.vinado.boot.secrets.PropertyIndexSupplier;
import de.vinado.boot.secrets.SecretsEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Reads Docker Secret files from the local system and passes their values to the configured Spring properties.
 *
 * @author Vincent Nadoll
 */
public class DockerSecretsEnvironmentPostProcessor extends SecretsEnvironmentPostProcessor {

    public DockerSecretsEnvironmentPostProcessor(DeferredLogFactory logFactory) {
        super(logFactory);
    }

    @Override
    protected PropertyIndexSupplier getPropertyIndexSupplier(ConfigurableEnvironment environment) {
        Map<String, String> properties = new HashMap<>();
        properties.put("spring.mail.username", "MAIL_USER_FILE");
        properties.put("spring.mail.password", "MAIL_PASSWORD_FILE");
        properties.put("tg2smtp.notification.to", "TG2SMTP_MAIL_TO_FILE");
        properties.put("tg2smtp.bot.chat-id", "TG2SMTP_CHAT_ID_FILE");
        properties.put("tg2smtp.bot.username", "TG2SMTP_BOT_USERNAME_FILE");
        properties.put("tg2smtp.bot.token", "TG2SMTP_BOT_TOKEN_FILE");

        return PropertyIndexSupplier.from(properties)
                .substituteValues(environment);
    }
}
