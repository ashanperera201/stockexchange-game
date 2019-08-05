package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.service.daoService.CompanyDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CompanyService {
    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired
    @Qualifier("mysqlCS")
    private  CompanyDaoService companydaoservice;

    public List<Company> getAllCompanies() throws IOException {

        List<Company>results = this.companydaoservice.getAllCompanies();
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }
    public List<Company> getSectorCompanies(String sectorId) throws IOException {

        List<Company>results = this.companydaoservice.getSectorCompanies(sectorId);
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }
    public Company createCompany(Company company)  {
        companydaoservice.insertCompaniesToDb(company);
        return company;
    }

    public boolean deleteCompany(String companyId)  {
        this.companydaoservice.deleteCompaniesToDb(companyId);
        return true;
    }
}
