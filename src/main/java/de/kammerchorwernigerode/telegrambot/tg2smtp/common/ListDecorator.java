package de.kammerchorwernigerode.telegrambot.tg2smtp.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.List;

/**
 * Simple decorator pattern implementation of {@link List} that allows to reference collections w/o generics.
 *
 * @param <T> the type of list elements
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public abstract class ListDecorator<T> implements List<T> {

    @Getter
    @NonNull
    @Delegate
    private final List<T> subject;
}
