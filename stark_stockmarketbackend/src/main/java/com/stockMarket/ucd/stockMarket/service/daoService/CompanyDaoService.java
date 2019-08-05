package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Company;

import java.util.List;

public interface CompanyDaoService {
    List<Company> getAllCompanies();
    List<Company> getSectorCompanies(String sectorId);
    void insertCompaniesToDb(Company company);
    void deleteCompaniesToDb(String companyId);
}
