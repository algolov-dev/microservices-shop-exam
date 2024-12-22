package com.techie.microservices.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertService {
    private final JavaMailSender javaMailSender;
    private final String recipientEmail = "your_email@example.com"; // Замените на нужный email

    @Scheduled(fixedRate = 30000) // Отправка каждую минуту (60000 миллисекунд)
    public void sendAdvertisement() {
        log.info("Sending advertisement email...");
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject("Spring Shop Advertisement");
            messageHelper.setText("""
                    Привет!
                    
                    Посети наш магазин Spring Shop и найди своего идеального котика!
                    
                    У нас большой выбор пушистых друзей на любой вкус.
                    
                    Не упусти свой шанс!
                    
                    С уважением,
                    Команда Spring Shop
                    """);
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Advertisement email sent successfully!");
        } catch (MailException e) {
            log.error("Exception occurred while sending advertisement email", e);
        }
    }
}