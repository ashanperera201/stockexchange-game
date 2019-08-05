package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.PlayerActor;
import com.stockMarket.ucd.stockMarket.domain.HelpText;
import com.stockMarket.ucd.stockMarket.domain.User;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.BasicDataService;
import com.stockMarket.ucd.stockMarket.service.UserService;
import com.stockMarket.ucd.stockMarket.webRest.UserResource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerActorTest {
    //Global variables declaration,begins
    List<User> userListMock = new ArrayList<>();
    List<User> userList = new ArrayList<>();
    User user = new User();
    User user1 = new User();

    HelpText helpText1 = new HelpText();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    GenerateCurrentDate generateCurrentDate = new GenerateCurrentDate();
    //Global variables declaration,ends
    @InjectMocks
    private UserResource userResource;
    @Autowired
    private UserService userService;
    @Autowired
    BasicDataService basicDataService;

    private static ActorSystem system;

    private ActorRef playerActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void setupSubject() {
        final Props props = PlayerActor.props();
        playerActor = system.actorOf(props);
    }

    @Before
    public void init() {
        //Dummy player values,begin
        user.setUserName("stark");
        user.setUserId("stark");
        user.setEmail("stark@gmail.com");
        user.setNic("43524242");
        user.setAddress("Pluto");
        user.setContactNo(1234567890);
        user.setDob(generateCurrentDate.getCurrentDate());
        //Dummy player values,end

        //Dummy user dummy values,begin
        user1.setUserName("starkdummy");
        user1.setUserId("starkdummy");
        user1.setEmail("starkd@gmail.com");
        user1.setNic("43524242");
        user1.setAddress("mars");
        user1.setContactNo(1234567890);
        user1.setDob(generateCurrentDate.getCurrentDate());
        //Dummy usr dummy values,end

        //Assigning to actual checking objects ,begin
        userList.add(user);
        userListMock.add(user1);
        //Assigning to actual checking objects ,end
    }

    @Test
    public void addUserRegister() {
        boolean val = true;
        User result = new User();
        try {
            Mockito.when(userService.getUser(user1)).thenReturn(user1);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Mockito.when(basicDataService.addBank(user1.getUserId())).thenReturn(val);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.AddUserDetails(user, userService, basicDataService), timeout);
            result = (User) Await.result(future, timeout.duration());//Akka playerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertNotNull(result);//check to see whether data user is saved in database and not null from db
    }

    @Test
    public void getEmail() {
        User fetchUser = new User();
        try {
            Mockito.when(userService.getEmail(user1.getEmail())).thenReturn((user1));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.EmailVerification(userService, user.getEmail()), timeout);
            fetchUser = (User) Await.result(future, timeout.duration());//Akka playerActor tell imposed
            //Testing whether user email values are existing and fetch from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(user.getUserName(), fetchUser.getUserName());//check to see whether user exist in our database
    }

    @Test
    public void getUserAll() {
        try {
            Mockito.when(userService.findAll()).thenReturn((userListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.FetchUserDetails(userService), timeout);
            userList = (List<User>) Await.result(future, timeout.duration());//Akka playerActor tell imposed
            //Testing whether user all values are existing and fetch from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(userList.size(), is(greaterThanOrEqualTo(1)));//check to see whether fetch user exist in our database
    }

    @Test
    public void getUserCanUpdate() {
        //Inputs
        user1.setContactNo(1222222329);
        user.setContactNo(1222222329);
        try {
            Mockito.when(userService.updateUser(user)).thenReturn((user1));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.UpdateUserDetails(userService, user), timeout);
            assertNotNull(Await.result(future, timeout.duration()));//Akka playerActor tell imposed
            //Testing whether user values are existing and update from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void getUserHelpText() {
        HelpText helpText = new HelpText();
        try {
            Mockito.when(userService.fetchHelpText()).thenReturn((helpText));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(playerActor, new Messages.FetchHelpTextDetails(userService), timeout);
            helpText1 = (HelpText) Await.result(future, timeout.duration());//Akka playerActor tell imposed
            //Testing whether user help values are existing and fetch from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertNotNull(helpText1);//check to see whether Help Values exist in our database
    }

    @Test
    public void getUserWillDelete() {
        boolean val = true;
        try {
            Mockito.when(userService.deleteUser(user1.getEmail())).thenReturn((val));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(playerActor, new BrokerMessages.DeleteUserDetails(userService, user.getEmail()), timeout);
            assertTrue((String) Await.result(future, timeout.duration()), val);//Akka playerActor tell imposed
            //Testing whether user values are existing and delete from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}