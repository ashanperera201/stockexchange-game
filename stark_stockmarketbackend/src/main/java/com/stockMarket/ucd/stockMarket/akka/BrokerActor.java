package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.*;

import java.util.List;

public class BrokerActor extends AbstractActor {
    public static Props props() {
        return Props.create(BrokerActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(BrokerMessages.GameRegister.class, msg -> {

                    try {
                        List<Game> results;
                        results = msg.gameService.getGame(msg.game);
                        boolean val=msg.groupService.deleteGroup(results.get(0).getGroupName());
                        getSender().tell(results, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(BrokerMessages.FetchBasicData.class, msg -> {

                    try {
                        org.javatuples.Triplet<List<Company>, List<Sector>, Bank> PairVal = msg.gameService.getBasicData(msg.userId);
                        getSender().tell(PairVal, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.GetCalculator.class, msg -> {

                    try {
                        Calculator calc;
                        calc=msg.gameService.getCalc(msg.calc);
                        if(calc!=null){
                        getSender().tell(calc, getSelf());}
                        else{getSender().tell(new Calculator(), getSelf());}
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.FetchUserPortfolioDetails.class, msg -> {

                    try {
                        List<UserPortfolio> UserPortList = null;
                        UserPortList=msg.gameService.getUserPortfolio(msg.userId);
                        getSender().tell(UserPortList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.AddUserPortfolioDetails.class, msg -> {

                    try {
                        UserPortfolio result= msg.gameService.setUserPortfolio(msg.userPortfolio);
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.ModifyGame.class, msg -> {
                    try {
                        Game result= msg.gameService.updateGame(msg.game);
                        msg.bankService.getBank(msg.game.getAmount(),msg.game.getUserId());
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.GenerateGameSummary.class, msg -> {
                    try {
                        List<Game> gameList=null;
                        gameList= msg.gameService.getAllSummary(msg.groupName);
                        getSender().tell(gameList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchSectorCompany.class, msg -> {
                    try {
                        List<Company> companyList=null;
                        companyList= msg.gameService.getCompanyPerSector(msg.sector);
                        getSender().tell(companyList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchShareSummarySector.class, msg -> {
                    try {
                        List<Share> shareList=null;
                        shareList= msg.gameService.getValuePerSector();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchShareSummaryCompany.class, msg -> {
                    try {
                        List<Share> shareList=null;
                        shareList= msg.gameService.getCompanyShare();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchTopCompanyGainers.class, msg -> {
                    try {
                        List<Share> shareList=null;
                        shareList= msg.gameService.getTopCompanyGainers();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchTopCompanyLoosers.class, msg -> {
                    try {
                        List<Share> shareList=null;
                        shareList= msg.gameService.getTopCompanyLoosers();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.FetchTopShares.class, msg -> {
                    try {
                        List<Share> shareList=null;
                        shareList= msg.gameService.getTopShares();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(BrokerMessages.DeleteGame.class, msg -> {
                    try {
                        int val=0;
                        val= msg.gameService.deletGame(msg.groupName);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.DeleteGameUser.class, msg -> {
                    try {
                        boolean val=true;
                        val= msg.gameService.deletGameUser(msg.groupName,msg.user);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).build();
    }
}

