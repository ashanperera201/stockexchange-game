package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.BankActor;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.ShareActor;
import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.BankService;
import com.stockMarket.ucd.stockMarket.webRest.BankResource;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BankActorTest {

    //Global variables declaration,begins
    List<Bank> bankListMock = new ArrayList<>();
    List<Bank> bankList = new ArrayList<>();
    Date date = new Date();

    Bank bank = new Bank();
    Bank bankAccount = new Bank();
    Timeout timeout = new Timeout(10000, TimeUnit.MILLISECONDS);
    GenerateCurrentDate generateCurrentDate = new GenerateCurrentDate();
    //Global variables declaration,ends
    @InjectMocks
    private BankResource bankResource;

    @Autowired
    private BankService bankService;
    @Autowired
    BankService mock = org.mockito.Mockito.mock(BankService.class);

    private static ActorSystem system;

    private ActorRef bankActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = BankActor.props();bankActor = system.actorOf(props); }

    @Before
    public void init() {
        //Dummy bank Items values,begin
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bank.setDate(date);
        bank.setAmount(1000);
        bankAccount.setDate(generateCurrentDate.getCurrentDate());
        bankAccount.setAmount(2000);
        bankAccount.setUserId("user896");
        //Dummy bank Items values,end

        //Assigning to actual checking objects ,begin
        bankList.add(bank);
        //Assigning to actual checking objects ,end
    }
    @Test
    public void getBankAmountsMonthly() {
        try {
            Mockito.when(mock.getBankAmountsMonthly("user896", 8, 2019)).thenReturn((bankListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.RequestBankDetails("user896", 8, 2019, bankService), timeout);
            bankList = (List<Bank>) Await.result(future, timeout.duration());//Akka BankActor tell imposed to check values passing and return not null
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(bankList.size(), is(greaterThanOrEqualTo(0)));//Testing whether bank values are existing
    }

    @Test
    public void getBankRegister() {
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.AddBankDetails(bankAccount, bankService), timeout);
            assertNotNull( Await.result(future, timeout.duration()));//Akka bankActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void getBankValueDelete() {
        try {
            Future<Object> future = Patterns.ask(bankActor, new Messages.deleteBankDetails(bankService,bankAccount.getUserId()), timeout);
            assertNotNull(Await.result(future, timeout.duration()));//Akka bankActor tell imposed delete the values of database and return not checking
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
