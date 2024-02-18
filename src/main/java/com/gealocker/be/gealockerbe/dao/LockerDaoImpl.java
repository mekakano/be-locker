package com.gealocker.be.gealockerbe.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.gealocker.be.gealockerbe.entity.ApiResponse;
import com.gealocker.be.gealockerbe.entity.Locker;
import com.gealocker.be.gealockerbe.entity.User;
import org.springframework.jdbc.core.RowMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Transactional
@Repository

public class LockerDaoImpl implements LockerDao {
    @Autowired
    @Qualifier("postgres")
    private JdbcTemplate jdbcTemplate;

    @Override
    public ApiResponse rent(Locker locker) {
        // Get current timestamp
        Locker lockerTemp = new Locker();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Add one day
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Create Timestamp object for the new date
        Timestamp timestampTomorrow = new Timestamp(calendar.getTimeInMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String password_locker = UUID.randomUUID().toString();
        password_locker = password_locker.substring(0, 8).toUpperCase();
        String sql = "INSERT INTO tb_locker (id_user, nomor_locker, tanggal_peminjaman, tanggal_pengembalian,created_at, deposit, password_locker, attemp_password, denda) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String lihatJumlah = "SELECT nomor_locker FROM tb_locker WHERE id_user = ? and updated_at is null";
        ApiResponse response = new ApiResponse();
        try {
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(lihatJumlah, locker.getId_user());
            if (resultList.size() == 3) {
                response.setStatuscode(99);
                response.setMessage("MAKSIMAL MEMINJAM LOCKER 3");
                response.setObject(lockerTemp);
            } else {
                int result = jdbcTemplate.update(sql, locker.getId_user(), locker.getNomor_locker(), timestamp,
                        timestampTomorrow, timestamp, 10000, password_locker, 0, 0);

                if (result > 0) {
                    lockerTemp.setPassword_locker(password_locker);
                    lockerTemp.setNomor_locker(locker.getNomor_locker());
                    lockerTemp.setId_user(locker.getId_user());
                    response.setStatuscode(0);
                    response.setMessage("success");
                    response.setObject(lockerTemp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatuscode(99);
            response.setMessage("error");
            response.setObject(null);
        }

        return response;
    }

    @Override
    public ApiResponse pengembalian(Locker locker) {
        int id_user = locker.getId_user();
        String nomor_locker = locker.getNomor_locker();
        String password_locker = locker.getPassword_locker();
        boolean isTrue = checkPasswordLocker(locker);
        Locker lockerTemp = new Locker();
        ApiResponse response = new ApiResponse();
        String created_atSql = "SELECT * FROM tb_locker WHERE id_user = ? and nomor_locker = ? and updated_at is NULL";
        String updateSqlDenda = "UPDATE tb_locker SET nomor_locker = '', updated_at = ?, denda = ?, deposit = ? WHERE id_user = ? and nomor_locker = ?";
        RowMapper<Locker> rowMapper = new BeanPropertyRowMapper<>(Locker.class);
        Timestamp timestampnow = new Timestamp(System.currentTimeMillis());

        if (isTrue) {
            try {
                // Check if the email exists
                int success = 0;
                int successClosed = 0;
                String updateIsClosed = "UPDATE tb_locker SET isClosed = true WHERE id_user = ? and nomor_locker = ? and password_locker = ?";
                successClosed = jdbcTemplate.update(updateIsClosed,
                        id_user, nomor_locker,
                        password_locker);

                if (successClosed > 0) {
                    Locker created_at = jdbcTemplate.queryForObject(created_atSql, rowMapper, id_user,
                            nomor_locker);
                    if (created_at != null && created_at.getId_user() > 0) {

                        if (timestampnow.compareTo(created_at.getTanggal_pengembalian()) > 0) {
                            Timestamp timestampNow2 = Timestamp.valueOf(LocalDateTime.now());
                            Timestamp timestampCreatedAt = Timestamp
                                    .valueOf(created_at.getTanggal_pengembalian().toString());

                            // Convert Timestamp to LocalDateTime
                            LocalDateTime now = timestampNow2.toLocalDateTime();
                            LocalDateTime created = timestampCreatedAt.toLocalDateTime();

                            Duration duration = Duration.between(created, now);
                            long days = duration.toDays();
                            int days2 = (int) days;
                            int deposit = 0;
                            int denda = (int) days2 * 5000;
                            if (denda == 5000) {
                                denda = created_at.getDeposit() - denda;
                                lockerTemp.setDeposit(denda);
                                lockerTemp.setDenda(denda);

                            } else if (denda == 10000) {
                                denda = created_at.getDeposit();
                                deposit = 0;
                                lockerTemp.setDeposit(deposit);
                                lockerTemp.setDenda(10000);
                            } else {
                                denda = denda - created_at.getDeposit();
                                lockerTemp.setDeposit(0);
                                lockerTemp.setDenda(denda);
                            }

                            success = jdbcTemplate.update(updateSqlDenda,
                                    timestampnow, denda, 0, locker.getId_user(), locker.getNomor_locker());

                        } else {
                            success = jdbcTemplate.update(updateSqlDenda,
                                    timestampnow, 0, 0, locker.getId_user(), locker.getNomor_locker());
                            lockerTemp.setDeposit(10000);
                            lockerTemp.setDenda(0);
                        }

                        if (success > 0) {
                            response.setStatuscode(0);
                            response.setMessage("success");
                            response.setObject(lockerTemp);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatuscode(99);
                response.setMessage("Error occurred");
                response.setObject(null);
            }
        } else {
            try {
                // Check if the email exists
                int success = 0;
                int denda = 0;
                int deposit = 0;
                Locker created_at = jdbcTemplate.queryForObject(created_atSql, rowMapper,
                        id_user,
                        password_locker);

                if (created_at != null && created_at.getId_user() > 0) {

                    if (timestampnow.compareTo(created_at.getTanggal_pengembalian()) > 0) {
                        Timestamp timestampNow2 = Timestamp.valueOf(LocalDateTime.now());
                        Timestamp timestampCreatedAt = Timestamp
                                .valueOf(created_at.getTanggal_pengembalian().toString());

                        // Convert Timestamp to LocalDateTime
                        LocalDateTime now = timestampNow2.toLocalDateTime();
                        LocalDateTime created = timestampCreatedAt.toLocalDateTime();

                        Duration duration = Duration.between(created, now);
                        long days = duration.toDays();
                        int days2 = (int) days;
                        deposit = 0;
                        denda = (int) days2 * 5000;
                        if (denda == 5000) {
                            denda = created_at.getDeposit() - denda;
                            lockerTemp.setDeposit(denda);
                            lockerTemp.setDenda(denda);

                        } else if (denda == 10000) {
                            denda = created_at.getDeposit();
                            deposit = 0;
                            lockerTemp.setDeposit(deposit);
                            lockerTemp.setDenda(10000);
                        } else {
                            denda = denda - created_at.getDeposit();
                            lockerTemp.setDeposit(0);
                            lockerTemp.setDenda(denda);
                        }
                    } else {
                        lockerTemp.setDeposit(10000);
                        lockerTemp.setDenda(0);
                    }
                }
                denda += 25000;
                jdbcTemplate.update(updateSqlDenda,
                        timestampnow, 25000, 0, locker.getId_user(), locker.getNomor_locker());
                lockerTemp.setDenda(denda);
                // lockerTemp.setAttemp_password(attempPassword);
                lockerTemp.setDeposit(0);
                response.setStatuscode(99);
                response.setMessage("3x salah password");
                response.setObject(lockerTemp);

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatuscode(99);
                response.setMessage("Error occurred");
                response.setObject(null);
            }

        }

        return response;
    }

    public boolean checkPasswordLocker(Locker locker) {
        boolean isTrue = false;
        String checkPassword = "SELECT * FROM tb_locker WHERE id_user = ? and nomor_locker = ?";
        RowMapper<Locker> rowMapper = new BeanPropertyRowMapper<>(Locker.class);
        String password_locker = locker.getPassword_locker();
        try {
            Locker checkPasswordLocker = jdbcTemplate.queryForObject(checkPassword, rowMapper,
                    locker.getId_user(), locker.getNomor_locker());

            if (checkPasswordLocker != null) {
                if (checkPasswordLocker.getPassword_locker().equals(password_locker)) {
                    isTrue = true;
                } else {
                    int attempPassword = checkPasswordLocker.getAttemp_password();
                    attempPassword += 1;
                    String updateAttempPassword = "UPDATE tb_locker SET attemp_password = ? WHERE id_user = ? and nomor_locker = ?";
                    jdbcTemplate.update(updateAttempPassword,
                            attempPassword, checkPasswordLocker.getId_user(), checkPasswordLocker.getNomor_locker());
                }

            } else {
                int attempPassword = checkPasswordLocker.getAttemp_password();
                attempPassword += 1;
                String updateAttempPassword = "UPDATE tb_locker SET attemp_password = ? WHERE id_user = ? and nomor_locker = ?";
                jdbcTemplate.update(updateAttempPassword,
                        attempPassword, checkPasswordLocker.getId_user(), checkPasswordLocker.getNomor_locker());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isTrue;
    }

    @Override
    public ApiResponse lihatData(User user) {
        String selectData = "SELECT nomor_locker, tanggal_peminjaman, tanggal_pengembalian, deposit, isopen, isclosed FROM tb_locker WHERE id_user = ? and updated_at IS NULL";

        ApiResponse response = new ApiResponse();
        try {
            // Check if the data exists
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(selectData, user.getId_user());

            response.setStatuscode(00); // Email does not exist
            response.setMessage("Data Found");
            response.setObject(resultList);

        } catch (EmptyResultDataAccessException e) {
            response.setStatuscode(99); // Email does not exist
            response.setMessage("Data Not Found");
            response.setObject(null);
        } catch (Exception e) {
            response.setStatuscode(99);
            response.setMessage("Error occurred");
            response.setObject(null);
        }

        return response;
    }

    @Override
    public ApiResponse lihatAvailable() {
        String selectData = "SELECT nomor_locker FROM tb_locker where updated_at is NULL";

        ApiResponse response = new ApiResponse();
        try {
            // Check if the data exists
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(selectData);

            response.setStatuscode(00); // Email does not exist
            response.setMessage("Data Found");
            response.setObject(resultList);

        } catch (EmptyResultDataAccessException e) {
            response.setStatuscode(99); // Email does not exist
            response.setMessage("Data Not Found");
            response.setObject(null);
        } catch (Exception e) {
            response.setStatuscode(99);
            response.setMessage("Error occurred");
            response.setObject(null);
        }

        return response;
    }

    @Override
    public ApiResponse validatePasswordLocker(Locker locker) {
        Locker lockerTemp = new Locker();
        String selectData = "SELECT * FROM tb_locker where id_user = ? and nomor_locker = ? and updated_at is NULL";
        RowMapper<Locker> rowMapper = new BeanPropertyRowMapper<>(Locker.class);
        ApiResponse response = new ApiResponse();
        String id_user = locker.getPassword_locker();
        String password_locker = locker.getPassword_locker();
        String nomor_locker = locker.getNomor_locker();
        String updateSqlDenda = "UPDATE tb_locker SET nomor_locker = '', updated_at = ?, denda = ?, deposit = ? WHERE id_user = ? and nomor_locker = ?";
        Timestamp timestampnow = new Timestamp(System.currentTimeMillis());
        int denda = 0;
        int deposit = 0;
        try {
            // Check if the data exists
            Locker checkPasswordLocker = jdbcTemplate.queryForObject(selectData, rowMapper,
                    locker.getId_user(), locker.getNomor_locker());

            if (checkPasswordLocker.getPassword_locker().equals(password_locker)) {
                String updateIsOpen = "UPDATE tb_locker SET isopen = true WHERE id_user = ? and nomor_locker = ? and password_locker = ?";
                jdbcTemplate.update(updateIsOpen,
                        checkPasswordLocker.getId_user(), checkPasswordLocker.getNomor_locker(),
                        checkPasswordLocker.getPassword_locker());
                response.setStatuscode(00);
                response.setMessage("Data Found");
                response.setObject(locker.getPassword_locker());
            } else {
                int attempPassword = checkPasswordLocker.getAttemp_password();
                attempPassword += 1;
                String updateAttempPassword = "UPDATE tb_locker SET attemp_password = ? WHERE id_user = ? and nomor_locker = ? and password_locker = ?";
                jdbcTemplate.update(updateAttempPassword,
                        attempPassword, checkPasswordLocker.getId_user(), checkPasswordLocker.getNomor_locker(),
                        checkPasswordLocker.getPassword_locker());
                if (attempPassword == 3) {

                    Locker created_at = jdbcTemplate.queryForObject(selectData, rowMapper,
                            checkPasswordLocker.getId_user(),
                            checkPasswordLocker.getNomor_locker());

                    if (created_at != null && created_at.getId_user() > 0) {

                        if (timestampnow.compareTo(created_at.getTanggal_pengembalian()) > 0) {
                            Timestamp timestampNow2 = Timestamp.valueOf(LocalDateTime.now());
                            Timestamp timestampCreatedAt = Timestamp
                                    .valueOf(created_at.getTanggal_pengembalian().toString());

                            // Convert Timestamp to LocalDateTime
                            LocalDateTime now = timestampNow2.toLocalDateTime();
                            LocalDateTime created = timestampCreatedAt.toLocalDateTime();

                            Duration duration = Duration.between(created, now);
                            long days = duration.toDays();
                            int days2 = (int) days;
                            deposit = 0;
                            denda = (int) days2 * 5000;
                            if (denda == 5000) {
                                denda = created_at.getDeposit() - denda;
                                lockerTemp.setDeposit(denda);
                                lockerTemp.setDenda(denda);

                            } else if (denda == 10000) {
                                denda = created_at.getDeposit();
                                deposit = 0;
                                lockerTemp.setDeposit(deposit);
                                lockerTemp.setDenda(10000);
                            } else {
                                denda = denda - created_at.getDeposit();
                                lockerTemp.setDeposit(0);
                                lockerTemp.setDenda(denda);
                            }
                        } else {
                            lockerTemp.setDeposit(10000);
                            lockerTemp.setDenda(0);
                        }
                    }
                    denda += 25000;
                    jdbcTemplate.update(updateSqlDenda,
                            timestampnow, 25000, 0, locker.getId_user(), locker.getNomor_locker());
                    lockerTemp.setDenda(denda);
                    lockerTemp.setAttemp_password(attempPassword);
                    lockerTemp.setDeposit(0);
                    response.setStatuscode(99);
                    response.setMessage("3x salah password");
                    response.setObject(lockerTemp);
                } else {
                    response.setStatuscode(99); // Email does not exist
                    response.setMessage("Data Not Found");
                    response.setObject(null);
                }

            }

        } catch (EmptyResultDataAccessException e) {
            response.setStatuscode(99); // Email does not exist
            response.setMessage("Data Not Found woi");
            response.setObject(null);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatuscode(99);
            response.setMessage("Error occurred");
            response.setObject(null);
        }

        return response;
    }

}
