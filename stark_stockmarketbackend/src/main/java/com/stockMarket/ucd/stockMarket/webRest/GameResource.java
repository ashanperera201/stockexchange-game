package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.BrokerActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.MarketActor;
import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.BankService;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.service.GroupService;
import com.stockMarket.ucd.stockMarket.service.SectorService;
import com.stockMarket.ucd.stockMarket.webRest.util.HeaderUtil;
import io.swagger.annotations.Api;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Game Resource REST Endpoint", description = "Shows the Game info")
public class GameResource {
    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private ActorRef brokerActor;
    private ActorRef marketActor;
    public GameResource() {
        brokerActor = StockMarketApplication.system.actorOf(BrokerActor.props());
        marketActor=  StockMarketApplication.system.actorOf(MarketActor.props());
    }

    @Autowired
    GameService gameService;
    @Autowired
    GroupService groupService;
    @Autowired
    BankService bankService;
    @Autowired
    SectorService sectorService;

    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    @PostMapping("/saveGame")
    public ResponseEntity<List<Game>> getGameRegister(@Valid @RequestBody List<Game> game) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to save Game : {}",game);
        try {

            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GameRegister(game, gameService,groupService), timeout);
            List<Game> result = (List<Game>) Await.result(future, timeout.duration());
            //List<Game> result = gameService.getGame(game);
            //delete groupdetails,begin
            //groupService.deleteGroup(game.get(0).getGroupName());
            //delete groupdetails,end
            return ResponseEntity.created(new URI("/saveGame" + ( result.get(0)).getGroupName()))
                    .headers(HeaderUtil.createEntityCreationAlert(result.get(0).getGroupName(),result.get(0).getUserId()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/BasicData-all")
    public ResponseEntity<Triplet<List<Company>, List<Sector>,Bank>> getBasicData (String userId) throws IOException {
        org.javatuples.Triplet<List<Company>, List<Sector>, Bank> PairVal=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchBasicData(userId, gameService), timeout);
            PairVal = (org.javatuples.Triplet<List<Company>, List<Sector>, Bank>) Await.result(future, timeout.duration());
            //PairVal = gameService.getBasicData(userId);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(PairVal, HttpStatus.OK);
    }

    @PostMapping("/GetCalculator")
    public ResponseEntity<Calculator> getCalcValue (@Valid @RequestBody Calculator calc) throws IOException {
        Calculator calcVal=null;
        try{
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GetCalculator(calc, gameService), timeout);
            calcVal = (Calculator) Await.result(future, timeout.duration());
         //Calculator calcVal = gameService.getCalc(calc);
        }
         catch (Exception ex)
         {
             System.out.println(ex.getMessage());
         }
         return new ResponseEntity<>(calcVal, HttpStatus.OK);
    }

    @GetMapping("/GetUserPortfolio")
    public ResponseEntity<List<UserPortfolio>> getUserPotfolioData (String userId) throws IOException {
        List<UserPortfolio> UserPortList = null;
        try{
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchUserPortfolioDetails(userId, gameService), timeout);
            UserPortList = ( List<UserPortfolio>) Await.result(future, timeout.duration());
           //UserPortList = gameService.getUserPortfolio(userId);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(UserPortList, HttpStatus.OK);
    }

    @PostMapping("/saveUserPortfoloio")
    public ResponseEntity<UserPortfolio> getGameRegister(@Valid @RequestBody UserPortfolio userPort) throws URISyntaxException, ParseException, Exception {
        UserPortfolio userPortfolio=null;
        log.info("REST request to save UserPortfoloio : {}",userPort);
        try {

            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.AddUserPortfolioDetails(userPort, gameService), timeout);
            userPortfolio = ( UserPortfolio) Await.result(future, timeout.duration());
            //UserPortfolio result = gameService.setUserPortfolio(userPort);
            //return ResponseEntity.created(new URI("/saveUserPortfoloio" + result.getCompanyId()))
                   // .headers(HeaderUtil.createEntityCreationAlert(result.getCompanyId(),result.getUserName()))
                   // .body(result);
            return new ResponseEntity<>(userPortfolio, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/updateGame")
    public ResponseEntity<Game> updateUser(@Valid @RequestBody  Game game) throws  Exception {
        log.info("REST request to update Game : {}", game);
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.ModifyGame(game, gameService,bankService), timeout);
            Game result = (Game) Await.result(future, timeout.duration());
            //Game result = gameService.updateGame(game);
            //bankService.getBank(game.getAmount(),game.getUserId());
            return ResponseEntity.created(new URI("/api/game/" + result.getGroupName()))
                    .headers(HeaderUtil.createEntityCreationAlert(result.getUserId(), result.getUserId().toString()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/marketBoom")
    public ResponseEntity<List<Sector>> getCalcValue (int round) throws IOException {
        List<Sector> sectorList=null;

        try {
            Future<Object> future = Patterns.ask(marketActor, new BrokerMessages.GenerateMarketBoom(round, gameService,sectorService), timeout);
            sectorList = (List<Sector>) Await.result(future, timeout.duration());
            //sectorList = sectorService.getAllSectors();
            //gameService.martketBoom(sectorList, round);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(sectorList, HttpStatus.OK);
    }

    @GetMapping("/gameSummary")
    public ResponseEntity<List<Game>> getGameSummary (String groupName) throws IOException {
        List<Game> gameList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GenerateGameSummary(groupName,gameService), timeout);
            gameList = (List<Game>) Await.result(future, timeout.duration());
            //gameList = gameService.getAllSummary(groupName);
            //delete
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Game>>(gameList, HttpStatus.OK);
    }

    @GetMapping("/getSectorCompany")
    public ResponseEntity<List<Company>> getSectorCompany (String sector) throws IOException {
        List<Company> resultList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchSectorCompany(sector,gameService), timeout);
            resultList = (List<Company>) Await.result(future, timeout.duration());
           //resultList = gameService.getCompanyPerSector(sector);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Company>>(resultList, HttpStatus.OK);
    }

    @GetMapping("/getShareSummarySector")
    public ResponseEntity<List<Share>> getSectorWatchSector () throws IOException {
        List<Share> resultList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchShareSummarySector(gameService), timeout);
            resultList = (List<Share>) Await.result(future, timeout.duration());
            //resultList = gameService.getValuePerSector();
            //delete
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Share>>(resultList, HttpStatus.OK);
    }

    @GetMapping("/getShareSummaryCompany")
    public ResponseEntity<List<Share>> getSectorWatchCompany () throws IOException {
        List<Share> resultList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchShareSummaryCompany(gameService), timeout);
            resultList = (List<Share>) Await.result(future, timeout.duration());
            //resultList = gameService.getCompanyShare();
        }
        catch (Exception ex)
        {}
        return new ResponseEntity<List<Share>>(resultList, HttpStatus.OK);
    }

    @GetMapping("/getTopCompanyGainers")
    public ResponseEntity<List<Share>> getTopCompanyGainers () throws IOException {
        List<Share> resultList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchTopCompanyGainers(gameService), timeout);
            resultList = (List<Share>) Await.result(future, timeout.duration());
            //resultList = gameService.getTopCompanyGainers();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Share>>(resultList, HttpStatus.OK);
    }

    @GetMapping("/getTopCompanyLoosers")
    public ResponseEntity<List<Share>> getTopCompanyLoosers () throws IOException {
        List<Share> resultList =null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchTopCompanyLoosers(gameService), timeout);
            resultList = (List<Share>) Await.result(future, timeout.duration());
            //resultList = gameService.getTopCompanyLoosers();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Share>>(resultList, HttpStatus.OK);
    }

    @GetMapping("/getTopShares")
    public ResponseEntity<List<Share>> getTopShares () throws IOException {
        List<Share> resultList=null;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchTopShares(gameService), timeout);
            resultList = (List<Share>) Await.result(future, timeout.duration());
            //resultList = gameService.getTopShares();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<List<Share>>(resultList, HttpStatus.OK);
    }

    @DeleteMapping("/obsoleteGame")
    public ResponseEntity<Void> DeleteGame (String groupName) throws IOException {
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.DeleteGame(gameService,groupName), timeout);
            int val = (int) Await.result(future, timeout.duration());
            //gameService.deleteGame(groupName);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/obsoleteGameUser")
    public ResponseEntity<Boolean> DeleteGameser (String groupName,String User) throws IOException {
        boolean val = true;
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.DeleteGameUser(gameService,groupName,User), timeout);
            val = (boolean) Await.result(future, timeout.duration());
            //gameService.deleteGameUser(groupName,User);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(val,HttpStatus.OK);
    }
}
