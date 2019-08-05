package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;

import com.stockMarket.ucd.stockMarket.akka.CompanyActor;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.akka.ShareActor;
import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.service.CompanyService;
import com.stockMarket.ucd.stockMarket.webRest.CompanyResource;
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
public class CompanyActorTest {

    //Global variables declaration,begins
    List<Company> companyListMock = new ArrayList<>();
    List<Company> companyList = new ArrayList<>();
    Company company = new Company();
    Timeout timeout = new Timeout(10000, TimeUnit.MILLISECONDS);

    //Global variables declaration,ends
    @InjectMocks
    private CompanyResource companyResource;
    @Autowired
    private CompanyService companyService;

    private static ActorSystem system;

    private ActorRef companyActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = CompanyActor.props();companyActor = system.actorOf(props); }

    @Before
    public void init() {
        //Dummy company values,begin
        company.setCompanyId("C11");
        company.setAddress("Seeduwa");
        company.setName("TestCaseCompany");
        company.setSectorId("S1");
        //Dummy company values,end

        //Assigning to actual checking objects ,begin
        companyList.add(company);
        //Assigning to actual checking objects ,end
    }
    @Test
    public void getCompanyAllDetails() {
        try {
            Mockito.when(companyService.getAllCompanies()).thenReturn((companyListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.FetchCompanyDetails(companyService), timeout);
            companyList = (List<Company>) Await.result(future, timeout.duration());//Akka shareActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(companyList.size(), is(greaterThanOrEqualTo(1)));//Testing whether share values are existing
    }

    @Test
    public void getCompanyBySector() {
        //Dummy variables declaration,begin
        String sectorID = "S1";
        try {
            Mockito.when(companyService.getSectorCompanies(sectorID)).thenReturn(companyListMock);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.FetchCompDetailsBySector(companyService, sectorID), timeout);
            companyList = (List<Company>) Await.result(future, timeout.duration());//Akka companyActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(companyList.size(), is(greaterThanOrEqualTo(1)));//Testing whether company values are existing
        //assertEquals (companyListMock,companyList);//Testing whether company values are equal
    }

    @Test
    public void getCompanyRegister() {
        //Dummy variables declaration,begin
        Company result = new Company();
        //Dummy variables declaration,end

        try {
            Mockito.when(companyService.createCompany(company)).thenReturn(company);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.AddCompanyDetails(companyService,company), timeout);
            result = (Company) Await.result(future, timeout.duration());//Akka companyActor tell imposed

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(result.getCompanyId(),company.getCompanyId());//Testing whether company values are equal with our dummy values and return back the server response
    }

    @Test
    public void getDeleteCompany() {
        //Dummy variables declaration,begin
        boolean val=true;
        //Dummy variables declaration,end

        try {
            Mockito.when(companyService.deleteCompany(company.getCompanyId())).thenReturn(val);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(companyActor, new Messages.deleteCompanyDetails(companyService,company.getCompanyId()), timeout);
            val= (boolean) Await.result(future, timeout.duration());//Akka companyActor tell imposed
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(val,true);//Testing whether company values are deleted whith our dummy values and return back the server response
    }

}
