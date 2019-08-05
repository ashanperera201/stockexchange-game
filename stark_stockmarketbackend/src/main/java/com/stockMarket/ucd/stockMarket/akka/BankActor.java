package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Bank;
import java.util.List;


public class BankActor extends AbstractActor {
    public static Props props() {
        return Props.create(BankActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RequestBankDetails.class, msg -> {

                    try {
                        List<Bank> BankAmountList;
                        BankAmountList = msg.bankService.getBankAmountsMonthly(msg.UserID, msg.Month, msg.Year);
                        getSender().tell(BankAmountList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(Messages.AddBankDetails.class, msg -> {

                    try {
                        msg.bankService.getBank(msg.bank.getAmount(),msg.bank.getUserId());
                        getSender().tell("", getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.deleteBankDetails.class, msg -> {

                    try {
                        msg.bankService.deleteBank(msg.userId);
                        getSender().tell("", getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(Messages.getBankAmountDetails.class, msg -> {
                    try {
                        Bank bank= msg.bankService.getBankValues(msg.userId);
                        if(bank!=null) {
                            getSender().tell(bank, getSelf());
                        }
                        else
                        {
                            getSender().tell(new Bank(), getSelf());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).build();
    }
}




