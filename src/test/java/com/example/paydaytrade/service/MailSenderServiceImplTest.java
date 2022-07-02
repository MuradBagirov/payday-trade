package com.example.paydaytrade.service;

import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.impl.MailServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSenderServiceImplTest {

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor messageCaptor;

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService = new MailServiceImpl(javaMailSender,userRepository);
    }

    @Test
    public void testSendEmail() throws Exception {
        mailService.sendWithoutAttachment("xeyal.aqayev1998@gmail.com", "cavansir.asad@gmail.com", "testContent");
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        verify(message);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("xeyal.aqayev1998@gmail.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }
}
