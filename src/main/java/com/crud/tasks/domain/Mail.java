package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mail {

    private String mailTo;
    private String toCc;
    private String subject;
    private String message;
}
