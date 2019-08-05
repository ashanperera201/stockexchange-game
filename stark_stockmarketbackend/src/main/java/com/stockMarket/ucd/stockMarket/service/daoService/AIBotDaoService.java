package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Game;

public interface AIBotDaoService {
    int executeBot(Game game);
    void insertGamesToDb(Game game);
    void updateGame(Game game);
    boolean deleteBotToDb(String Bot);
    boolean isPortfolioExit(String userId);
}
