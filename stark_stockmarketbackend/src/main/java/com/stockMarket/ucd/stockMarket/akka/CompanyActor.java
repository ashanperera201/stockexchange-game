package com.stockMarket.ucd.stockMarket.akka;
import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Company;

import java.util.List;

public class CompanyActor extends AbstractActor {
    public static Props props() {
        return Props.create(CompanyActor.class);
    }

    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.FetchCompanyDetails.class, msg -> {

                    try {
                        List<Company> CompanyList;
                        CompanyList = msg.companyService.getAllCompanies();
                        getSender().tell(CompanyList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(Messages.FetchCompDetailsBySector.class, msg -> {

                    try {
                        List<Company> CompanyList;
                        CompanyList = msg.companyService.getSectorCompanies(msg.sectorId);
                        getSender().tell(CompanyList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.AddCompanyDetails.class, msg -> {

                    try {
                        Company CompanyList;
                        CompanyList = msg.companyService.createCompany(msg.company);
                        getSender().tell(CompanyList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.deleteCompanyDetails.class, msg -> {

                    try {
                        boolean output = msg.companyService.deleteCompany(msg.companyId);
                        getSender().tell(output, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).build();
    }
}


