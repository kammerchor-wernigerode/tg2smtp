package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class MetadataHeadedNotification implements Notification {

    private final @NonNull Metadata metadata;

    @Override
    public Optional<String> getSubject(@NonNull Renderer renderer) throws IOException {
        HashMap<String, Object> model = new HashMap<>();
        model.put("metadata", metadata);

        String render = renderer.render("subject", Locale.getDefault(), model);
        return Optional.of(render);
    }
}
