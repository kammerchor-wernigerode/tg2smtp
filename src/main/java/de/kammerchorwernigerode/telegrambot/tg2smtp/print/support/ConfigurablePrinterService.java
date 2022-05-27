package de.kammerchorwernigerode.telegrambot.tg2smtp.print.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import lombok.NonNull;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.format.Printer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.springframework.core.convert.TypeDescriptor.forObject;
import static org.springframework.core.convert.TypeDescriptor.valueOf;

@SuppressWarnings("rawtypes,unchecked")
public class ConfigurablePrinterService implements PrinterRegistry, PrinterService {

    private final Map<Class<?>, Printer> registry = new HashMap<>();

    @Override
    public void addPrinter(@NonNull Printer<?> printer) {
        Class<?> fieldType = getFieldType(printer, Printer.class);
        registry.put(fieldType, printer);
    }

    /**
     * Shamelessly stolen from
     * {@link org.springframework.format.support.FormattingConversionService#getFieldType(Object, Class)}
     */
    private static <T> Class<?> getFieldType(T instance, Class<T> genericInterface) {
        Class<?> fieldType = GenericTypeResolver.resolveTypeArgument(instance.getClass(), genericInterface);
        if (fieldType == null && instance instanceof DecoratingProxy) {
            fieldType = GenericTypeResolver.resolveTypeArgument(
                    ((DecoratingProxy) instance).getDecoratedClass(), genericInterface);
        }
        Assert.notNull(fieldType, () -> "Unable to extract the parameterized field type from " +
                ClassUtils.getShortName(genericInterface) + " [" + instance.getClass().getName() +
                "]; does the class parameterize the <T> generic type?");
        return fieldType;
    }

    @Override
    public String print(@NonNull Object object, @NonNull Locale locale) {
        return getPrinter(object)
                .map(printer -> printer.print(object, locale))
                .orElseThrow(() -> new ConverterNotFoundException(forObject(object), valueOf(String.class)));
    }

    private Optional<Printer> getPrinter(Object object) {
        Class<?> fieldType = object.getClass();
        return Optional.ofNullable(registry.get(fieldType));
    }
}
