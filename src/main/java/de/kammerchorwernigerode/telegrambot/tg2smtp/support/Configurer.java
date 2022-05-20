package de.kammerchorwernigerode.telegrambot.tg2smtp.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;

/**
 * Defines callback methods to customize the Java-based configuration for this application.
 *
 * @author Vincent Nadoll
 */
public interface Configurer {

    default void addPrinters(PrinterRegistry registry) {
    }
}
