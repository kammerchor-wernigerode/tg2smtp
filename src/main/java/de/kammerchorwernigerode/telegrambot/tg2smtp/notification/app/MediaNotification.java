package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class MediaNotification implements Notification {

    private final Downloader<MediaReference> downloader;

    @Override
    public Stream<Resource> listAttachments() {
        return Stream.of(download());
    }

    @SneakyThrows
    private Resource download() {
        MediaReference mediaReference = create();
        return downloader.download(mediaReference);
    }

    protected abstract MediaReference create();
}
