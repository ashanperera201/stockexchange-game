package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.*;

import java.util.List;

public class MarketActor extends AbstractActor {
    public static Props props() {
        return Props.create(MarketActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(BrokerMessages.GenerateMarketBoom.class, msg -> {
                    try {
                        List<Sector> sectorList=null;
                        sectorList= msg.sectorService.getAllSectors();
                        msg.gameService.martketBoom(sectorList,msg.round);
                        getSender().tell(sectorList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).build();
    }
}


