package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NotificationComposite implements Notification {

    private final @NonNull List<Notification> delegates;
    private final @NonNull MessageCompositor compositor;

    @Override
    public String getMessage() {
        return compositor.compose(delegates);
    }

    @Override
    public Stream<Resource> listAttachments() {
        return delegates.stream().flatMap(Notification::listAttachments);
    }
}
