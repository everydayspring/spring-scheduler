package com.sparta.springscheduler.controller;

import com.sparta.springscheduler.dto.TaskRequestDto;
import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller : Client가 보낸 HTTP요청을 처리, 필요한 Service를 호출
@RestController
@RequestMapping("/api")
public class TaskController {

    // Service 객체
    private final TaskService taskService;

    // Service 객체 초기화
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 일정 등록
    // POST http://localhost:8080/api/scheduler
    /* body-JSON
        {
        "name":"관리자명",
        "content":"일정 내용",
        "password":"비밀번호"
        }
    */
    @PostMapping("/scheduler")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto requestDto) {
        return taskService.createTask(requestDto);
    }

    // 선택한 일정 조회
    // GET http://localhost:8080/api/scheduler/1
    @GetMapping("/scheduler/{id}")
    public TaskResponseDto getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    // 일정 목록 조회
    // GET http://localhost:8080/api/scheduler/search?upadatedAt=2024-08-14&name=관리자명
    @GetMapping("/scheduler")
    public List<TaskResponseDto> searchTasks(
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) String name) {
        return taskService.searchTasks(updatedAt, name);
    }

    // 선택한 일정 수정
    // PUT http://localhost:8080/api/scheduler/1
    /* body-JSON
        {
        "name":"관리자명",
        "content":"일정 내용",
        "password":"비밀번호"
        }
    */
    @PutMapping("/scheduler/{id}")
    public TaskResponseDto updateTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {
        return taskService.updateTask(id, requestDto);
    }

    // 선택한 일정 삭제
    // DELETE http://localhost:8080/api/scheduler/1
    /* body-JSON
        {
        "password":"비밀번호"
        }
    */
    @DeleteMapping("/scheduler/{id}")
    public void deleteTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {
        taskService.deleteTask(id, requestDto);
    }
}
