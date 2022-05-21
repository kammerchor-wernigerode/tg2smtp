package de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet;
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet;
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb;
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
class AuthorizedLongPollingBotTests {

    private static final long CHAT_ID = 0;
    private static final Predicate<Update> FILTER = update -> CHAT_ID == update.getMessage().getChatId();

    private LongPollingBot subject;
    private AuthorizedLongPollingBot bot;

    private Update update;

    @BeforeEach
    void setUp() {
        subject = mock(LongPollingBot.class);
        bot = new AuthorizedLongPollingBot(subject, FILTER, new NoopSender());

        update = mock(Update.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Test
    void receiveInvalidUpdate_shouldDiscardUpdate() {
        when(update.getMessage().getChatId()).thenReturn(1L);

        bot.onUpdateReceived(update);

        verify(subject, never()).onUpdateReceived(update);
    }

    @Test
    void receivingValidUpdate_shouldDelegateToSubject() {
        when(update.getMessage().getChatId()).thenReturn(CHAT_ID);

        bot.onUpdateReceived(update);

        verify(subject).onUpdateReceived(eq(update));
    }

    @Test
    void receiveInvalidUpdates_shouldDelegateEmptyList() {
        Update update1 = mock(Update.class, Answers.RETURNS_DEEP_STUBS);

        when(update.getMessage().getChatId()).thenReturn(1L);
        when(update1.getMessage().getChatId()).thenReturn(2L);

        bot.onUpdatesReceived(Arrays.asList(update, update1));

        verify(subject).onUpdatesReceived(eq(Collections.emptyList()));
    }

    @Test
    void receivePartialValidUpdates_shouldDelegateToSubject() {
        Update update1 = mock(Update.class, Answers.RETURNS_DEEP_STUBS);

        when(update.getMessage().getChatId()).thenReturn(CHAT_ID);
        when(update1.getMessage().getChatId()).thenReturn(2L);

        bot.onUpdatesReceived(Arrays.asList(update, update1));

        verify(subject).onUpdatesReceived(eq(List.of(update)));
    }

    @Test
    void receiveValidUpdates_shouldDelegateToSubject() {
        Update update1 = mock(Update.class, Answers.RETURNS_DEEP_STUBS);
        List<Update> updates = Arrays.asList(update, update1);

        when(update.getMessage().getChatId()).thenReturn(CHAT_ID);
        when(update1.getMessage().getChatId()).thenReturn(CHAT_ID);

        bot.onUpdatesReceived(updates);

        verify(subject).onUpdatesReceived(eq(updates));
    }


    private static final class NoopSender extends AbsSender {

        @Override
        public Message execute(SendDocument sendDocument) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendPhoto sendPhoto) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendVideo sendVideo) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendVideoNote sendVideoNote) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendSticker sendSticker) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendAudio sendAudio) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendVoice sendVoice) throws TelegramApiException {
            return null;
        }

        @Override
        public List<Message> execute(SendMediaGroup sendMediaGroup) throws TelegramApiException {
            return null;
        }

        @Override
        public Boolean execute(SetChatPhoto setChatPhoto) throws TelegramApiException {
            return null;
        }

        @Override
        public Boolean execute(AddStickerToSet addStickerToSet) throws TelegramApiException {
            return null;
        }

        @Override
        public Boolean execute(SetStickerSetThumb setStickerSetThumb) throws TelegramApiException {
            return null;
        }

        @Override
        public Boolean execute(CreateNewStickerSet createNewStickerSet) throws TelegramApiException {
            return null;
        }

        @Override
        public File execute(UploadStickerFile uploadStickerFile) throws TelegramApiException {
            return null;
        }

        @Override
        public Serializable execute(EditMessageMedia editMessageMedia) throws TelegramApiException {
            return null;
        }

        @Override
        public Message execute(SendAnimation sendAnimation) throws TelegramApiException {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendDocument sendDocument) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendPhoto sendPhoto) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendVideo sendVideo) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendVideoNote sendVideoNote) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendSticker sendSticker) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendAudio sendAudio) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendVoice sendVoice) {
            return null;
        }

        @Override
        public CompletableFuture<List<Message>> executeAsync(SendMediaGroup sendMediaGroup) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> executeAsync(SetChatPhoto setChatPhoto) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> executeAsync(AddStickerToSet addStickerToSet) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> executeAsync(SetStickerSetThumb setStickerSetThumb) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> executeAsync(CreateNewStickerSet createNewStickerSet) {
            return null;
        }

        @Override
        public CompletableFuture<File> executeAsync(UploadStickerFile uploadStickerFile) {
            return null;
        }

        @Override
        public CompletableFuture<Serializable> executeAsync(EditMessageMedia editMessageMedia) {
            return null;
        }

        @Override
        public CompletableFuture<Message> executeAsync(SendAnimation sendAnimation) {
            return null;
        }

        @Override
        protected <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>> void sendApiMethodAsync(Method method, Callback callback) {

        }

        @Override
        protected <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> sendApiMethodAsync(Method method) {
            return null;
        }

        @Override
        protected <T extends Serializable, Method extends BotApiMethod<T>> T sendApiMethod(Method method) throws TelegramApiException {
            return null;
        }
    }
}
