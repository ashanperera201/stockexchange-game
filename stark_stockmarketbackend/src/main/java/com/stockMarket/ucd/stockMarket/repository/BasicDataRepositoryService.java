package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.BasicDataService;
import com.stockMarket.ucd.stockMarket.service.daoService.BasicDataDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository("mysqlBDS")
public class BasicDataRepositoryService implements BasicDataDaoService {

    private final Logger log = LoggerFactory.getLogger(BasicDataService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    GenerateCurrentDate dateValue= new GenerateCurrentDate();
    @Override
    public void insertBankToDb(String userId) {
        try {
            final String sql = "INSERT INTO stark.bank (userId,amount,transDate) VALUES (?,?,?)";
            final int amount = 1000;
            final Date transDate = dateValue.getCurrentDate();
            jdbcTemplate.update(sql, new Object[]{userId, amount,transDate});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ group Repository @@@@@@@@@@" + e);
        }

    }

    @Override
    public void insertSectorsToDb(Sector sector)
    {

        try {

            final String sql = "INSERT INTO sector (sectorId,name) VALUES (?,?)";
            final String sectorID = sector.getSectorId();
            final String name = sector.getName();
            jdbcTemplate.update(sql, new Object[]{sectorID, name});

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Sector Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public  void insertCompanyToDb(Company company)
    {
        try {

            final String sql = "INSERT INTO company (companyId,name) VALUES (?,?)";
            final String sectorID = company.getCompanyId();
            final String name = company.getName();
            jdbcTemplate.update(sql, new Object[]{sectorID, name});

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Company Repository @@@@@@@@@@" + e);
        }

    }

}
