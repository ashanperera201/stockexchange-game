package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BrokerActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.service.GroupService;
import com.stockMarket.ucd.stockMarket.webRest.GameResource;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BrokerActorTest {

    //Global variables declaration,begins
    List<Game> gameListMock = new ArrayList<>();
    List<Game> gameList = new ArrayList<>();
    Game game = new Game();
    Game mockGame = new Game();
    UserPortfolio userPortfolio= new UserPortfolio();
    UserPortfolio mockuserPortfolio =new UserPortfolio();
    Calculator calc = new Calculator();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    GenerateCurrentDate generateCurrentDate = new GenerateCurrentDate();
    //Global variables declaration,ends
    @InjectMocks
    private GameResource gameResource;
    @Autowired
    private GameService gameService;

    @Autowired
    private GroupService groupService;

    private static ActorSystem system;

    private ActorRef brokerActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = BrokerActor.props();brokerActor = system.actorOf(props); }

    @Before
    public void init() {
        //Dummy Game Items values,begin
        game.setEmail("kanth.praveenpk@gmail.com");
        game.setUserName("UserKanth");
        game.setInitialValue(100.00);
        game.setGroupName("User");
        game.setPhotoURL("dummy");
        game.setAmount(100);
        game.setRoundNo(1);
        game.setUserId("User21");
        //Dummy Game Items values,end

        //Dummy Game mockItems values,begin
        mockGame.setEmail("kanth.praveenpk@gmail.com");
        mockGame.setUserName("PkKanth");
        mockGame.setInitialValue(100.00);
        mockGame.setGroupName("User");
        mockGame.setPhotoURL("dummy");
        mockGame.setAmount(100);
        mockGame.setRoundNo(1);
        mockGame.setUserId("PkAdmin");
        //Dummy Game mockItems values,end

        //Adding Calculator values, begin
        calc.setQuantity(5);
        calc.setAmount(100);
        calc.setBuyDate(generateCurrentDate.getCurrentDate());
        calc.setPurchComm(10);
        calc.setPurchPrice(5);
        calc.setSellDate(generateCurrentDate.getCurrentDate());
        calc.setSoldComm(10);
        calc.setSoldPrice(100);
        //Adding Calculator values, end

        //Adding Portfolio values, begin
        userPortfolio.setProfitLoss(0);
        userPortfolio.setTotalAmount(0);
        userPortfolio.setCompanyId("C1");
        userPortfolio.setUserName("PkKanth");
        userPortfolio.setUserId("PkAdmin");
        userPortfolio.setType("BUY");
        userPortfolio.setMarketPrice(10);
        userPortfolio.setQuantity(10);
        userPortfolio.setAccBalance(1000.00);
        userPortfolio.setCompanyName("Rolex");

        mockuserPortfolio.setProfitLoss(0);
        mockuserPortfolio.setTotalAmount(0);
        mockuserPortfolio.setCompanyId("C2");
        mockuserPortfolio.setUserName("PkKanth");
        mockuserPortfolio.setUserId("PkAdmin");
        mockuserPortfolio.setType("BUY");
        mockuserPortfolio.setMarketPrice(10);
        mockuserPortfolio.setQuantity(10);
        mockuserPortfolio.setAccBalance(1000.00);
        mockuserPortfolio.setCompanyName("Rolexwer");
        //Adding Portfolio values, end

        gameListMock.add(mockGame);
        gameList.add(game);

    }

    @Test
    public void getGameRegister() {
        boolean val=true;
        List<Game> result=null;
        try {
            Mockito.when(gameService.getGame(gameListMock)).thenReturn((gameListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Mockito.when(groupService.deleteGroup(gameListMock.get(0).getGroupName())).thenReturn((val));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GameRegister(gameList, gameService,groupService), timeout);
            result = (List<Game>) Await.result(future, timeout.duration());//Akka brokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(result.size(), is(greaterThanOrEqualTo(1)));//Testing whether Game values are existing
    }
    @Test
    public void getCalculator() {
        Calculator calcVal = null;
        try {
            Mockito.when(gameService.getCalc(calc)).thenReturn((calc));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GetCalculator(calc, gameService), timeout);
            calcVal = (Calculator) Await.result(future, timeout.duration());//Akka brokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(Integer.valueOf((int)calcVal.getTotalInvest()),Integer.valueOf((int)calc.getTotalInvest()));//Testing whether Calculator values are existing
    }
    @Test
    public void getCalculatorSame() {
        Calculator calcVal  = null;
        try {
            Mockito.when(gameService.getCalc(calc)).thenReturn((calc));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.GetCalculator(calc, gameService), timeout);
            calcVal = (Calculator) Await.result(future, timeout.duration());//Akka brokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(Integer.valueOf((int) calcVal.getTotalInvest()), is(greaterThanOrEqualTo(1)));//Testing whether Game values are existing

    }
    @Test
    public void setGameRegisterFolio() {
        try {
            Mockito.when(gameService.setUserPortfolio(mockuserPortfolio)).thenReturn((mockuserPortfolio));
        } catch (Exception ex) {
            System.out.println(ex.getMessage()); }
        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.AddUserPortfolioDetails(userPortfolio, gameService), timeout);
            userPortfolio = ( UserPortfolio) Await.result(future, timeout.duration());//Akka BrokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage()); }
        assertEquals(userPortfolio.getUserId(),mockuserPortfolio.getUserId());//Testing whether portfolio same
    }
    @Test
    public void getUserPotfolioData() {
        String id="User21";
        List<UserPortfolio> UserPortList = null;
        try {
            Mockito.when(gameService.getUserPortfolio(id)).thenReturn((UserPortList));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.FetchUserPortfolioDetails(id, gameService), timeout);
            UserPortList = ( List<UserPortfolio>) Await.result(future, timeout.duration());//Akka BrokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(UserPortList.size(),is(greaterThanOrEqualTo(0)));//Testing whether portfolio exists
    }


    @Test
    public void setobsoleteGame() {
        String bot="User";
        int val=0;
        try {
            Mockito.when(gameService.deletGame(bot)).thenReturn((val));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            Future<Object> future = Patterns.ask(brokerActor, new BrokerMessages.DeleteGame(gameService,bot), timeout);
            val = (int) Await.result(future, timeout.duration());//Akka BrokerActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(val,is(greaterThanOrEqualTo(1)));//Testing whether Game Removed
    }
}
