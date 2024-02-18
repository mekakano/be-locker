package com.gealocker.be.gealockerbe.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.User;
import com.gealocker.be.gealockerbe.entity.UserRowMapper;
import net.sf.jsqlparser.schema.Database;
import org.springframework.jdbc.core.RowMapper;

@Transactional
@Repository

public class UserDaoImpl implements UserDao {
    @Autowired
    @Qualifier("postgres")
    private JdbcTemplate jdbcTemplate;

    @Override
    public ApiResponse getUserByEmail(String email, String password) {
        String emailSql = "SELECT email FROM tb_users WHERE email = ?";
        String passwordSql = "SELECT * FROM tb_users WHERE email = ?";

        ApiResponse response = new ApiResponse();
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

        try {
            // Check if the email exists
            User userWithEmail = jdbcTemplate.queryForObject(emailSql, rowMapper, email);

            if (userWithEmail != null && userWithEmail.getEmail().length() > 0) {
                // If the email exists, retrieve the user's password and compare
                User userWithPassword = jdbcTemplate.queryForObject(passwordSql, rowMapper, email);
                if (userWithPassword.getPassword().equals(password)) {
                    response.setStatuscode(0);
                    response.setMessage("success");
                    response.setObject(userWithPassword);
                } else {
                    response.setStatuscode(1); // Incorrect password
                    response.setMessage("Incorrect password");
                    response.setObject(null);
                }
            } else {
                response.setStatuscode(2); // Email does not exist
                response.setMessage("Email does not exist");
                response.setObject(null);
            }
        } catch (EmptyResultDataAccessException e) {
            response.setStatuscode(2); // Email does not exist
            response.setMessage("Email does not exist");
            response.setObject(null);
        } catch (Exception e) {
            response.setStatuscode(99);
            response.setMessage("Error occurred");
            response.setObject(null);
        }

        return response;
    }

    @Override
    public ApiResponse insertUser(User user) {
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        String sql = "INSERT INTO tb_users (email, password, nama, no_ktp, phone_number) VALUES (?, ?, ?, ?, ?)";
        String checkUserExist = "SELECT * FROM tb_users where email = ?";
        ApiResponse response = new ApiResponse();
        try {
            User results = jdbcTemplate.queryForObject(checkUserExist, rowMapper, user.getEmail());
            System.out.println(results);
            if (results.getEmail().length() > 0) {
                response.setStatuscode(99);
                response.setMessage("email sudah ada");
                response.setObject(null);
            }

        } catch (Exception e) {
            try {
                int result = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getNama(),
                        user.getNo_ktp(),
                        user.getPhone_number());

                if (result > 0) {
                    response.setStatuscode(0);
                    response.setMessage("success");
                    response.setObject(null);
                }
            } catch (Exception ee) {
                ee.printStackTrace();
                response.setStatuscode(99);
                response.setMessage("error");
                response.setObject(null);
            }

        }

        return response;
    }

}
