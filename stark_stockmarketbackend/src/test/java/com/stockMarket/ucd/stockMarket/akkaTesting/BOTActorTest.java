package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.AIBotActor;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.domain.Game;
import com.stockMarket.ucd.stockMarket.service.AIBotService;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.webRest.AIBotResource;
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
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BOTActorTest {
    //Global variables declaration,begins
    Game game = new Game();
    Game mockGame = new Game();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    //Global variables declaration,ends

    @InjectMocks
    private AIBotResource aiBotResource;
    @Autowired
    AIBotService aiBotService;
    @Autowired
    GameService gameService;
    private static ActorSystem system;

    private ActorRef AIbotActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = AIBotActor.props();AIbotActor = system.actorOf(props); }

    @Before
    public void init() {

        //Dummy Game Items values,begin
        game.setEmail("kanth.praveenpk@gmail.com");
        game.setUserName("PkKanth");
        game.setInitialValue(100.00);
        game.setGroupName("PkAdminBOT");
        game.setPhotoURL("dummy");
        game.setAmount(100);
        game.setRoundNo(1);
        game.setUserId("PkAdmin");
        //Dummy Game Items values,end

        //Dummy Game mockItems values,begin
        mockGame.setEmail("kanth.praveenpk@gmail.com");
        mockGame.setUserName("PkKanth");
        mockGame.setInitialValue(100.00);
        mockGame.setGroupName("PkAdminBOT");
        mockGame.setPhotoURL("dummy");
        mockGame.setAmount(100);
        mockGame.setRoundNo(1);
        mockGame.setUserId("PkAdmin");
        //Dummy Game mockItems values,end
    }

    @Test
    public void getAddBotGame() {
        Integer val=1;
        try {
            Mockito.when(aiBotService.getGame(mockGame)).thenReturn((mockGame));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(AIbotActor, new BrokerMessages.AddBotPlayergame(aiBotService, mockGame), timeout);
            mockGame=(Game) Await.result(future, timeout.duration());//Akka BOTActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(mockGame.getUserId(),game.getUserId());//Testing whether BOT game values are same
    }

    @Test
    public void getObseleteBotGame() {
        String bot="PkAdminBOT";
        boolean botReturn =true;
        int val=0;
        try {
            Mockito.when(aiBotService.obseleteAIBot(bot)).thenReturn((botReturn));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            Mockito.when(gameService.deletGame(bot)).thenReturn((val));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(AIbotActor, new BrokerMessages.DeleteBotPlayergame(aiBotService, bot), timeout);
            botReturn=(boolean) Await.result(future, timeout.duration());//Akka BOTActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(botReturn,true);//Testing whether BOT Vanished
    }

}
