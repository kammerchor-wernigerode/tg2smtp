package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.infrastructure;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.format.Printer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AuthorPrinter implements Printer<User> {

    @NonNull
    private final MessageSource messageSource;

    @Override
    public String print(@Nullable User user, @NonNull Locale locale) {
        if (null == user) {
            return messageSource.getMessage("message.author.name.null-value", null, "Moderator", locale);
        }

        if (null == user.getLastName()) {
            return user.getFirstName();
        } else {
            return user.getFirstName() + " " + user.getLastName();
        }
    }
}
