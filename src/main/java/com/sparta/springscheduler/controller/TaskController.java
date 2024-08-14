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
import java.util.Date;
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

    @GetMapping("/scheduler/{id}")
    public TaskResponseDto getTaskById(@PathVariable int id) {
        String sql = "SELECT * FROM tasks WHERE Id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String content = resultSet.getString("Content");
                String createdAt = resultSet.getString("CreatedAt");
                String updatedAt = resultSet.getString("updatedAt");

                return new TaskResponseDto(id, name, content, createdAt, updatedAt);
            } else {
                return null;
            }
        }, id);
    }

    @GetMapping("/scheduler/search")
    public List<TaskResponseDto> searchTasks(
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) String name) {

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM tasks WHERE 1=1");

        if (updatedAt != null && !updatedAt.isEmpty()) {
            sqlBuilder.append(" AND DATE_FORMAT(UpdatedAt, '%Y-%m-%d') = '").append(updatedAt).append("'");
        }

        if (name != null && !name.isEmpty()) {
            sqlBuilder.append(" AND Name = '").append(name).append("'");
        }

        sqlBuilder.append(" ORDER BY UpdatedAt DESC");

        String sql = sqlBuilder.toString();

        return jdbcTemplate.query(sql, new RowMapper<TaskResponseDto>() {
            @Override
            public TaskResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("Id");
                String content = rs.getString("Content");
                String taskName = rs.getString("Name");
                String createdAt = rs.getString("CreatedAt");
                String updated = rs.getString("UpdatedAt");

                return new TaskResponseDto(id, taskName, content, createdAt, updated);
            }
        });
    }

    @PutMapping("/scheduler/{id}")
    public TaskResponseDto updateTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {

        String sqlCheckPassword = "SELECT Password FROM tasks WHERE Id = ?";
        String storedPassword = jdbcTemplate.queryForObject(sqlCheckPassword, new Object[]{id}, String.class);
        if (storedPassword == null || !storedPassword.equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String sqlUpdate = "UPDATE tasks SET Name = ?, Content = ?, UpdatedAt = ? WHERE Id = ?";
        jdbcTemplate.update(sqlUpdate, requestDto.getName(), requestDto.getContent(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), id);

        return getTaskById(id);
    }

    @DeleteMapping("/scheduler/{id}")
    public void deleteTask(@PathVariable int id, @RequestBody TaskRequestDto requestDto) {

        String sqlCheckPassword = "SELECT Password FROM tasks WHERE Id = ?";
        String storedPassword = jdbcTemplate.queryForObject(sqlCheckPassword, new Object[]{id}, String.class);
        if (storedPassword == null || !storedPassword.equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String sqlDelete = "DELETE FROM tasks WHERE Id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }
}
