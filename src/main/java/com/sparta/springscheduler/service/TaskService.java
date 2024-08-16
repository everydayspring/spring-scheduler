package com.sparta.springscheduler.service;

import com.sparta.springscheduler.dto.TaskRequestDto;
import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.entity.Task;
import com.sparta.springscheduler.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

// Service : 비즈니스 로직 처리, 필요한 Repository 호출
@Service
public class TaskService {

    // Repository 객체
    private final TaskRepository taskRepository;

    // Repository 객체 초기화
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 일정 등록
    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        // requestDto를 Entity에 담음
        Task task = new Task(requestDto);
        // Repository의 Database 등록 호출
        int id = taskRepository.save(task);
        // 일정을 등록하면서 생성된 id값을 Entity 객체에 추가
        task.setId(id);

        // ResponseDto 반환
        return new TaskResponseDto(task);
    }

    // 선택한 일정 조회
    public TaskResponseDto getTaskById(int id) {
        // id 입력값 검증
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID에 해당하는 일정이 존재하지 않습니다.");
        }
        // 조회된 일정 반환
        return taskRepository.findById(id);
    }

    // 일정 목록 조회
    public List<TaskResponseDto> searchTasks(String updatedAt, String name) {
        // 조회된 일정 List 반환
        return taskRepository.findByCondition(updatedAt, name);
    }

    // 선택한 일정 수정
    public TaskResponseDto updateTask(int id, TaskRequestDto requestDto) {
        // id 입력값 검증
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID에 해당하는 일정이 존재하지 않습니다.");
        }

        // password 입력값 검증
        if (!taskRepository.verifyPassword(id, requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Task task = new Task(requestDto);

        // Database의 일정 수정 호출
        taskRepository.update(id, task, new Date());
        // 수정 후 단건 검색하여 반환
        return taskRepository.findById(id);
    }

    // 선택한 일정 삭제
    public void deleteTask(int id, TaskRequestDto requestDto) {
        // id 입력값 검증
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID에 해당하는 일정이 존재하지 않습니다.");
        }

        // password 입력값 검증
        if (!taskRepository.verifyPassword(id, requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 일정 삭제
        taskRepository.delete(id);
    }
}