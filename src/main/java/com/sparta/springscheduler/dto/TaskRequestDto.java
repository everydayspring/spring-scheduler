package com.sparta.springscheduler.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class TaskRequestDto {
    private int id;
    private String content;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private String password;
}
