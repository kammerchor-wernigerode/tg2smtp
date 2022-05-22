package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

/**
 * Function component that extracts a preferred locale from a Telegram {@link Message}.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface LocaleResolver {

    Locale resolve(@NonNull Message message);
}
