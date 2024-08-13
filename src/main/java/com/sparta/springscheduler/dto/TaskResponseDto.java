package com.sparta.springscheduler.dto;

import com.sparta.springscheduler.entity.Task;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class TaskResponseDto {
    private int id;
    private String content;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private String password;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.content = task.getContent();
        this.name = task.getName();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.password = task.getPassword();
    }

    public TaskResponseDto(int id, String name, String content, String createdAt, String updatedAt) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            this.id = id;
            this.name = name;
            this.content = content;
            this.createdAt = format.parse(createdAt);
            this.updatedAt = format.parse(updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
