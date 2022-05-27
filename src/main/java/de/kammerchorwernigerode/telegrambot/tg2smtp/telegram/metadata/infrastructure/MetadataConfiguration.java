package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Printer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Configuration(proxyBeanMethods = false)
public class MetadataConfiguration {

    @Primary
    @Bean
    public Printer<Instant> instantPrinter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        return new TimestampPrinter(formatter);
    }
}
