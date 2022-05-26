package de.kammerchorwernigerode.telegrambot.tg2smtp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest({"telegrambots.enabled=false"})
class Tg2SmtpApplicationTests {

    @Test
    void startingApplication_shouldNotFail() {
    }
}
