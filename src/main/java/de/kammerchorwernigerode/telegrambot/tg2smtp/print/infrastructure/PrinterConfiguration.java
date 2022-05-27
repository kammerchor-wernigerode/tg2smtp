package de.kammerchorwernigerode.telegrambot.tg2smtp.print.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.spi.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Printer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class PrinterConfiguration implements Configurer {

    private final ListableBeanFactory beanFactory;

    @Override
    public void addPrinters(PrinterRegistry registry) {
        beanFactory.getBeansOfType(Printer.class).values().forEach(registry::addPrinter);
    }
}
