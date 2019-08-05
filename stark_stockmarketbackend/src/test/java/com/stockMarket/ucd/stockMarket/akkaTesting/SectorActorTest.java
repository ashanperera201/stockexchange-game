package com.stockMarket.ucd.stockMarket.akkaTesting;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.SectorActor;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;
import com.stockMarket.ucd.stockMarket.service.SectorService;
import com.stockMarket.ucd.stockMarket.webRest.SectorResource;
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
import java.io.IOException;
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
public class SectorActorTest {

    //Global variables declaration,begins
    List<Sector> sectorList = new ArrayList<>();
    List<Sector> mockSectorList = new ArrayList<>();
    Sector sector = new Sector();
    Sector mockSector = new Sector();
    List<Sector> SectorListReturn = new ArrayList<>();
    List<Sector> SectorList = new ArrayList<>();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    //Global variables declaration,ends
    @InjectMocks
    private SectorResource sectorResource;
    @Autowired
    private SectorService sectorService;

    private static ActorSystem system;

    private ActorRef sectorActor;

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
        final Props props = SectorActor.props();
        sectorActor = system.actorOf(props);
    }

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

        sector.setSectorId("S1");
        sector.setName("Financial");
        SectorListReturn.add(sector);
    }

    @Test
    public void getAllSectorsTest() {

        try {
            Mockito.when(sectorService.getAllSectors()).thenReturn(SectorListReturn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            //SectorList = sectorService.getAllSectors();
            Future<Object> future = Patterns.ask(sectorActor, new Messages.FetchSectorDetails(sectorService), timeout);
            sectorList = (List<Sector>) Await.result(future, timeout.duration());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(SectorListReturn.get(0).getSectorId(), sectorList.get(0).getSectorId());
    }

    @Test
    public void fetchSummarySectorTest() {

        List<SummarySector> SummarySectorListReturn = new ArrayList<>();
        List<SummarySector> SummarySectorList = new ArrayList<>();

        SummarySector sumSector = new SummarySector();
        sumSector.setSectorId("S1");
        sumSector.setName("Financial");
        sumSector.setTotalQuantity(100);
        sumSector.setTotalAmount(1000.0);
        try {

            Mockito.when(sectorService.fetchSummarySector()).thenReturn(SummarySectorListReturn);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        //summarySectorList = sectorService.fetchSummarySector();
        try {

            Future<Object> future = Patterns.ask(sectorActor, new Messages.FetchSectorDetails(sectorService), timeout);
            sectorList = (List<Sector>) Await.result(future, timeout.duration());

            assertThat(sectorList.size(), is(greaterThanOrEqualTo(1)));
        } catch (Exception ex) {

        }
    }
}