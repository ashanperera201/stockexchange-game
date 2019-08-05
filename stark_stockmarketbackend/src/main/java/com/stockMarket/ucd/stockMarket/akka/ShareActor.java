package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Share;
import com.stockMarket.ucd.stockMarket.domain.ShareItem;

import java.util.List;

public class ShareActor extends AbstractActor {
    public static Props props() {
        return Props.create(ShareActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(BrokerMessages.RequestshareDetails.class, msg -> {

                    try {
                        List<ShareItem>shareItemList;
                        shareItemList = msg.shareService.getshareItems(msg.companyId,msg.userId,msg.startDate,msg.endDate);
                        getSender().tell(shareItemList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(BrokerMessages.AddShareDetails.class, msg -> {

                    try {
                        ShareItem result=  msg.shareService.getShareitems(msg.shareItem);
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.DeleteShareDetails.class, msg -> {

                    try {
                        int val = msg.shareService.deleteShareItem(msg.id);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.FetchShareDetails.class, msg -> {

                    try {
                        List<Share>shareList = null;
                        shareList = msg.shareService.findAll();
                        getSender().tell(shareList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.UpdateShareDetails.class, msg -> {

                    try {
                        Share share;
                        share = msg.shareService.updateShare(msg.share);
                        getSender().tell(share, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).build();
    }
}
