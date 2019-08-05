package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.domain.Sector;

public interface BasicDataDaoService {
    void insertBankToDb(String userId);
    void insertSectorsToDb(Sector sector);
    void insertCompanyToDb(Company company);
}
