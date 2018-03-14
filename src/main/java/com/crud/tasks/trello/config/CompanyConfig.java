package com.crud.tasks.trello.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CompanyConfig {

    @Value("${info.company.name}")
    private String companyName;

    @Value("${info.company.email}")
    private String companyMail;

    @Value("${info.company.phone}")
    private String companyPhone;


}
