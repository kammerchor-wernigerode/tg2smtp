package de.kammerchorwernigerode.telegrambot.tg2smtp.test;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Tag("de.kammerchorwernigerode.telegrambot.tg2smtp.test.IntegrationTest")
public @interface IntegrationTest {
}
