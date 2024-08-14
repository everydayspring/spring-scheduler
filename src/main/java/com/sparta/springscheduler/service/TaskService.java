package com.sparta.springscheduler.service;

import com.sparta.springscheduler.dto.TaskRequestDto;
import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.entity.Task;
import com.sparta.springscheduler.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = new Task(requestDto);
        int id = taskRepository.save(task);
        task.setId(id);

        return new TaskResponseDto(task);
    }

    public List<TaskResponseDto> getTasks() {
        return taskRepository.findAll();
    }

    public TaskResponseDto getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public List<TaskResponseDto> searchTasks(String updatedAt, String name) {
        return taskRepository.findByCondition(updatedAt, name);
    }

    public TaskResponseDto updateTask(int id, TaskRequestDto requestDto) {
        String storedPassword = taskRepository.findPasswordById(id);
        if (storedPassword == null || !storedPassword.equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        taskRepository.update(id, requestDto.getName(), requestDto.getContent(), new Date());
        return taskRepository.findById(id);
    }

    public void deleteTask(int id, TaskRequestDto requestDto) {
        String storedPassword = taskRepository.findPasswordById(id);
        if (storedPassword == null || !storedPassword.equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        taskRepository.delete(id);
    }
}