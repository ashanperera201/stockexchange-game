package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Bank;

import java.util.List;


public interface BankDaoService {

    List<Bank> getBankAmountsMonthly(String userIDint,int month,int year);
    List<Bank> getBankAccount(String userId);
    void insertBanksToDb(double amount,String userId);
    void deleteBankToDb(String userId);
    Bank getBankAmount(String userId);
}