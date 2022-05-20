package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Nadoll
 */
class PrinterServiceTests {

    @Test
    void localizedPrinting_shouldDefault() {
        PrinterService service = spy(new StaticPrinterService("foo"));

        service.print("any", Locale.getDefault());

        verify(service).print(eq("any"));
    }

    @Getter
    @RequiredArgsConstructor
    private static class StaticPrinterService implements PrinterService {

        private final String value;

        @org.springframework.lang.NonNull
        @Override
        public String print(@NonNull Object object) {
            return value;
        }
    }
}
