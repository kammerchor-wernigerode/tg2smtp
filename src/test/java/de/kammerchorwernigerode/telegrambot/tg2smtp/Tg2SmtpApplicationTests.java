package de.kammerchorwernigerode.telegrambot.tg2smtp;

import de.kammerchorwernigerode.telegrambot.tg2smtp.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@IntegrationTest
@SpringBootTest({"telegrambots.enabled=false"})
class Tg2SmtpApplicationTests {

    @Test
    void startingApplication_shouldNotFail() {
    }
}
