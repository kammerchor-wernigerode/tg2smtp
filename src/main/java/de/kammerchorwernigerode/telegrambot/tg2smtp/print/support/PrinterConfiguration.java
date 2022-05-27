package de.kammerchorwernigerode.telegrambot.tg2smtp.print.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Printer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class PrinterConfiguration {

    private final ListableBeanFactory beanFactory;

    @Bean
    public PrinterService printerService() {
        ConfigurablePrinterService service = new ConfigurablePrinterService();
        beanFactory.getBeansOfType(Printer.class).values().forEach(service::addPrinter);
        return service;
    }
}
