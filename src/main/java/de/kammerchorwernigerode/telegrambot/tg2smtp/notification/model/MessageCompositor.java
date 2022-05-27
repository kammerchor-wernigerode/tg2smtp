package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.NonNull;

import java.util.List;

@FunctionalInterface
public interface MessageCompositor {

    String compose(@NonNull List<Notification> notifications);
}
