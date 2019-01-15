package no.ntnu.sysdev.SpringBoot_JDBC_DEMO.repository;

import no.ntnu.sysdev.SpringBoot_JDBC_DEMO.Entity.User;
import no.ntnu.sysdev.SpringBoot_JDBC_DEMO.Entity.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        RowMapper<User> rowMapper = new UserRowMapper();
        return this.jdbcTemplate.query(sql, rowMapper);
    }

    // TODO Change this so it can return null
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        RowMapper<User> rowMapper = new UserRowMapper();
        User user = null;
        try {
            user = this.jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            e.getStackTrace();
        }
        return user;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO user (name, email, phone, age) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(),
                user.getPhone(), user.getAge());
    }

    public void delete(String email) {
        String sql = "DELETE FROM user WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }

}
