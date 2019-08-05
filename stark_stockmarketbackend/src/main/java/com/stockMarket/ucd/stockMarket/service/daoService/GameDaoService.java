package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.*;
import org.javatuples.Triplet;

import java.util.List;

public interface GameDaoService {
    void insertGamesToDb(List<Game> game);
    Triplet<List<Company>, List<Sector>,Bank> getBasicData(String userId);
    Calculator calculateProfit(Calculator calc);
    List<UserPortfolio> getUserPortfolio(String userId);
    UserPortfolio modifyUserPortFolio(UserPortfolio userPort);
    boolean isUserPortfolioExit(String companyId,String userId);
    void updateGame(Game game);
    List<Game>getGameSummmary(String GroupName);
    void updateMarketValue(String sector , int marketValue, int randomRoundValue,int ratio);
    List<Share> fetchSectorValues();
    List<Company> fetchCompanyValuesPerSector(String sector);
    List<Share> fetchCompanyValues();
    List<Share> fetchTopCompanyGainers();
    List<Share> fetchTopCompanyLoosers();
    List<Share> fetchTopShares();
    UserPortfolio fetchUserPortfolioExit(String companyId,String userId);
    void deleteGame(String groupName);
    boolean isGameGroupExit(String groupName);
    int deleteGameUser(String groupName,String user);
}
