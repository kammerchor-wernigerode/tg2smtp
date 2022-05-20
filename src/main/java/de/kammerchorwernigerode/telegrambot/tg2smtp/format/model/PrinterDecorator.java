package de.kammerchorwernigerode.telegrambot.tg2smtp.format.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Abstract decorator that delegates method calls to the given subject.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public abstract class PrinterDecorator<T> implements Printer<T> {

    @Getter
    @NonNull
    @Delegate
    private final Printer<T> subject;
}
