package de.kammerchorwernigerode.telegrambot.tg2smtp.print.app;

import lombok.NonNull;

import java.util.Locale;

public interface PrinterService {

    String print(@NonNull Object object, @NonNull Locale locale);
}
