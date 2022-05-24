package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationUrlResolver;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.TextPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledVideo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer.nullSafe;

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
    public void addPrinters(PrinterRegistry registry) {
        registry.addPrinter(String.class, textPrinter());
        registry.addPrinter(Poll.class, pollPrinter());
        registry.addPrinter(Location.class, locationPrinter());
    }

    private Printer<String> textPrinter() {
        return nullSafe(new TextPrinter());
    }

    private Printer<Poll> pollPrinter() {
        return nullSafe(new PollPrinter());
    }

    private Printer<Location> locationPrinter() {
        return nullSafe(new LocationPrinter(locationUrlResolver));
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        registry.addNotificationFactory(String.class, textNotificationFactory());
        registry.addNotificationFactory(Location.class, locationNotificationFactory());
        registry.addNotificationFactory(Poll.class, pollNotificationFactory());
        registry.addNotificationFactory(Photos.class, photoNotificationFactory());
        registry.addNotificationFactory(Document.class, documentNotificationFactory());
        registry.addNotificationFactory(Audio.class, audioNotificationFactory());
        registry.addNotificationFactory(Voice.class, voiceNotificationFactory());
        registry.addNotificationFactory(TitledVideo.class, videoNotificationFactory());
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
