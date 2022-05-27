package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * A function that downloads any resource.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface Downloader<T> {

    Resource download(T reference) throws IOException;
}
