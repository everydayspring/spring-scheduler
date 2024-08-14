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

// Repository : DB 접근 로직
@Repository
public class TaskRepository {

    // JDBC(Java Database Connectivity)
    private final JdbcTemplate jdbcTemplate;

    // JDBC 객체 초기화
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 일정 저장
    public int save(Task task) {
        // DB에서 AUTO_INCREMENT 옵션에 의해 생성되는 값을 담기위한 객체
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // query
        String sql = "INSERT INTO tasks (Name, Content, Password, CreatedAt, UpdatedAt) VALUES (?,?,?,?,?)";

        // 작성된 query문을 넣어 동작
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Date 자료형 변환을 위한 Format
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 전달받은 task의 값들을 입력
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getContent());
            preparedStatement.setString(3, task.getPassword());
            preparedStatement.setString(4, format.format(task.getCreatedAt()));
            preparedStatement.setString(5, format.format(task.getUpdatedAt()));
            return preparedStatement;
        }, keyHolder);

        // 생성된 id값 반환
        return keyHolder.getKey().intValue();
    }

    // 일정 전체 조회
    public List<TaskResponseDto> findAll() {
        // query
        String sql = "SELECT * FROM tasks";
        // 조회된 전체 일정 List 반환
        return jdbcTemplate.query(sql, taskRowMapper());
    }

    // id 값으로 조회
    public TaskResponseDto findById(int id) {
        // query
        String sql = "SELECT * FROM tasks WHERE Id = ?";
        // queryForObject로 단건 조회
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            String name = rs.getString("Name");
            String content = rs.getString("Content");
            String createdAt = rs.getString("CreatedAt");
            String updatedAt = rs.getString("UpdatedAt");

            // 조회된 정보를 ResponseDto에 담아서 반환
            return new TaskResponseDto(id, name, content, createdAt, updatedAt);
        });
    }

    // 일정 목록 조회
    public List<TaskResponseDto> findByCondition(String updatedAt, String name) {
        // 조건값이 없을때에도 동작하도록 허수조건 추가
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM tasks WHERE 1=1");

        // 수정일 조건이 있을 경우 추가
        if (updatedAt != null && !updatedAt.isEmpty()) {
            sqlBuilder.append(" AND DATE_FORMAT(UpdatedAt, '%Y-%m-%d') = '").append(updatedAt).append("'");
        }

        // 관리자명 조건이 있을 경우 추가
        if (name != null && !name.isEmpty()) {
            sqlBuilder.append(" AND Name = '").append(name).append("'");
        }

        // 수정일 내림차순 정렬
        sqlBuilder.append(" ORDER BY UpdatedAt DESC");

        // 조회된 일정 List 반환
        return jdbcTemplate.query(sqlBuilder.toString(), taskRowMapper());
    }

    // 일정 수정
    public void update(int id, String name, String content, Date updatedAt) {
        // query
        String sqlUpdate = "UPDATE tasks SET Name = ?, Content = ?, UpdatedAt = ? WHERE Id = ?";
        // 작성된 query 실행
        jdbcTemplate.update(sqlUpdate, name, content, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updatedAt), id);
    }

    // 일정 삭제
    public void delete(int id) {
        // query
        String sqlDelete = "DELETE FROM tasks WHERE Id = ?";
        // 작성된 query 실행
        jdbcTemplate.update(sqlDelete, id);
    }

    // JDBC에서 반환된 다건 정보 처리
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

    // id 유효성 검증
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE Id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    // password 검증
    public boolean verifyPassword(int id, String password) {
        String sql = "SELECT Password FROM tasks WHERE Id = ?";
        String storedPassword = jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
        return storedPassword != null && storedPassword.equals(password);
    }
}