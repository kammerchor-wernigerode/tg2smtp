package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.email;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Nadoll
 */
class MimeMessagePreparatorCompositeTests {

    private MimeMessagePreparatorComposite composite;

    @BeforeEach
    void setUp() {
        composite = new MimeMessagePreparatorComposite();
    }

    @Test
    void addingNullPreparator_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> composite.add(null));
    }

    @Test
    void preparingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> composite.prepare(null));
    }

    @Test
    @SneakyThrows
    void preparingMessage_shouldDelegate() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessagePreparator preparator = mock(MimeMessagePreparator.class);
        composite.add(preparator);

        composite.prepare(mimeMessage);

        verify(preparator).prepare(eq(mimeMessage));
    }

    @Test
    @SneakyThrows
    void preparingMessages_shouldDelegateInOrder() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessagePreparator preparator1 = mock(MimeMessagePreparator.class);
        MimeMessagePreparator preparator2 = mock(MimeMessagePreparator.class);
        composite.add(preparator1);
        composite.add(preparator2);

        composite.prepare(mimeMessage);

        InOrder inOrder = Mockito.inOrder(preparator1, preparator2);
        inOrder.verify(preparator1).prepare(eq(mimeMessage));
        inOrder.verify(preparator2).prepare(eq(mimeMessage));
    }
}
