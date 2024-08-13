package com.sparta.springscheduler.controller;

import com.sparta.springscheduler.dto.TaskRequestDto;
import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.entity.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final JdbcTemplate jdbcTemplate;

    public TaskController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/scheduler")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto requestDto) {
        Task task = new Task(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tasks (Name, Content, Password, CreatedAt, UpdatedAt) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getContent());
            preparedStatement.setString(3, task.getPassword());
            preparedStatement.setString(4, format.format(task.getCreatedAt()));
            preparedStatement.setString(5, format.format(task.getUpdatedAt()));
            return preparedStatement;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        task.setId(id);

        TaskResponseDto taskResponseDto = new TaskResponseDto(task);

        return taskResponseDto;
    }

    @GetMapping("/scheduler")
    public List<TaskResponseDto> getTasks() {
        String sql = "SELECT * FROM tasks";

        return jdbcTemplate.query(sql, new RowMapper<TaskResponseDto>() {
            @Override
            public TaskResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String content = rs.getString("Content");
                String createdAt = rs.getString("CreatedAt");
                String updatedAt = rs.getString("updatedAt");

                return new TaskResponseDto(id, name, content, createdAt, updatedAt);
            }
        });
    }


}
