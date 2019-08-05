package com.stockMarket.ucd.stockMarket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;

import java.util.List;

public class PlayerActor extends AbstractActor {
    public static Props props() {
        return Props.create(PlayerActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(BrokerMessages.FetchUserDetails.class, msg -> {

                    try {
                        List<User> userList = null;
                        userList=msg.userService.findAll();
                        getSender().tell(userList, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.AddUserDetails.class, msg -> {

                    try {
                        User result= msg.userService.getUser(msg.user);
                        boolean val=msg.basicDataService.addBank(result.getUserId());
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.EmailVerification.class, msg -> {
                    try {
                        User result= msg.userService.getEmail(msg.email);
                        //Login Online Process
                        if(result != null) {
                            getSender().tell(result, getSelf());
                        }
                        else
                        {
                            getSender().tell(new User(), getSelf());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.UpdateUserDetails.class, msg -> {
                    try {
                        User result= msg.userService.updateUser(msg.user);
                        if(result!=null){ getSender().tell(result, getSelf());}
                        else{getSender().tell(new User(), getSelf());}
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.DeleteUserDetails.class, msg -> {
                    try {
                        boolean val=msg.userService.deleteUser(msg.email);
                        getSender().tell(val, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }).match(BrokerMessages.LogoutDetails.class, msg -> {
                    try {
                        User result=msg.userService.getLogoutDetails(msg.user);
                        getSender().tell(result, getSelf());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).match(Messages.FetchHelpTextDetails.class, msg -> {
                    try {
                        HelpText helpText=msg.userService.fetchHelpText();
                        if(helpText!=null){getSender().tell(helpText, getSelf());}
                        else {getSender().tell(new HelpText(), getSelf());}
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } }).build();
    }
}

