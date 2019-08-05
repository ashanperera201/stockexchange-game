package com.stockMarket.ucd.stockMarket.service;

import java.io.*;
import java.util.*;

import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.service.ThreadService.TestThreadService;
import com.stockMarket.ucd.stockMarket.service.daoService.UserDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users.
 */

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    @Qualifier("mysql")
    private UserDaoService userDaoService;

    public User getUser(User user)  {
        this.userDaoService.insertUsersToDb(user);
        return user;
    }

    public List<User> findAll() throws IOException {

        List<User>results = this.userDaoService.getAllUsers();
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }

    public User getEmail(String email)  {
        User results= userDaoService.isEmailExit(email);
        return results;
    }

    public User updateUser(User user)  {
            this.userDaoService.updateUsersToDb(user);
            return user;
    }
    public void threadTest ()  {

        for (int i = 0; i == 0; ) {
            try {
                TestThreadService testThreadService=new TestThreadService();
                testThreadService.run();
            }
            catch(Exception e) {
                System.err.println(" main error :"+e.getMessage());
            }
        }
    }
    public boolean deleteUser(String email)  {
        this.userDaoService.deleteUsersToDb(email);
        return true;
    }

    public  User getLogoutDetails(User user)
    {
        this.userDaoService.updateLogoutUsersToDb(user);
        return  user;
    }

    public  User addLoginUser(User user)
    {
        this.userDaoService.userLoginAuth(user);
        return  user;
    }
    public HelpText fetchHelpText()
    {
        HelpText helpText=this.userDaoService.fetchHelpData();
        return  helpText;
    }

}







