package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import lombok.NonNull;

/**
 * {@link Printer} that formats Telegram messages into plaintext.
 *
 * @author Vincent Nadoll
 */
public class TextPrinter implements Printer<String> {

    @org.springframework.lang.NonNull
    @Override
    public String print(@NonNull String text) {
        return text;
    }
}
