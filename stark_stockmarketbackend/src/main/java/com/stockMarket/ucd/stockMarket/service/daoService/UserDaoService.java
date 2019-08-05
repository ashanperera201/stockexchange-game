package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;

import java.util.List;

public interface UserDaoService {

    void insertUsersToDb(User user);
    List<User> getAllUsers();
    List<User> selectUsers(String email);
    User isEmailExit(String email);
    void updateUsersToDb(User user);
    boolean isUserExit(String email);
    User selectUser(String email);
    void deleteUsersToDb(String email);
    User updateLogoutUsersToDb(User user);
    User userLoginAuth(User user);
    HelpText fetchHelpData();
}
