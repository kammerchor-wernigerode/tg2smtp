package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumSet;

@Getter
@Setter
@ConfigurationProperties("tg2smtp.messages")
public class Tg2SmtpMessageTypeProperties {

    private EnumSet<MessageType> active = EnumSet.allOf(MessageType.class);
}
