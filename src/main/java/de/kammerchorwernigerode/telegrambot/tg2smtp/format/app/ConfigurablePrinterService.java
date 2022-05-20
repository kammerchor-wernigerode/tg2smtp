package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * {@link PrinterService} implementation that loads printers from its encapsulated registry.
 *
 * @author Vincent Nadoll
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConfigurablePrinterService implements PrinterService, PrinterRegistry {

    private final Map<Class, Printer> registry = new LinkedHashMap<>();

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
        return registry.entrySet().stream()
                .filter(byKey(isAssignableFrom(type)))
                .map(Entry::getValue)
                .findFirst()
                .orElseGet(StaticPrinter::new);
    }

    private static <K> Predicate<Entry<K, ?>> byKey(Predicate<K> subject) {
        return entry -> subject.test(entry.getKey());
    }

    private static Predicate<Class> isAssignableFrom(Class type) {
        return self -> self.isAssignableFrom(type);
    }


    /**
     * {@link Printer} implementation always printing {@link ""};
     *
     * @author Vincent Nadoll
     */
    private static final class StaticPrinter implements Printer<String> {

        @org.springframework.lang.NonNull
        @Override
        public String print(@Nullable String object) {
            return "";
        }
    }
}
