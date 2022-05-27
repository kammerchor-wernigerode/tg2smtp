package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
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
import java.util.stream.Stream;

/**
 * A specialized notification that resolves its messages by processing a Freemarker template.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@RequiredArgsConstructor
public class FreemarkerNotification implements Notification {

    private final @NonNull TemplateBuilder templateBuilder;
    private final @NonNull Configuration configuration;
    private final HashMap<String, Object> model = new HashMap<>();

    private final List<Resource> attachments = new ArrayList<>();

    public FreemarkerNotification with(@NonNull Resource attachment) {
        attachments.add(attachment);
        return this;
    }

    public FreemarkerNotification with(@NonNull String key, @Nullable Object value) {
        model.put(key, value);
        return this;
    }

    @Override
    public String getMessage() {
        try {
            Template template = templateBuilder.build(configuration);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            log.error("Unable to process email template", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMessage(@NonNull Renderer renderer) throws IOException {
        Template template = templateBuilder.build(configuration);
        return renderer.render(template.getName(), template.getLocale(), model);
    }

    @Override
    public Stream<Resource> listAttachments() {
        return attachments.stream();
    }
}
