package com.techie.microservices.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final JdbcTemplate jdbcTemplate;
    private final String recipientEmail = "test@example.com"; // Замените на нужный email

    @Scheduled(fixedRate = 60000) // Отправка каждую минуту (60000 миллисекунд)
    public void sendAdvertisement() {
        log.info("Проверка флага рекламы в базе данных...");

        String selectSql = "SELECT flag FROM adv WHERE id = ?";
        Boolean flag = false;

        try {
            flag = jdbcTemplate.queryForObject(selectSql, new Object[]{1}, Boolean.class);
        } catch (DataAccessException e) {
            log.error("Ошибка при получении флага из базы данных", e);
            return;
        }

        if (Boolean.TRUE.equals(flag)) {
            log.info("Флаг установлен в true. Отправка рекламного email...");

            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("CatShop@email.com");
                messageHelper.setTo(recipientEmail);
                messageHelper.setSubject("Cat Shop Реклама");
                messageHelper.setText("""
                        Мяу!
                        
                        Мяу Мяу Мяу Мяу мяу мяу мяу
                        """);
            };

            try {
                javaMailSender.send(messagePreparator);
                log.info("Рекламный email успешно отправлен!");

                // Устанавливаем flag = false после отправки
                String updateSql = "UPDATE adv SET flag = ? WHERE id = ?";
                jdbcTemplate.update(updateSql, false, 1);
                log.info("Флаг рекламы установлен в false.");
            } catch (MailException e) {
                log.error("Ошибка при отправке рекламного email", e);
            } catch (DataAccessException e) {
                log.error("Ошибка при обновлении флага в базе данных", e);
            }
        } else {
            log.info("Флаг рекламы не установлен. Email не отправлен.");
        }
    }
}
