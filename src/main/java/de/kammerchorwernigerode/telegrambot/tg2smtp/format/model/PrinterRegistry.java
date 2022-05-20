package de.kammerchorwernigerode.telegrambot.tg2smtp.format.model;

import lombok.NonNull;

/**
 * Registers {@link Printer}-implementations.
 *
 * @author Vincent Nadoll
 */
public interface PrinterRegistry {

    <T> void addPrinter(@NonNull Class<T> type, @NonNull Printer<? extends T> printer);
}
