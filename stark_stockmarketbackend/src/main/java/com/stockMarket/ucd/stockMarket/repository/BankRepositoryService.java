package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.BankService;
import com.stockMarket.ucd.stockMarket.service.daoService.BankDaoService;
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

@Repository("mysqlDummy")
public class BankRepositoryService implements BankDaoService {
    private final Logger log = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Bank> getBankAmountsMonthly(String userID, int month, int year) {
        final String sql = "SELECT transDate,amount FROM stark.bank WHERE userId = '" + userID + " ' AND MONTH(transDate) = " + Integer.toString(month) + " AND year(transDate) = " + Integer.toString(year);
        List<Bank> AmountList = jdbcTemplate.query(sql, new BankRowMapper());
        return AmountList;
    }

    public static class BankRowMapper implements RowMapper<Bank> {
        @Override
        public Bank mapRow(ResultSet resultSet, int i) throws SQLException {
            Bank bank = new Bank();
            bank.setDate(resultSet.getDate("transDate"));
            bank.setAmount(resultSet.getInt("amount"));
            return bank;
        }
    }

    @Override
    public List<Bank>getBankAccount(String userId)
    {
        List<Bank> BankList = null;
        try {
            final String sql = "SELECT * FROM bank where userId=?";
            BankList = jdbcTemplate.query(sql,new Object[]{userId}, new BankRowMapper());
            return BankList;
        } catch (Exception val)
        {
            System.out.println("");
        }
        return BankList;

    }
    GenerateCurrentDate dateValue= new GenerateCurrentDate();
    @Override
    public void insertBanksToDb(double amount,String userId) {
        try {
            final String sql = "INSERT INTO stark.bank (userId,amount,transDate) VALUES (?,?,?)";

            final Date transDate = dateValue.getCurrentDate();

            jdbcTemplate.update(sql, new Object[]{userId, amount,transDate});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Bank Repository @@@@@@@@@@" + e);
        }

    }

    @Override
    public void deleteBankToDb(String userId)
    {
        try {
            final String sql = "delete from bank where userId = ?";
            jdbcTemplate.update(sql, new Object[]{userId});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ company Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public Bank getBankAmount(String userId) {
        Bank bank  = null;
        try {
            final String sqlBank = "SELECT amount, transDate FROM stark.bank where userId=? and countNo= (select max(countNo) FROM stark.bank where userId=?) ";
            bank = jdbcTemplate.queryForObject(sqlBank,new Object[]{userId,userId}, new BankRepositoryService.BankRowMapper());
            return bank;
        } catch (Exception val)
        {
            System.out.println(val.getMessage());
        }
        return bank;
    }
}

