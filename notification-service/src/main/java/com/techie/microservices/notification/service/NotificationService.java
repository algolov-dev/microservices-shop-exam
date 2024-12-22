package com.techie.microservices.notification.service;

//import com.techie.microservices.notification.event.CatPlacedEvent;
import com.techie.microservices.cat.event.CatPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "cat-placed")
    public void listen(CatPlacedEvent catPlacedEvent) {
        log.info("Got Message from cat-placed topic {}", catPlacedEvent);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(catPlacedEvent.getOwnerEmail().toString());
            messageHelper.setSubject("Your cat from Spring Shop");
            messageHelper.setText(String.format("""
                    Заказ %s
                   
                    Пользователь %s хочет купить у тебя %s за %s
                   
                    Магазин котиков
                    """,
                    catPlacedEvent.getCatNumber(),
                    catPlacedEvent.getUserEmail(),
                    catPlacedEvent.getTitle(),
                    catPlacedEvent.getPrice()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Cat notification email send!");
        } catch( MailException e) {
            log.error("Exception occurred with sending mail", e);
            throw new RuntimeException(e);
        }
    }
}
