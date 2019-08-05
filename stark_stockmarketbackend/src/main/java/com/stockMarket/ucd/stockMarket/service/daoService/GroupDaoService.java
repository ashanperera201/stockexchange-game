package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Group;

import java.util.List;

public interface GroupDaoService {

    void insertGroupsToDb(Group group);
    List<Group> getAllGroups();
    boolean isGroupExit(String groupName);
    List<Group> selectGroups(String groupName);
    void deleteGroups(String groupName);

    void updateGroupToDb(Group group);

}
