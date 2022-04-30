package de.kammerchorwernigerode.telegrambot.tg2smtp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@link SpringBootApplication Spring Boot application}-class that is the entrypoint into this application.
 *
 * @author Vincent Nadoll
 */
@SpringBootApplication
public class Tg2SmtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tg2SmtpApplication.class, args);
    }
}
