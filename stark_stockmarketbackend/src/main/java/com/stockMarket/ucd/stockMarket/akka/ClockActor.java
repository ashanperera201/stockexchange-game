package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.Group;

import java.util.List;

public class ClockActor extends AbstractActor {
    public static Props props() {
        return Props.create(ClockActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.AddGroupDetails.class, msg -> {

                    try {
                        Group result;
                        result = msg.groupService.getGroup(msg.group);
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }

                }).match(Messages.FetchGroupDetails.class, msg -> {

                    try {
                        List<Group> groupList=null;
                        groupList = msg.groupService.getAll();
                        getSender().tell(groupList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.DeleteGroupDetails.class, msg -> {

                    try {
                        boolean val=msg.groupService.deleteGroup(msg.group);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(Messages.UpdateGroupDetails.class, msg -> {

                    try {
                        Group result;
                        result = msg.groupService.updateGroup(msg.group);
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).build();
    }
}