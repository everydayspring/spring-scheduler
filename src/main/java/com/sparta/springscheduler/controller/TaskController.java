package com.sparta.springscheduler.controller;

import com.sparta.springscheduler.dto.TaskRequestDto;
import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/scheduler")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto requestDto) {
        return taskService.createTask(requestDto);
    }

    @GetMapping("/scheduler")
    public List<TaskResponseDto> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/scheduler/{id}")
    public TaskResponseDto getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/scheduler/search")
    public List<TaskResponseDto> searchTasks(
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) String name) {
        return taskService.searchTasks(updatedAt, name);
    }

    @PutMapping("/scheduler/{id}")
    public TaskResponseDto updateTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {
        return taskService.updateTask(id, requestDto);
    }

    @DeleteMapping("/scheduler/{id}")
    public void deleteTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {
        taskService.deleteTask(id, requestDto);
    }
}
