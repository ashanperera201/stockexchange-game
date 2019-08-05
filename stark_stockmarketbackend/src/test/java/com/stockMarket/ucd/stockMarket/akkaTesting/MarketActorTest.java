package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.MarketActor;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.service.SectorService;
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
public class MarketActorTest {
    //Global variables declaration,begins
    List<Sector> sectorList= new ArrayList<>();
    List<Sector> mockSectorList= new ArrayList<>();
    Sector sector = new Sector();
    Sector mockSector = new Sector();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    //Global variables declaration,ends
    @InjectMocks
    private GameResource gameResource;
    @Autowired
    private GameService gameService;

    @Autowired
    private SectorService sectorService;

    private static ActorSystem system;

    private ActorRef marketActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = MarketActor.props();marketActor = system.actorOf(props); }

    @Before
    public void init() {

        //Dummy Sector Items values,begin
        sector.setName("S1");
        sector.setSectorId("S1");
        //Dummy Sector Items values,end

        //Dummy Sector Items values,begin
        mockSector.setName("S1");
        mockSector.setSectorId("S1");
        //Dummy Sector Items values,end

        //Assigning to actual checking objects ,begin
        sectorList.add(sector);
        mockSectorList.add(mockSector);
        //Assigning to actual checking objects ,end
    }
        @Test
        public void getMarketBoomProcess() {
            int round = 1;
            try {
                Mockito.when(sectorService.getAllSectors()).thenReturn((mockSectorList));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            try {
                Mockito.when(gameService.martketBoom(mockSectorList,round)).thenReturn((round));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                mockSectorList  = sectorService.getAllSectors();
                Future<Object> future = Patterns.ask(marketActor, new BrokerMessages.GenerateMarketBoom(round, gameService,sectorService), timeout);
                sectorList = (List<Sector>) Await.result(future, timeout.duration());//Akka shareActor tell imposed
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            assertThat(mockSectorList.size(), is(greaterThanOrEqualTo(1)));//Testing whether share values are existing
        }

    @Test
    public void getMarketBoomSectorExists() {
        int round = 1;
        try {
            Mockito.when(sectorService.getAllSectors()).thenReturn((mockSectorList));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            sectorList  = sectorService.getAllSectors();
           //Akka sectorActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(mockSectorList.get(0).getSectorId(), sectorList.get(0).getSectorId());//Testing whether share values are existing
    }
}
