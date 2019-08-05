package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.service.daoService.BankDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class BankService {

    private final Logger log = LoggerFactory.getLogger(BankService.class);

    @Autowired
    @Qualifier("mysqlDummy")
    private BankDaoService bankDaoService;

    public List<Bank> getBankAmountsMonthly(String userID, int month, int year) throws IOException {

        List<Bank>  results =  this.bankDaoService.getBankAmountsMonthly(userID,month,year);
        log.info("@@@@@@@@@@@@ IAccount details @@@@@@@@@@@"+results);
        return results;
    }

    public List<Bank> getBankAccount(String userId)
    {
        List<Bank>results = this.bankDaoService.getBankAccount(userId);
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }
    public void getBank(double amount,String userId)  {

        bankDaoService.insertBanksToDb(amount,userId);
    }
    public void deleteBank(String userId)  {

        bankDaoService.deleteBankToDb(userId);
    }

    public Bank getBankValues(String userId)  {

       Bank bank =bankDaoService.getBankAmount(userId);
       return bank;
    }
}
