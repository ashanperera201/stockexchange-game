package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.other.SequenceGenerator;
import com.stockMarket.ucd.stockMarket.service.UserService;
import com.stockMarket.ucd.stockMarket.service.daoService.UserDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@Repository("mysql")
public class UserRepositoryService implements UserDaoService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    SequenceGenerator seq = new SequenceGenerator();
    public static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setUserId(resultSet.getString("userId"));
            user.setUserName(resultSet.getString("userName"));
            user.setEmail(resultSet.getString("email"));
            user.setAddress(resultSet.getString("address"));
            user.setContactNo(resultSet.getInt("contactNo"));
            user.setNic(resultSet.getString("nic"));
            user.setDob(resultSet.getDate("dob"));
            return user;
        }
    }

    private static class HelpRowMapper implements RowMapper<HelpText> {

        @Override
        public HelpText mapRow(ResultSet resultSet, int i) throws SQLException {
            HelpText helpText = new HelpText();
            helpText.setHeader(resultSet.getString("header"));
            helpText.setBody(resultSet.getString("body"));
            helpText.setFormula(resultSet.getString("formula"));
            helpText.setFooter(resultSet.getString("footer"));
            helpText.setEnd(resultSet.getString("end"));
            return helpText;
        }
    }

    // @Override
    public List<User> getAllUsers() {
        // SELECT column_name(s) FROM table_name
        final String sql = "SELECT * FROM users";
        List<User> userList = jdbcTemplate.query(sql, new UserRowMapper());
        return userList;
    }

    @Override
    public void insertUsersToDb(User user) {
        try {

            if (!isUserExit(user.getEmail())) {
                user.setUserId(SequenceGenerator.generateSequence("user"));
                final String sql = "INSERT INTO users (userId,username,email,address,contactNo,nic,dob) VALUES (?,?,?,?,?,?,?)";
                final String userId = user.getUserId();
                final String userName = user.getUserName();
                final String email = user.getEmail();
                final String address = user.getAddress();
                final int contactNo = user.getContactNo();
                final String nic = user.getNic();
                final Date dob = user.getDob();

                jdbcTemplate.update(sql, new Object[]{userId, userName, email, address, contactNo, nic, dob});
            }
            else
            {
                System.out.println("Invalid");
            }


        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + e);
        }

    }

    @Override
    public List<User> selectUsers(String email) {
        // SELECT column_name(s) FROM table_name
        final String sql = "SELECT * FROM users WHERE email=?; ";
        List<User> userList = jdbcTemplate.query(sql, new Object[]{email},new UserRowMapper());
        return userList;
    }

    @Override
    public boolean isUserExit(String email) {
        //dont delete this code
        boolean result = false;
        final String sql = "SELECT count(*) FROM users WHERE email = ?";
        int count = (Integer) jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public User isEmailExit(String email) {

        final String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{email}, new UserRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + ex);
        }
        return user;
    }

    @Override
    public void updateUsersToDb(User user)
    {
        try {
                final String sql = "UPDATE users set username=? ,email =?, address=?,contactNo=?,nic=?,dob=? where userId =?";
                final String userId = user.getUserId();
                final String userName = user.getUserName();
                final String email = user.getEmail();
                final String address = user.getAddress();
                final int contactNo = user.getContactNo();
                final String nic = user.getNic();
                final Date dob = user.getDob();
                jdbcTemplate.update(sql, new Object[]{userName, email, address, contactNo, nic, dob,userId});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + e);
        }
    }


    @Override
    public User selectUser(String email) {
        final String sql = "SELECT * FROM users WHERE email=?; ";
        User userList = (User) jdbcTemplate.query(sql, new Object[]{email},new UserRowMapper());
        return userList;
    }
    @Override
    public void deleteUsersToDb(String email)
    {
        try {
            List<User> userList=selectUsers(email);
            final String sql = "delete from bank where userId = ?";
            jdbcTemplate.update(sql, new Object[]{userList.get(0).getUserId()});
            final String sql1 = "delete from users where userId = ?";
            jdbcTemplate.update(sql1, new Object[]{userList.get(0).getUserId()});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public User updateLogoutUsersToDb(User user)
    {
        try {
            final String sql = "UPDATE Login set status=?  where name =?";
            final String userName = user.getUserName();
            final String status = "Active";
            jdbcTemplate.update(sql, new Object[]{userName, status,});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + e);
        }
        return  user;
    }

    @Override
    public User userLoginAuth(User user) {
        return null;
    }

    @Override
    public HelpText fetchHelpData() {
        HelpText helpText=null;
        try {
            final String sql = "SELECT * FROM helpData";
            helpText = jdbcTemplate.queryForObject(sql, new HelpRowMapper());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return helpText;
    }
}
