package com.stockMarket.ucd.stockMarket.service.ThreadService;

import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.service.UserService;
import com.stockMarket.ucd.stockMarket.service.daoService.UserDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestThreadService implements Runnable {
    private UserDaoService userDaoService;

    private final Logger log = LoggerFactory.getLogger(UserService.class);


    @Override
    public void run(){
try {
       log.info("@@@@@____TEST_THREAD_____@@@@@@");
            Thread.sleep((long) (1 * 60000));
        }catch(Exception e){}
    }

//    public TestThreadService(String email){
//        User infoUser = userDaoService.selectUser(email);
//    }


}
