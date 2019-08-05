package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.service.daoService.GroupDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GroupService {
    private final Logger log = LoggerFactory.getLogger(GroupService.class);
    @Autowired
    @Qualifier("mysql1")
    private GroupDaoService groupDaoService;

    public Group getGroup(Group group)  {

        //List<Group> selectGroups =groupDaoService.selectGroups(group.getGroupName());
        //if(selectGroups!=null){
            this.groupDaoService.insertGroupsToDb(group);
            return group;
       // }
       // return group;
    }
    public List<Group> getAll() throws IOException {
        List<Group>results = this.groupDaoService.getAllGroups();
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@"+results);
        return results;
    }

    public boolean deleteGroup (String groupName)  {

        groupDaoService.deleteGroups(groupName);
        return  true;

    }

    public Group updateGroup(Group group)  {
        this.groupDaoService.updateGroupToDb(group);
        return group;
    }
}
