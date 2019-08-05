package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;

import java.util.List;

public interface SectorDaoService {
    List<Sector> getAllSectors();
    void deleteSectorToDb(String email);
    void updateSectorToDb(Sector sector);
    List<SummarySector> fetchSummarySectorValues();
}
