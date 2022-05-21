package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.NonNull;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link MimeMessagePreparator} that delegates behavior to one or more other.
 *
 * @author Vincent Nadoll
 */
public class MimeMessagePreparatorComposite implements MimeMessagePreparator {

    private final List<MimeMessagePreparator> delegates = new ArrayList<>();

    public void add(@NonNull MimeMessagePreparator preparator) {
        delegates.add(preparator);
    }

    @Override
    public void prepare(@NonNull MimeMessage mimeMessage) throws Exception {
        for (MimeMessagePreparator preparator : delegates) {
            preparator.prepare(mimeMessage);
        }
    }
}

