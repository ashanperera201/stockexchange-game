package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.CompanyActor;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.service.CompanyService;
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
@Api(value = "Company Resource REST Endpoint", description = "Shows the company info")
public class CompanyResource {
    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    private ActorRef companyActor;

    public  CompanyResource()
    {
        companyActor = StockMarketApplication.system.actorOf(CompanyActor.props());
    }

    @Autowired
    CompanyService companyservice;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @GetMapping("/companies-all")
    public ResponseEntity<List<Company>> getAllComp () throws IOException {
        List<Company> companyList = null;
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.FetchCompanyDetails(companyservice), timeout);
            companyList = (List<Company>) Await.result(future, timeout.duration());
           // List<Company> companyList = companyservice.getAllCompanies();
        }
        catch(Exception ex)
        {
            System.out.println("Exception:   " + ex.getMessage());
        }

        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }
    @GetMapping("/sectorcompany/{sectorId}")
    public ResponseEntity<List<Company>> getSectorComp (@PathVariable("sectorId") final String sectorId) throws IOException {

        List<Company> companyList = null;
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.FetchCompDetailsBySector(companyservice,sectorId), timeout);
            companyList = (List<Company>) Await.result(future, timeout.duration());
            //List<Company> companyList = companyservice.getSectorCompanies(sectorId);
        }
        catch(Exception ex)
        {
            System.out.println("Exception:   " + ex.getMessage());
        }
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> getCompanyRegister(@Valid @RequestBody Company company) throws Exception {
        log.info("REST request to save Company : {}", company);
        Company result;
        try {

            Future<Object> future = Patterns.ask(companyActor, new Messages.AddCompanyDetails(companyservice,company), timeout);
            result = (Company) Await.result(future, timeout.duration());
            //Company result = companyService.createCompany(company);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCompany{companyId}")
    public ResponseEntity<Boolean> deleteCompany (@PathVariable String companyId){
        log.debug("REST request to delete company : {}", companyId);
        boolean val=true;
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.deleteCompanyDetails(companyservice,companyId), timeout);
            val= (boolean) Await.result(future, timeout.duration());
            //companyservice.deleteCompany(name);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(val, HttpStatus.OK);
    }
}
