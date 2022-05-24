package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

/**
 * {@link PhotoPicker} that picks the photo with the largest file size.
 *
 * @author Vincent Nadoll
 */
@Component
public class LargestPhotoPicker implements PhotoPicker {

    @Override
    public PhotoSize pickFrom(@NonNull List<PhotoSize> candidates) throws UndecidableException {
        return candidates.stream()
                .reduce(LargestPhotoPicker::pickGreatest)
                .orElseThrow(() -> new UndecidableException("No candidates to compare"));
    }

    private static PhotoSize pickGreatest(PhotoSize first, PhotoSize second) {
        return first.getFileSize() > second.getFileSize()
                ? first
                : second;
    }
}
