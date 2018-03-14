package com.crud.tasks.service;

import com.crud.tasks.trello.config.AdminConfig;
import com.crud.tasks.trello.config.CompanyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CompanyConfig companyConfig;

    public String buildTrelloCardEmail(String message) {
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button","Visit website");
        context.setVariable("admin_name", adminConfig);
        context.setVariable("company", companyConfig);
        context.setVariable("application_functionality",functionality);
        context.setVariable("goodbye_message", "Thank you for using our services");
        context.setVariable("show_button",false);
        context.setVariable("is_friend", true);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }

    public String buildInformationEmail(String message) {
        List<String> features = new ArrayList<>();
        features.add(System.getProperty("os.name"));
        features.add(System.getProperty("os.version"));
        features.add(System.getProperty("os.arch"));
        features.add(System.getProperty("java.version"));

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("date_time", LocalDateTime.now());
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button","Enter Crud application");
        context.setVariable("admin_name", adminConfig);
        context.setVariable("company", companyConfig);
        context.setVariable("features",features);
        context.setVariable("goodbye_message", "Thank you for using our services");
        context.setVariable("show_button",true);
        context.setVariable("is_friend", true);
        return templateEngine.process("mail/information-mail", context);
    }

}
