package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

/**
 * VO-like that supplies a string message.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface Notification {

    String getMessage();
}
