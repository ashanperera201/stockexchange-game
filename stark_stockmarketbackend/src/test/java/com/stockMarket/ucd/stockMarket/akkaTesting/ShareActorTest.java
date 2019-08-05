package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BrokerMessages;
import com.stockMarket.ucd.stockMarket.akka.ShareActor;
import com.stockMarket.ucd.stockMarket.domain.Share;
import com.stockMarket.ucd.stockMarket.domain.ShareItem;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.ShareService;
import com.stockMarket.ucd.stockMarket.webRest.ShareResource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.*;
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
public class ShareActorTest {
    //Global variables declaration,begins
    List<Share> shareListMock = new ArrayList<>();
    List<Share> shareList = new ArrayList<>();
    List<ShareItem> mockShareItemList = new ArrayList<>();
    List<ShareItem> shareItemList = new ArrayList<>();
    Share share = new Share();
    ShareItem shareItem = new ShareItem();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    GenerateCurrentDate generateCurrentDate = new GenerateCurrentDate();
    //Global variables declaration,ends
    @InjectMocks
    private ShareResource shareResource;
    @Autowired
    private ShareService shareService;

    private static ActorSystem system;

    private ActorRef shareActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = ShareActor.props();shareActor = system.actorOf(props); }

    @Before
    public void init() {
        //Dummy share values,begin
        shareItem.setMarketPrice(5);
        shareItem.setCompanyId("C2");
        shareItem.setShareId("S007");
        shareItem.setMarketDate(generateCurrentDate.getCurrentDate());
        shareItem.setCompanyId("C7");
        shareItem.setUserId("PkAdmin");
        //Dummy share values,end

        //Dummy share Items values,begin
        share.setQuantity(5);
        share.setCurrentPrice(5);
        share.setName("pk");
        share.setOriginalPrice(10);
        share.setCompanyId("C47");
        share.setShareId("S0078");
        share.setGainLoss(100.0);
        //Dummy share Items values,end

        //Assigning to actual checking objects ,begin
        shareList.add(share);
        mockShareItemList.add(shareItem);
        //Assigning to actual checking objects ,end
    }
    @Test
    public void getShareDetails() {
        try {
            Mockito.when(shareService.findAll()).thenReturn((shareListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.FetchShareDetails(shareService), timeout);
            shareList = (List<Share>) Await.result(future, timeout.duration());//Akka shareActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(shareList.size(), is(greaterThanOrEqualTo(1)));//Testing whether share values are existing
    }

    @Test
    public void getShareAmountsMonthly() {
        //Dummy variables declaration,begin
        String companyID,userId,startDate,endDate;
        companyID ="C7";
        userId    ="PkAdmin";
        startDate = "2019-08-03 00:00:00";
        endDate   = "2019-08-03 23:00:00";
        //Dummy variables declaration,end

        try {
            Mockito.when(shareService.getshareItems(companyID,userId,startDate,endDate)).thenReturn((mockShareItemList));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.RequestshareDetails(companyID, userId, startDate, endDate, shareService), timeout);
            shareItemList = (List<ShareItem>) Await.result(future, timeout.duration());//Akka shareActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(shareItemList.size(), is(greaterThanOrEqualTo(1)));//Testing whether share values are existing
        //assertEquals (mockShareItemList,(shareItemList));//Testing whether share values are equal
    }

    @Test
    public void getShareItemRegister() {
        //Dummy variables declaration,begin
        ShareItem result = new ShareItem();
        //Dummy variables declaration,end

        try {
            Mockito.when(shareService.getShareitems(shareItem)).thenReturn((shareItem));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.AddShareDetails(shareItem, shareService), timeout);
            result = (ShareItem) Await.result(future, timeout.duration());//Akka shareActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(result.getCompanyId(),shareItem.getCompanyId());//Testing whether share values are equal
    }

    @Test
    public void setDeleteShareItem() {
        //Dummy variables declaration,begin
        String shareId="S002";
        int dummyVal  = 0;
        int actualVal = 0;
        //Dummy variables declaration,end

        try {
            Mockito.when(shareService.deleteShareItem(shareId)).thenReturn((dummyVal));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(shareActor, new BrokerMessages.DeleteShareDetails(shareId, shareService), timeout);
            actualVal= (int) Await.result(future, timeout.duration());//Akka shareActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(actualVal, is(greaterThanOrEqualTo(1))); //Testing whether data deleted with return value 1
    }

}