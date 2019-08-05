package com.stockMarket.ucd.stockMarket.webRest;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.BankActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.service.BankService;
import com.stockMarket.ucd.stockMarket.webRest.util.HeaderUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import akka.actor.ActorRef;
import scala.concurrent.Await;
import scala.concurrent.Future;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Bank Resource REST Endpoint", description = "Shows the Bank info")
public class BankResource {
    private final Logger log = LoggerFactory.getLogger(BankService.class);
    private ActorRef bankActor;

    public BankResource() {
        bankActor = StockMarketApplication.system.actorOf(BankActor.props());
    }

    @Autowired
    BankService bankService;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @GetMapping("/bank/{userID}/{month}/{year}")
    public ResponseEntity<List<Bank>> getBankAmountsMonthly(@PathVariable("userID") final String userID, @PathVariable("month") final int month, @PathVariable("year") final int year) {
        List<Bank> BankAmountList = null;

        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.RequestBankDetails(userID, month, year, bankService), timeout);
            BankAmountList = (List<Bank>) Await.result(future, timeout.duration());


        } catch (Exception e) {
            System.out.println("Error:   " + e.getMessage());
        }
        return new ResponseEntity<>(BankAmountList, HttpStatus.OK);
    }
    @PostMapping("/saveBank")
    public ResponseEntity<String> getBankRegister(@Valid @RequestBody Bank bank) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to save Bank : {}", bank);
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.AddBankDetails(bank, bankService), timeout);
            Await.result(future, timeout.duration());
            //bankService.getBank(bank.getAmount(),bank.getUserId());
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/bankDelete/{userId}")
    public ResponseEntity<Void> deleteBank (@PathVariable String userId){
        log.debug("REST request to delete user : {}", userId);
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.deleteBankDetails(bankService,userId), timeout);
            Await.result(future, timeout.duration());
            //bankService.deleteBank(userId);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(userId, userId)).build();
    }

    @GetMapping("/getBankDetails")
    ResponseEntity<Bank> fetchBankDetail(String userId)throws IOException {
        Bank bank = null;
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.getBankAmountDetails(bankService,userId), timeout);
            bank = (Bank) Await.result(future, timeout.duration());
            //bank = bankService.getBankValues(userId);
        }
        catch (Exception ex){ex.getMessage();}
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

}
