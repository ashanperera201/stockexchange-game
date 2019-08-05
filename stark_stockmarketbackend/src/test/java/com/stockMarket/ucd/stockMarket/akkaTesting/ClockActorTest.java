package com.stockMarket.ucd.stockMarket.akkaTesting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.akka.ClockActor;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.GroupService;
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
import com.stockMarket.ucd.stockMarket.webRest.GroupResource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClockActorTest {

    //Global variables declaration,begins
    List<Group> groupListMock = new ArrayList<>();
    List<Group> groupList = new ArrayList<>();
    Group group = new Group();
    Group group1= new Group();
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);
    //Global variables declaration,ends
    @InjectMocks
    private GroupResource groupResource;
    @Autowired
    private GroupService groupService;

    private static ActorSystem system;

    private ActorRef clockActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() { TestKit.shutdownActorSystem(system);system = null; }

    @Before
    public void setupSubject() { final Props props = ClockActor.props();clockActor = system.actorOf(props); }

    @Before
    public void init() {
        //Dummy group values,begin
        group.setUserName("PkAdmin");
        group.setPlayers(2);
        group.setUserId("user896");
        group.setGroupName("PkAdmin");

        //Dummy group values,end

        //Dummy group values,begin
        group1.setUserName("TeamStark");
        group1.setPlayers(2);
        group1.setUserId("user317");
        group1.setGroupName("PkAdmin");
        //Dummy group values,end

        //Assigning to actual checking objects ,begin
        groupList.add(group);
        groupListMock.add(group1);
        //Assigning to actual checking objects ,end
    }
    @Test
    public void addallGroups() {
        try {
            Mockito.when(groupService.getGroup(group1)).thenReturn((group1));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.AddGroupDetails(groupService,group), timeout);
            group  = (Group) Await.result(future, timeout.duration());//Akka clockActor tell imposed
            //Testing whether group values are existing and saved to database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(group.getGroupName(),group1.getGroupName());
    }
    @Test
    public void addvalueLookAll() {
        try {
            Mockito.when(groupService.getAll()).thenReturn((groupListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.FetchGroupDetails(groupService), timeout);
            groupList = (List<Group>) Await.result(future, timeout.duration());//Akka clockActor tell imposed
            //Testing whether group values are existing and saved to database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertThat(groupList.size(), is(greaterThanOrEqualTo(1)));//checking whether there is at least clock 1 based group user
    }


    @Test
    public void addvalueLookAllNotNull() {
        try {
            Mockito.when(groupService.getAll()).thenReturn((groupListMock));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {

            Future<Object> future = Patterns.ask(clockActor, new Messages.FetchGroupDetails(groupService), timeout);
            assertNotNull( Await.result(future, timeout.duration()));//Akka clockActor tell imposed
            //Testing whether group values are existing and fetch from database and return a not null object
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void finallydeleteGroup() {
        String delGroup="PkAdmin";
        boolean val = false;
        try {
            Mockito.when(groupService.deleteGroup(delGroup)).thenReturn((val));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.DeleteGroupDetails(groupService,delGroup), timeout);
            assertTrue((String)Await.result(future, timeout.duration()),val);//Akka clockActor tell imposed
            //Testing whether group of clock values are delete return a true
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //assertThat(groupList.size(), is(greaterThanOrEqualTo(1)));//checking whether there is atleast clock based group user
    }
}
