package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class SimpleEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailCreatorService mailCreatorService;

    public void send(final Mail mail, String mailType) {

        LOGGER.info("Preparing email");
        try {
            //SimpleMailMessage mailMessage = createMailMessage(mail);
            //javaMailSender.send(mailMessage);
            javaMailSender.send(createMimeMessage(mail, mailType));
            LOGGER.info("Mail has been sent.");
        } catch (MailException e) {
            LOGGER.error("Failed to process email sending: ", e.getMessage(),e);
        }
    }

    private SimpleMailMessage createMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        Optional.ofNullable(mail.getToCc()).ifPresent(email -> mailMessage.setCc(email));
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        return mailMessage;
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail, String mailType) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            if (mailType.equals("card")) {
                messageHelper.setText(mailCreatorService.buildTrelloCardEmail(mail.getMessage()),true);
            } else if (mailType.equals("cron")) {
                messageHelper.setText(mailCreatorService.buildInformationEmail(mail.getMessage()),true);
            }
        };
    }
}
