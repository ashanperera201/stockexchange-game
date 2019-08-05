package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.BankActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.ShareActor;
import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.domain.Share;
import com.stockMarket.ucd.stockMarket.domain.ShareItem;
import com.stockMarket.ucd.stockMarket.service.ShareService;
import com.stockMarket.ucd.stockMarket.webRest.util.HeaderUtil;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Share Resource REST Endpoint", description = "Shows the Share info")
public class ShareResource {
    private final Logger log = LoggerFactory.getLogger(ShareService.class);
    private ActorRef shareActor;

    public ShareResource() {
        shareActor = StockMarketApplication.system.actorOf(ShareActor.props());
    }

    @Autowired
    ShareService shareService;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @GetMapping("/shareItems/{companyId}/{userId}/{startDate}/{endDate}")
    public ResponseEntity<List<ShareItem>> getShareAmountsMonthly(@PathVariable("companyId") final String companyID, @PathVariable("userId") final String userId, @PathVariable("startDate") final String startDate, @PathVariable("endDate") final String endDate)
    {
        List<ShareItem> ShareItemList = null;
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.RequestshareDetails(companyID, userId, startDate, endDate, shareService), timeout);
            ShareItemList = (List<ShareItem>) Await.result(future, timeout.duration());
            //ShareItemList  =   shareService.getshareItems(companyID,userId,startDate,endDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(ShareItemList, HttpStatus.OK);
    }

    @PostMapping("/saveShareItem")
    public ResponseEntity<ShareItem> getShareItemRegister(@Valid @RequestBody ShareItem shareItem) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to save Shareitem : {}", shareItem);
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.AddShareDetails(shareItem, shareService), timeout);
            ShareItem result = (ShareItem) Await.result(future, timeout.duration());
            //ShareItem result = shareService.getShareitems(shareItem);
            return ResponseEntity.created(new URI("/saveShareItem" + ( result).getShareId()))
                    .headers(HeaderUtil.createEntityCreationAlert(shareItem.getCompanyId(),shareItem.getUserId()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/shareItems/{id}")
    public ResponseEntity<Void> deleteShareItem (@PathVariable String id){
        log.debug("REST request to delete shareItem : {}", id);
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.DeleteShareDetails(id, shareService), timeout);
            int val = (int) Await.result(future, timeout.duration());
            //shareService.deleteShareItem(id);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(id, id)).build();
    }

    @GetMapping("/share-all")
    public ResponseEntity<List<Share>> getAll () throws IOException {
        List<Share>shareList = null;
        try{
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.FetchShareDetails( shareService), timeout);
            shareList= (List<Share>)Await.result(future, timeout.duration());
            //shareList = shareService.findAll();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(shareList, HttpStatus.OK);
    }

    @PostMapping("/shareUpdate")
    public ResponseEntity<Share> getUpdateRegister(@Valid @RequestBody Share share) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to Update share : {}", share);
        try {

            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.UpdateShareDetails( share,shareService), timeout);
            Share result= (Share)Await.result(future, timeout.duration());
            //Share result = shareService.updateShare(share);
            return ResponseEntity.created(new URI("/api/share/" + result.getShareId()))
                    .headers(HeaderUtil.createEntityCreationAlert(share.getCompanyId(), result.getShareId()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }

}
