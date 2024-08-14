package com.sparta.springscheduler.repository;

import com.sparta.springscheduler.dto.TaskResponseDto;
import com.sparta.springscheduler.entity.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tasks (Name, Content, Password, CreatedAt, UpdatedAt) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getContent());
            preparedStatement.setString(3, task.getPassword());
            preparedStatement.setString(4, format.format(task.getCreatedAt()));
            preparedStatement.setString(5, format.format(task.getUpdatedAt()));
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<TaskResponseDto> findAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, taskRowMapper());
    }

    public TaskResponseDto findById(int id) {
        String sql = "SELECT * FROM tasks WHERE Id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String content = resultSet.getString("Content");
                String createdAt = resultSet.getString("CreatedAt");
                String updatedAt = resultSet.getString("UpdatedAt");

                return new TaskResponseDto(id, name, content, createdAt, updatedAt);
            } else {
                return null;
            }
        }, id);
    }

    public List<TaskResponseDto> findByCondition(String updatedAt, String name) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM tasks WHERE 1=1");

        if (updatedAt != null && !updatedAt.isEmpty()) {
            sqlBuilder.append(" AND DATE_FORMAT(UpdatedAt, '%Y-%m-%d') = '").append(updatedAt).append("'");
        }

        if (name != null && !name.isEmpty()) {
            sqlBuilder.append(" AND Name = '").append(name).append("'");
        }

        sqlBuilder.append(" ORDER BY UpdatedAt DESC");

        return jdbcTemplate.query(sqlBuilder.toString(), taskRowMapper());
    }

    public void update(int id, String name, String content, Date updatedAt) {
        String sqlUpdate = "UPDATE tasks SET Name = ?, Content = ?, UpdatedAt = ? WHERE Id = ?";
        jdbcTemplate.update(sqlUpdate, name, content, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updatedAt), id);
    }

    public void delete(int id) {
        String sqlDelete = "DELETE FROM tasks WHERE Id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    public String findPasswordById(int id) {
        String sqlCheckPassword = "SELECT Password FROM tasks WHERE Id = ?";
        return jdbcTemplate.queryForObject(sqlCheckPassword, new Object[]{id}, String.class);
    }

    private RowMapper<TaskResponseDto> taskRowMapper() {
        return new RowMapper<TaskResponseDto>() {
            @Override
            public TaskResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String content = rs.getString("Content");
                String createdAt = rs.getString("CreatedAt");
                String updatedAt = rs.getString("UpdatedAt");

                return new TaskResponseDto(id, name, content, createdAt, updatedAt);
            }
        };
    }
}