package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Locale;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@UtilityClass
final class Locales {

    public static Arguments[] configured() {
        return new Arguments[]{
                arguments(Locale.ENGLISH),
                arguments(Locale.GERMANY),
        };
    }
}
