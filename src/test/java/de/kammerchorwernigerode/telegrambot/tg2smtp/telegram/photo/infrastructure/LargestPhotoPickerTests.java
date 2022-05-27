package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.app.PhotoPicker.UndecidableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
class LargestPhotoPickerTests {

    private LargestPhotoPicker picker;

    @BeforeEach
    void setUp() {
        picker = new LargestPhotoPicker();
    }

    @Test
    void pickingNullArgument_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> picker.pickFrom(null));
    }

    @Test
    void pickingFromEmpty_shouldTrowException() {
        assertThrows(UndecidableException.class, () -> picker.pickFrom(Collections.emptyList()));
    }

    @Test
    void pickingFomSingleton_shouldReturnSoleCandidate() {
        PhotoSize photo1 = mock(PhotoSize.class);
        when(photo1.getFileSize()).thenReturn(0);

        PhotoSize result = picker.pickFrom(Collections.singletonList(photo1));

        assertEquals(photo1, result);
    }

    @Test
    void pickingFromTwo_shouldReturnLargest() {
        PhotoSize photo1 = mock(PhotoSize.class);
        PhotoSize photo2 = mock(PhotoSize.class);
        when(photo1.getFileSize()).thenReturn(0);
        when(photo2.getFileSize()).thenReturn(1);

        PhotoSize result = picker.pickFrom(Arrays.asList(photo1, photo2));

        assertEquals(photo2, result);
    }

    @Test
    void pickingFromThree_shouldReturnLargest() {
        PhotoSize photo1 = mock(PhotoSize.class);
        PhotoSize photo2 = mock(PhotoSize.class);
        PhotoSize photo3 = mock(PhotoSize.class);
        when(photo1.getFileSize()).thenReturn(0);
        when(photo2.getFileSize()).thenReturn(1);
        when(photo3.getFileSize()).thenReturn(2);

        PhotoSize result = picker.pickFrom(Arrays.asList(photo1, photo2, photo3));

        assertEquals(photo3, result);
    }

    @Test
    void pickingFromThreeUnsorted_shouldReturnLargest() {
        PhotoSize photo1 = mock(PhotoSize.class);
        PhotoSize photo2 = mock(PhotoSize.class);
        PhotoSize photo3 = mock(PhotoSize.class);
        when(photo1.getFileSize()).thenReturn(42);
        when(photo2.getFileSize()).thenReturn(1337);
        when(photo3.getFileSize()).thenReturn(1);

        PhotoSize result = picker.pickFrom(Arrays.asList(photo1, photo2, photo3));

        assertEquals(photo2, result);
    }
}
