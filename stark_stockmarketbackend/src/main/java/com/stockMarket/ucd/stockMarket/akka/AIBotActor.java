package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Game;

public class AIBotActor extends AbstractActor {
    public static Props props() {
        return Props.create(AIBotActor.class);
    }

    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(BrokerMessages.BotPlayer.class, msg -> {
                    Integer val=0;
                    try {
                        val = msg.aiBotService.getBot(msg.game);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                })
                .match(BrokerMessages.AddBotPlayergame.class, msg -> {
                    try {
                        Game game = msg.aiBotService.getGame(msg.game);
                        if(game!=null){
                        getSender().tell(game, getSelf());}
                        else{getSender().tell(new Game(), getSelf());}
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                })
                .match(BrokerMessages.DeleteBotPlayergame.class, msg -> {
                    boolean expect=true;
                    try {
                        expect = msg.aiBotService.obseleteAIBot(msg.bot);
                        getSender().tell(expect, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).build();
    }
}

