package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link PrinterService} implementation that loads printers from its encapsulated registry.
 *
 * @author Vincent Nadoll
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConfigurablePrinterService implements PrinterService, PrinterRegistry {

    private final Map<Class, Printer> registry = new HashMap<>();

    @Override
    public <T> void addPrinter(@NonNull Class<T> type, @NonNull Printer<? extends T> printer) {
        registry.put(type, printer);
    }

    @org.springframework.lang.NonNull
    @Override
    public <T> String print(@NonNull T object) {
        Class<?> type = object.getClass();
        return findBy(type).print(object);
    }

    @org.springframework.lang.NonNull
    @Override
    public <T> String print(@NonNull T object, @NonNull Locale locale) {
        Class<?> type = object.getClass();
        return findBy(type).print(object, locale);
    }

    private Printer findBy(Class<?> type) {
        return registry.getOrDefault(type, obj -> "");
    }
}
