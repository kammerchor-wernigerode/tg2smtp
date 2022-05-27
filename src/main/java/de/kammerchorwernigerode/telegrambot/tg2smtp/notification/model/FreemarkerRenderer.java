package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Locale;

@RequiredArgsConstructor
public class FreemarkerRenderer implements Renderer {

    @NonNull
    private Configuration configuration;

    @Override
    public String render(@NonNull String templateName, @Nullable Locale locale, @Nullable Object model) throws IOException {
        try {
            Template template = configuration.getTemplate(templateName, locale);
            String render = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return render.trim();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
