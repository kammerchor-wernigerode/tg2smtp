package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Builder pattern implementation for configuring and creating new Freemarker {@link Template}s.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class TemplateBuilder {

    @NonNull
    private final String templateName;

    private Locale locale;
    private Charset encoding = StandardCharsets.UTF_8;

    public TemplateBuilder locale(@NonNull Locale locale) {
        this.locale = locale;
        return this;
    }

    public TemplateBuilder encoding(@NonNull Charset encoding) {
        this.encoding = encoding;
        return this;
    }

    public Template build(@NonNull Configuration configuration) throws IOException {
        return configuration.getTemplate(templateName, locale, encoding.name());
    }
}
