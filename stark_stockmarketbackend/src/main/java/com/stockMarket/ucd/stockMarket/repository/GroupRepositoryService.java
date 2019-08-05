package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.service.GroupService;
import com.stockMarket.ucd.stockMarket.service.daoService.GroupDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@Repository("mysql1")
public class GroupRepositoryService implements GroupDaoService {
    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class GroupRowMapper implements RowMapper<Group> {

        @Override
        public Group mapRow(ResultSet resultSet, int i) throws SQLException {
            Group group = new Group();
            group.setGroupName(resultSet.getString("groupName"));
            group.setUserId(resultSet.getString("userId"));
            group.setPlayers(resultSet.getInt("players"));
            group.setUserName(resultSet.getString("userName"));
            return group;
        }
    }

    @Override
    public List<Group> getAllGroups() {
        List<Group> groupList= null;
        try {
            final String sql = "SELECT  p.*,u.userName FROM stark.playergroup p , users u where u.userId = p.userId";
            groupList = jdbcTemplate.query(sql, new GroupRepositoryService.GroupRowMapper());
            return groupList;
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ Fetch group Repository @@@@@@@@@@" + ex);
        }
        return groupList;
    }

    @Override
    public List<Group> selectGroups(String groupName) {

        final String sql = "SELECT * FROM users WHERE groupName=?";
        List<Group> groupList = jdbcTemplate.query(sql, new GroupRepositoryService.GroupRowMapper());
        return groupList;
    }
    @Override
    public void insertGroupsToDb(Group group) {
        try {
            if (group.getGroupName() != null) {
                final String sql = "INSERT INTO playergroup (groupName,userId,players) VALUES (?,?,?)";
                final String groupName = group.getGroupName();
                final String userId = group.getUserId();
                final int players = group.getPlayers();
                jdbcTemplate.update(sql, new Object[]{groupName, userId, players});
            }

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ group Repository @@@@@@@@@@" + e);
        }

    }
    @Override
    public boolean isGroupExit(String groupName) {
        boolean result = false;
        final String sql = "SELECT count(*) FROM playergroup WHERE groupName = ?";
        int count = (Integer) jdbcTemplate.queryForObject(sql, new Object[]{groupName}, Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }
    @Override
    public  void deleteGroups(String groupName)
    {
        final String sql = "DELETE FROM playergroup WHERE groupName = ?";
        jdbcTemplate.update(sql, new Object[]{groupName});
    }

    @Override
    public void updateGroupToDb(Group group) {
        try {
            if (group.getGroupName() != null) {
                final String sql = "UPDATE playergroup set userId=?,players=? where groupName =?";;
                final String groupName = group.getGroupName();
                final String userId = group.getUserId();
                final int players = group.getPlayers();
                jdbcTemplate.update(sql, new Object[]{userId,players, groupName});
            }

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ group Repository @@@@@@@@@@" + e);
        }

    }


}
