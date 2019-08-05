package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Game;
import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.service.daoService.AIBotDaoService;
import com.stockMarket.ucd.stockMarket.service.daoService.GameDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AIBotService {
    private final Logger log = LoggerFactory.getLogger(AIBotService.class);
    @Autowired
    @Qualifier("mysqlAI")
    private AIBotDaoService aiBotDaoService;

    public Integer getBot(Game game) throws IOException {
         Integer results = this.aiBotDaoService.executeBot(game);
         log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
         return results;
    }

    public Game getGame(Game game)  {
        this.aiBotDaoService.insertGamesToDb(game);
        return game;
    }

    public boolean obseleteAIBot(String Bot)
    {
        boolean condition=this.aiBotDaoService.deleteBotToDb(Bot);
        return  condition;
    }
}
