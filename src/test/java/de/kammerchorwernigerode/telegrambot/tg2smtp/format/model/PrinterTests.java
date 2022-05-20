package de.kammerchorwernigerode.telegrambot.tg2smtp.format.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class PrinterTests {

    @Mock
    private Printer<Object> subject;

    @Test
    void decoratingNullPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Printer.nullSafe(null));
    }

    @Test
    void decoratingPrinter_shouldWrapSubject() {
        NullSafePrinterDecorator<Object> decorator = (NullSafePrinterDecorator<Object>) Printer.nullSafe(subject);

        assertEquals(subject.getClass(), decorator.getSubject().getClass());
    }
}
