package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

@RequiredArgsConstructor
public class FreemarkerRenderer implements Renderer {

    private final @NonNull Configuration configuration;
    private final @NonNull PrinterService printerService;

    @Override
    public String render(@NonNull String resourceKey, @Nullable Locale locale, @Nullable Object model) throws IOException {
        try {
            HashMap<String, Object> formattingModel = new HashMap<>();
            formattingModel.put("printer", printerService);
            formattingModel.put("root", model);

            Template template = configuration.getTemplate(resourceKey, locale);
            String render = FreeMarkerTemplateUtils.processTemplateIntoString(template, formattingModel);
            return render.trim();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
