package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.service.CompanyService;
import com.stockMarket.ucd.stockMarket.service.daoService.CompanyDaoService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Repository("mysqlCS")
public class CompanyRepositoryService implements CompanyDaoService {
    private  final Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static class  CompanyRowMapper implements RowMapper<Company>
    {
        @Override
        public Company mapRow(ResultSet resultSet, int i) throws SQLException {
            Company company = new Company();
            company.setCompanyId(resultSet.getString("companyId"));
            company.setName(resultSet.getString("name"));
            company.setAddress(resultSet.getString("address"));
            company.setSectorId(resultSet.getString("sectorId"));
            return company;
        }
    }


    @Override
    public List<Company> getAllCompanies() {
        List<Company> CompanyList = null;
        try {
            final String sql = "SELECT * FROM company";
            CompanyList = jdbcTemplate.query(sql, new CompanyRowMapper());
            return CompanyList;
        } catch (Exception val)
        {
            System.out.println("");
        }
        return CompanyList;
    }

    @Override
    public List<Company> getSectorCompanies(String sectorId) {
        List<Company> CompanyList = null;
        try {
            final String sql = "SELECT * FROM company where sectorId = ?";
            CompanyList = jdbcTemplate.query(sql, new Object[]{sectorId},new CompanyRowMapper());
            return CompanyList;
        } catch (Exception val)
        {
            System.out.println("");
        }
        return CompanyList;
    }

    @Override
    public void insertCompaniesToDb(Company company) {
        try {
            final String sql = "INSERT INTO company (companyId,name,address,sectorId) VALUES (?,?,?,?)";
            final String companyId = company.getCompanyId();
            final String name = company.getName();
            final String address = company.getAddress();
            final String sectorId = company.getSectorId();
            jdbcTemplate.update(sql, new Object[]{companyId, name, address, sectorId});
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteCompaniesToDb(String companyId)
    {
        try {
            final String sql = "delete from company where companyId = ?";
            jdbcTemplate.update(sql, new Object[]{companyId});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ company Repository @@@@@@@@@@" + e);
        }
    }
}
