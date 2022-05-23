package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import com.fasterxml.uuid.impl.NameBasedGenerator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.uuid.UUIDType.NAME_BASED_SHA1;
import static com.fasterxml.uuid.impl.NameBasedGenerator.NAMESPACE_URL;
import static org.apache.commons.codec.digest.DigestUtils.getSha1Digest;

/**
 * Adapter pattern implementation that translates an arbitrary {@link Notification}. MIME headers won't be altered.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class MimeMessagePreparatorAdapter implements MimeMessagePreparator {

    @NonNull
    private final Notification notification;

    @Override
    public void prepare(@NonNull MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, hasAttachments(), StandardCharsets.UTF_8.name());
        helper.setText(notification.getMessage());

        Iterator<Resource> attachments = notification.listAttachments().iterator();
        while (attachments.hasNext()) {
            Resource attachment = attachments.next();
            String attachmentFilename = getFilename(attachment)
                    .orElse(translate(attachment.getURL()).toString());

            helper.addAttachment(attachmentFilename, attachment);
        }
    }

    private boolean hasAttachments() {
        return notification.listAttachments().findAny().isPresent();
    }

    private Optional<String> getFilename(Resource attachment) {
        return Optional.ofNullable(attachment.getFilename());
    }

    private UUID translate(URL url) {
        String name = url.toString();
        return uuidV5From(name);
    }

    private static UUID uuidV5From(String name) {
        return generator().generate(name);
    }

    private static NameBasedGenerator generator() {
        return new NameBasedGenerator(NAMESPACE_URL, getSha1Digest(), NAME_BASED_SHA1);
    }
}
