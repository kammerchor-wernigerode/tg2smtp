package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;

import java.util.List;

/**
 * {@link Printer} that formats Telegram {@link Poll}s into plaintext.
 *
 * @author Vincent Nadoll
 */
public class PollPrinter implements Printer<Poll> {

    @org.springframework.lang.NonNull
    @Override
    public String print(@NonNull Poll poll) {
        String question = poll.getQuestion();
        StringBuilder builder = new StringBuilder()
                .append(question)
                .append("\n\n");

        List<PollOption> options = poll.getOptions();
        for (int i = 0; i < options.size(); i++) {
            PollOption option = options.get(i);
            builder.append(i + 1)
                    .append(") ")
                    .append(option.getText())
                    .append("\n");
        }

        return builder.toString();
    }
}