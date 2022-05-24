package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationUrlResolver;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * SPI-{@link Configurer} implementation that adapts the Telegram Bot API for then Tg2SMTP Bot domain.
 *
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class Tg2SmtpConfiguration implements Configurer {

    private final LocationUrlResolver locationUrlResolver;
    private final freemarker.template.Configuration configuration;
    private final PhotoPicker photoPicker;
    private final Downloader<MediaReference> mediaDownloader;

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        registry.addNotificationFactory(textNotificationFactory());
        registry.addNotificationFactory(locationNotificationFactory());
        registry.addNotificationFactory(pollNotificationFactory());
        registry.addNotificationFactory(photoNotificationFactory());
        registry.addNotificationFactory(documentNotificationFactory());
        registry.addNotificationFactory(audioNotificationFactory());
        registry.addNotificationFactory(voiceNotificationFactory());
        registry.addNotificationFactory(videoNotificationFactory());
    }

    private TextNotificationFactory textNotificationFactory() {
        return new TextNotificationFactory();
    }

    private LocationNotificationFactory locationNotificationFactory() {
        return new LocationNotificationFactory(configuration, new LocationPrinter(locationUrlResolver));
    }

    private PollNotificationFactory pollNotificationFactory() {
        return new PollNotificationFactory(configuration, new PollPrinter());
    }

    private PhotoNotificationFactory photoNotificationFactory() {
        return new PhotoNotificationFactory(configuration, photoPicker, mediaDownloader);
    }

    private DocumentNotificationFactory documentNotificationFactory() {
        return new DocumentNotificationFactory(configuration, mediaDownloader);
    }

    private AudioNotificationFactory audioNotificationFactory() {
        return new AudioNotificationFactory(configuration, mediaDownloader);
    }

    private VoiceNotificationFactory voiceNotificationFactory() {
        return new VoiceNotificationFactory(configuration, mediaDownloader);
    }

    private VideoNotificationFactory videoNotificationFactory() {
        return new VideoNotificationFactory(configuration, mediaDownloader);
    }
}
