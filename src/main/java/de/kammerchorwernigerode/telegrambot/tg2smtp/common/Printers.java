package de.kammerchorwernigerode.telegrambot.tg2smtp.common;

import lombok.experimental.UtilityClass;
import org.springframework.format.Printer;

@UtilityClass
public class Printers {

    public static <T> Printer<T> emptyString() {
        return (source, locale) -> "";
    }
}
