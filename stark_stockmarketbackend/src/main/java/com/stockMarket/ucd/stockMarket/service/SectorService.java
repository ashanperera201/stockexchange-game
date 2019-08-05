package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;
import com.stockMarket.ucd.stockMarket.service.daoService.SectorDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SectorService {
    private final Logger log = LoggerFactory.getLogger(BasicDataService.class);
    @Autowired
    @Qualifier("mysqlSRS")
    private SectorDaoService sectordaoservice;
    public List<Sector> getAllSectors() throws IOException {

        List<Sector> results = this.sectordaoservice.getAllSectors();
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }
    public void deleteSector(String email)  {
        this.sectordaoservice.deleteSectorToDb(email);
    }


    public Sector updateCreateSector(Sector sector)  {
        this.sectordaoservice.updateSectorToDb(sector);
        return sector;
    }

    public List<SummarySector> fetchSummarySector()  {
        List<SummarySector> sector = this.sectordaoservice.fetchSummarySectorValues();
        return sector;
    }
}
