package com.stockMarket.ucd.stockMarket.webRest;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.stockMarket.ucd.stockMarket.StockMarketApplication;
import com.stockMarket.ucd.stockMarket.akka.ClockActor;
import com.stockMarket.ucd.stockMarket.akka.Messages;
import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.service.GroupService;
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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Group Resource REST Endpoint", description = "Shows the Group info")
public class GroupResource {
    private final Logger log = LoggerFactory.getLogger(GroupService.class);
    private ActorRef clockActor;

    public GroupResource() {
        clockActor = StockMarketApplication.system.actorOf(ClockActor.props());
    }

    @Autowired
    GroupService groupservice;
    Timeout timeout = new Timeout(100000000, TimeUnit.MILLISECONDS);

    @PostMapping("/saveGroup")
    public ResponseEntity<Group> getGroupRegister(@Valid @RequestBody Group group) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to save Group : {}", group);
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.AddGroupDetails(groupservice,group), timeout);
            Group result  = (Group) Await.result(future, timeout.duration());
          //  Group result = groupservice.getGroup(group);
            return ResponseEntity.created(new URI("/saveGroup" + ( result).getGroupName()))
                    .headers(HeaderUtil.createEntityCreationAlert(group.getGroupName(),group.getUserId()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @GetMapping("/groups-all")
    public ResponseEntity<List<Group>> getAll () throws IOException {
        List<Group> groupList=null;
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.FetchGroupDetails(groupservice), timeout);
            groupList = (List<Group>) Await.result(future, timeout.duration());
//            groupList = groupservice.getAll();

        } catch (Exception e) {
            System.out.println("Error:   " + e.getMessage());
        }
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity<Group> deleteGroup(String group) throws URISyntaxException, ParseException, Exception {
        log.info("REST request to delete Group : {}", group);
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.DeleteGroupDetails(groupservice,group), timeout);
            boolean val= (boolean) Await.result(future, timeout.duration());
            //groupservice.deleteGroup(group);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/updateGroup")
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) throws Exception {
        log.info("REST request to save Company : {}", group);
        try {
            Future<Object> future = Patterns.ask(clockActor, new Messages.UpdateGroupDetails(groupservice,group), timeout);
            Group result = (Group)Await.result(future, timeout.duration());
            //Group result = groupservice.updateGroup(group);
            return ResponseEntity.created(new URI("/api/updateGroup/" + result.getGroupName()))
                    .headers(HeaderUtil.createEntityCreationAlert(group.getGroupName(), result.getUserId()))
                    .body(result);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
