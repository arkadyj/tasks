package com.crud.tasks.scheduler;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import com.crud.tasks.trello.config.AdminConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminConfig adminConfig;

    private static final String SUBJECT = "Task once a day email";

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    //@Scheduled(cron = "0 0 10 * * *")
    @Scheduled(fixedDelay = 10000)
    public void sendInformationEmail() {
        LOGGER.info("Processing scheduled method");

        simpleEmailService.send(new Mail(
                adminConfig.getAdminMail(),
                null,
                SUBJECT,
                messageBuilder()
        ),"cron");
    }

    private String messageBuilder() {
        long size = taskRepository.count();
        return (size == 1) ? "Database contains " + size + " task " : "Database contains " + size + " tasks ";
    }
}
