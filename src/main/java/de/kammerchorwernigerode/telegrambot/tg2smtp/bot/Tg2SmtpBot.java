package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Forwards Telegram messages as MIME messages via SMTP.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
public class Tg2SmtpBot extends TelegramLongPollingBot {

    private final Tg2SmtpBotProperties properties;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = extractMessage(update);
        if (null == message) {
            if (log.isDebugEnabled()) log.debug("Message is empty, skipping this update={}", update);
            return;
        }

        Long chatId = message.getChatId();
        if (log.isTraceEnabled()) log.trace("Received message from chat w/ chatId={}", chatId);
        if (!properties.getChatId().contains(chatId)) {
            if (log.isDebugEnabled()) log.debug("Chat not authorized");
            String text = "Your chat is not authorized to use this bot. "
                    + "Please remove " + properties.getUsername() + ".";
            SendMessage errorMessage = new SendMessage(chatId.toString(), text);
            execute(errorMessage);
            return;
        }

        String text = message.getText();
        if (!StringUtils.hasText(text)) {
            if (log.isDebugEnabled()) log.debug("Text is empty, noting to do");
            return;
        }

        MimeMessagePreparator[] preparators = properties.getTo().stream()
                .map(emailAddress -> prepareMimeMessage(emailAddress, text))
                .toArray(MimeMessagePreparator[]::new);

        mailSender.send(preparators);
    }

    @Nullable
    private Message extractMessage(Update update) {
        return Optional.ofNullable(update.getMessage())
                .or(() -> Optional.ofNullable(update.getChannelPost()))
                .orElse(null);
    }

    private MimeMessagePreparator prepareMimeMessage(InternetAddress emailAddress, String text) {
        return message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setTo(emailAddress);
            helper.setSubject(properties.getMimeSubject());
            helper.setText(text);
            applyReplyTo(helper);
            message.setFrom();
        };
    }

    private void applyReplyTo(MimeMessageHelper helper) throws MessagingException {
        String value = mailProperties.getProperties().get("mail.reply-to");
        if (StringUtils.hasText(value)) {
            helper.setReplyTo(value);
        }
    }
}
