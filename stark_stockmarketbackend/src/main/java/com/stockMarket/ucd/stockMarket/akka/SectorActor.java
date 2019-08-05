package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;

import java.util.List;


public class SectorActor extends AbstractActor {

    public static Props props() {
        return Props.create(SectorActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.FetchSectorDetails.class, msg -> {

                    try {
                        List<Sector> sectorList=null;
                        sectorList = msg.sectorService.getAllSectors();
                        getSender().tell(sectorList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(Messages.DeleteSectorDetails.class, msg -> {

                    try {
                        msg.sectorService.deleteSector(msg.name);
                        getSender().tell("", getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.DeleteSectorObject.class, msg -> {

                    try {
                        msg.sectorService.updateCreateSector(msg.sector);
                        getSender().tell("", getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
                .match(Messages.RequestSectorSummary.class, msg -> {

                    try {
                        List<SummarySector> sectorList=msg.sectorService.fetchSummarySector();
                        getSender().tell(sectorList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).build();
    }
}
