package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.AIBotActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.domain.Game;
import com.stockMarket.ucd.stockMarket.service.AIBotService;
import com.stockMarket.ucd.stockMarket.service.GameService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "AI Resource REST Endpoint", description = "Shows the AI info")
public class AIBotResource {

    private final Logger log = LoggerFactory.getLogger(AIBotService.class);
    private ActorRef AIbotActor;

    public AIBotResource() {
        AIbotActor = StockMarketApplication.system.actorOf(AIBotActor.props());
    }

    @Autowired
    AIBotService aiBotService;
    @Autowired
    GameService gameService;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @PostMapping("/BotExecute")
    public ResponseEntity<Integer> executeBot(@Valid @RequestBody Game game) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to start BOT : {}", game);
        Integer GameVal=1;
        try {
            Future<Object> future = Patterns.ask(AIbotActor, new BrokerMessages.BotPlayer(aiBotService, game), timeout);
            GameVal=(Integer) Await.result(future, timeout.duration());
            //bankService.getBank(bank.getAmount(),bank.getUserId());
            return new ResponseEntity<>(GameVal, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/AddBotGame")
    public ResponseEntity<Game> addBotGame(@Valid @RequestBody Game game) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to start BOT : {}", game);
        Game GameVal=null;
        try {
            Future<Object> future = Patterns.ask(AIbotActor, new BrokerMessages.AddBotPlayergame(aiBotService, game), timeout);
            GameVal=(Game) Await.result(future, timeout.duration());
            return new ResponseEntity<>(GameVal, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/DeleteBotGame")
    public ResponseEntity<Boolean> deleteBotGame(@Valid @RequestBody String bot) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to End BOT : {}", bot);
        boolean val=true;
        try {
            gameService.deletGame(bot);
            Future<Object> future = Patterns.ask(AIbotActor, new BrokerMessages.DeleteBotPlayergame(aiBotService, bot), timeout);
            val=(boolean) Await.result(future, timeout.duration());
            return new ResponseEntity<>(val, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
