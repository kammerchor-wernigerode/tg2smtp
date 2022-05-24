package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    private final @Nullable T model;

    public FreemarkerNotification(@NonNull TemplateBuilder templateBuilder, @NonNull Configuration configuration,
                                  @NonNull Printer<T> printer) {
        this.templateBuilder = templateBuilder;
        this.configuration = configuration;
        this.printer = printer;
        this.model = null;
    }

    private final List<Resource> attachments = new ArrayList<>();

    public FreemarkerNotification<T> with(@NonNull Resource attachment) {
        attachments.add(attachment);
        return this;
    }

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

    @Override
    public Stream<Resource> listAttachments() {
        return attachments.stream();
    }
}
