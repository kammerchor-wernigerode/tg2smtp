package de.kammerchorwernigerode.telegrambot.tg2smtp.print.app;

import lombok.NonNull;
import org.springframework.format.Printer;

public interface PrinterRegistry {

    void addPrinter(@NonNull Printer<?> printer);
}
