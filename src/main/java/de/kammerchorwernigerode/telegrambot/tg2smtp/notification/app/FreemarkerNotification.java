package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A specialized notification that resolves its messages by processing a Freemarker template.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@RequiredArgsConstructor
public class FreemarkerNotification<T> implements Notification {

    private final @NonNull TemplateBuilder templateBuilder;
    private final @NonNull Configuration configuration;
    private final @NonNull Printer<T> printer;
    private final @NonNull T model;

    @Override
    public String getMessage() {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("printer", printer);
            model.put("model", this.model);

            Template template = templateBuilder.build(configuration);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            log.error("Unable to process email template", e);
            throw new RuntimeException(e);
        }
    }
}
