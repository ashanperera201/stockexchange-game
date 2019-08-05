package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.daoService.GameDaoService;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    @Autowired
    @Qualifier("mysqlGS")

    private GameDaoService gamedaoService;

    public List<Game> getGame(List<Game> game)  {
        this.gamedaoService.insertGamesToDb(game);
        return game;
    }

    public Triplet<List<Company>, List<Sector>,Bank> getBasicData(String userId)  {
        org.javatuples.Triplet<List<Company>, List<Sector>,Bank> PairVal =this.gamedaoService.getBasicData(userId);
        return PairVal;
    }

    public Calculator getCalc(Calculator calc)
    {
        Calculator val=this.gamedaoService.calculateProfit(calc);
        return val;
    }

    public List<UserPortfolio> getUserPortfolio(String userId)  {
       List<UserPortfolio> val= this.gamedaoService.getUserPortfolio(userId);
       return val;
    }
    public  UserPortfolio setUserPortfolio(UserPortfolio userPort)
    {
        UserPortfolio result=  this.gamedaoService.modifyUserPortFolio(userPort);
        return result;

    }

    public int martketBoom(List<Sector> sectorList,int round)
    {
        int roundVal=0;
        int sectorCount=4;//number of sectors
        //Get the Sectors List Random Market Share price
        Random sectorTrend = new Random();
        //Get the Random Trend Market Share price
        Random randomTrend = new Random();

        for(int i =0; i < sectorCount; i++)//Assumption we have only 4 Sectors
        {
            int secTrend = sectorTrend.nextInt(5) + 1;
            int randTrend=0;
            //Get the Round based Random Values
            if(3>=round && round<=5 )
            {
               randTrend = randomTrend.nextInt(5+1)-3;
            }
            this.gamedaoService.updateMarketValue(sectorList.get(i).getSectorId(),secTrend,randTrend,round);
        }
        return  0;
    }

    public Game updateGame(Game game)  {
        this.gamedaoService.updateGame(game);
        return game;
    }

    public List<Game> getAllSummary(String groupName)  {
        List<Game> game = this.gamedaoService.getGameSummmary(groupName);
        return game;
    }
    public List<Share> getValuePerSector()
    {
        List<Share> ResultValue = this.gamedaoService.fetchSectorValues();
        return ResultValue;
    }

    public List<Company> getCompanyPerSector(String sector)
    {
        List<Company> ResultValue = this.gamedaoService.fetchCompanyValuesPerSector(sector);
        return ResultValue;
    }

    public List<Share> getCompanyShare()
    {
        List<Share> ResultValue = this.gamedaoService.fetchCompanyValues();
        return ResultValue;
    }

    public List<Share> getTopCompanyGainers()
    {
        List<Share> ResultValue = this.gamedaoService.fetchTopCompanyGainers();
        return ResultValue;
    }

    public List<Share> getTopCompanyLoosers()
    {
        List<Share> ResultValue = this.gamedaoService.fetchTopCompanyLoosers();
        return ResultValue;
    }

    public List<Share> getTopShares()
    {
        List<Share> ResultValue = this.gamedaoService.fetchTopShares();
        return ResultValue;
    }

    public int deletGame(String groupName)
    {
        this.gamedaoService.deleteGame(groupName);
        return 1;
    }

    public boolean isPortFolioExit(String companyId,String userId)
    {
       boolean val=gamedaoService.isUserPortfolioExit(companyId,userId);
       return val;
    }

    public Boolean deletGameUser(String groupName,String User)
    {
        int val=this.gamedaoService.deleteGameUser(groupName,User);
        if(val>0) {
            return true; }
        else
        {return false;}
    }
}
