package com.testTask.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "requests_pay")
public class RequestPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String number;

    @NotNull
    private Date date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}