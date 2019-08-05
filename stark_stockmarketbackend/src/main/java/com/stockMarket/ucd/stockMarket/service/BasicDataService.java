package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.other.SequenceGenerator;
import com.stockMarket.ucd.stockMarket.service.daoService.BasicDataDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BasicDataService {
    private final Logger log = LoggerFactory.getLogger(BasicDataService.class);
    @Autowired
    @Qualifier("mysqlBDS")
    private BasicDataDaoService basicdatadaoservice;
    SequenceGenerator seq = new SequenceGenerator();
    public boolean addBank(String userID)  {
        this.basicdatadaoservice.insertBankToDb(userID);
        return true;
    }

    public void addSector()  {
        Sector sector=null;
        for(int i=0;i<4;i++) {
            sector.setSectorId(SequenceGenerator.generateSequence("sector"));
            sector.setName("");
            this.basicdatadaoservice.insertSectorsToDb(sector);
        }
    }
}
