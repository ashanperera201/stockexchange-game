package com.stockMarket.ucd.stockMarket.webRest;


import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.SectorActor;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;
import com.stockMarket.ucd.stockMarket.service.SectorService;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Sector Resource REST Endpoint", description = "Shows the Sector info")
public class SectorResource {

    private final Logger log = LoggerFactory.getLogger(SectorService.class);
    private ActorRef sectorActor;

    public SectorResource() {
        sectorActor = StockMarketApplication.system.actorOf(SectorActor.props());
    }

    @Autowired
    SectorService sectorService;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @GetMapping("/sectors-all")
    public ResponseEntity<List<Sector>> getAll () throws IOException {
        List<Sector> sectorList= null;
        try {
            Future<Object> future = Patterns.ask(sectorActor, new Messages.FetchSectorDetails(sectorService), timeout);
            sectorList = (List<Sector>) Await.result(future, timeout.duration());
           //sectorList = sectorService.getAllSectors();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(sectorList, HttpStatus.OK);
    }

    @DeleteMapping("/sectors/{email}")
    public ResponseEntity<Void> deleteSector (@PathVariable String name){
        log.debug("REST request to delete user : {}", name);
        try {
             Future<Object> future = Patterns.ask(sectorActor, new Messages.DeleteSectorDetails(sectorService,name), timeout);
             Await.result(future, timeout.duration());
            //sectorService.deleteSector(name);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(name, name)).build();
    }

    @PutMapping("/sectors")
    public ResponseEntity<Sector> updateUser(@Valid @RequestBody Sector sector) throws  Exception {
        log.info("REST request to update sectors : {}", sector);
        Sector result;
        try {

            Future<Object> future = Patterns.ask(sectorActor, new Messages.DeleteSectorObject(sectorService,sector), timeout);
            result=(Sector)Await.result(future, timeout.duration());
            //Sector result = sectorService.updateCreateSector(sector);
            return ResponseEntity.created(new URI("/api/sector/" + result.getSectorId()))
                    .headers(HeaderUtil.createEntityCreationAlert(result.getSectorId(), result.getSectorId().toString()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @GetMapping("/getSumSectorsCompany")
    public ResponseEntity<List<SummarySector>> getSumSectorsCompany () throws IOException {
        List<SummarySector> sectorList= null;
        try {
            Future<Object> future = Patterns.ask(sectorActor, new Messages.RequestSectorSummary(sectorService), timeout);
            sectorList = (List<SummarySector>) Await.result(future, timeout.duration());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(sectorList, HttpStatus.OK);
    }
}

