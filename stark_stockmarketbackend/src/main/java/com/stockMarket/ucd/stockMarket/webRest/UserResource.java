package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.PlayerActor;
import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.service.BasicDataService;
import com.stockMarket.ucd.stockMarket.service.UserService;
import com.stockMarket.ucd.stockMarket.webRest.util.HeaderUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.stockMarket.ucd.stockMarket.StockMarketApplication.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "User Resource REST Endpoint", description = "Shows the user info")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private ActorRef playerActor;

    public UserResource() {
        playerActor = system.actorOf(PlayerActor.props());
    }
    @Autowired
    UserService userService;
    @Autowired
    BasicDataService basicDataService;

    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @GetMapping("/users-all")
    public ResponseEntity<List<User>> getAll () throws IOException {
        List<User> userList=null;
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.FetchUserDetails(userService), timeout);
            userList = (List<User>) Await.result(future, timeout.duration());
            //userList = userService.findAll();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> getUserRegister(@Valid @RequestBody User user) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to save user : {}", user);
        try {

            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.AddUserDetails(user,userService,basicDataService), timeout);
            User result = (User) Await.result(future, timeout.duration());
            //User result = userService.getUser(user);
            //add default bankdetails,begin
            //basicDataService.addBank(result.getUserId());
            //add default bankdetails,end
            return ResponseEntity.created(new URI("/api/user/" + result.getEmail()))
                    .headers(HeaderUtil.createEntityCreationAlert(user.getUserName(), result.getEmail()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/email")
    ResponseEntity<User> getEmail(String email)throws IOException  {
        User user = null;
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.EmailVerification(userService,email), timeout);
            user = (User) Await.result(future, timeout.duration());
            //user = userService.getEmail(email);
             }
             catch (Exception ex){ex.getMessage();}
            return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/userUpdate")
    public ResponseEntity<User> getUpdateRegister(@Valid @RequestBody User user) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to Update user : {}", user);
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.UpdateUserDetails(userService,user), timeout);
            User result = (User) Await.result(future, timeout.duration());
            //User result = userService.updateUser(user);
            return ResponseEntity.created(new URI("/api/user/" + result.getEmail()))
                    .headers(HeaderUtil.createEntityCreationAlert(user.getUserName(), result.getEmail()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e; }
    }
    @GetMapping("/test-Thread")
    public void testThread ()  {

        userService.threadTest();
    }
    @DeleteMapping("/user/{email}")
    public ResponseEntity<Void> deleteUser (@PathVariable String email){
        log.debug("REST request to delete user : {}", email);
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.DeleteUserDetails(userService,email), timeout);
            boolean val= (boolean) Await.result(future, timeout.duration());
           //userService.deleteUser(email);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(email, email)).build();
    }

    @PostMapping("/logoutService")
    public ResponseEntity<User> getLogout(@Valid @RequestBody User user) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to Update user : {}", user);
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.LogoutDetails(userService,user), timeout);
            User result=(User)Await.result(future, timeout.duration());
            //User result = userService.getLogoutDetails(user);
            return ResponseEntity.created(new URI("/api/user/" + result.getEmail()))
                    .headers(HeaderUtil.createEntityCreationAlert(user.getUserName(), result.getEmail()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/loginService")
    public ResponseEntity<User> getLogin(@Valid @RequestBody User user) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to login user : {}", user);
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.LoginDetails(userService,user), timeout);
            User result=(User)Await.result(future, timeout.duration());
            //User result = userService.addLoginUser(user);
            return ResponseEntity.created(new URI("/api/user/" + result.getEmail()))
                    .headers(HeaderUtil.createEntityCreationAlert(user.getUserName(), result.getEmail()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/helpText")
    public ResponseEntity<HelpText> gethelpText () throws IOException {
        HelpText helpText=null;
        try {
             Future<Object> future = Patterns.ask(playerActor, new Messages.FetchHelpTextDetails(userService), timeout);
             helpText = (HelpText) Await.result(future, timeout.duration());
            //helpText = userService.fetchHelpText();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(helpText, HttpStatus.OK);
    }
}
