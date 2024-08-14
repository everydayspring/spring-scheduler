package com.sparta.springscheduler.entity;

import com.sparta.springscheduler.dto.TaskRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private int id;
    private String content;
    private String name;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private String password;

    public Task(TaskRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
    }
}
