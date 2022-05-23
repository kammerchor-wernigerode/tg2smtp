package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.Tg2SmtpBotProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/**
 * A specialized {@link Downloader} for {@link PhotoSize Telegram photos}.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class PhotoDownloader implements Downloader<PhotoSize> {

    private final @NonNull Tg2SmtpBotProperties properties;
    private final @NonNull ThrowingFunction<GetFile, File, TelegramApiException> executor;

    @Override
    public Resource download(PhotoSize photo) throws IOException {
        try {
            String fileId = photo.getFileId();
            GetFile method = new GetFile(fileId);
            File reference = executor.apply(method);
            String botToken = properties.getBot().getToken();
            String path = reference.getFileUrl(botToken);
            return new UrlResource(path);
        } catch (TelegramApiException e) {
            throw new IOException("Download failed", e);
        }
    }
}
