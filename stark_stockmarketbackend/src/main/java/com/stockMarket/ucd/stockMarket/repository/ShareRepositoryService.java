package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.domain.Share;
import com.stockMarket.ucd.stockMarket.domain.ShareItem;
import com.stockMarket.ucd.stockMarket.other.GenerateCurrentDate;
import com.stockMarket.ucd.stockMarket.service.AIBotService;
import com.stockMarket.ucd.stockMarket.service.CompanyService;
import com.stockMarket.ucd.stockMarket.service.ShareService;
import com.stockMarket.ucd.stockMarket.service.daoService.ShareDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository("mysqlSRP")
public class ShareRepositoryService implements ShareDaoService {

    private final Logger log = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    CompanyService companyService;

    public static class ShareRowMapper implements RowMapper<Share> {

        @Override
        public Share mapRow(ResultSet resultSet, int i) throws SQLException {
            Share share = new Share();
            share.setShareId(resultSet.getString("shareId"));
            share.setCompanyId(resultSet.getString("companyId"));
            share.setOriginalPrice(resultSet.getInt("originalPrice"));
            share.setCurrentPrice(resultSet.getDouble("currentPrice"));
            share.setQuantity(resultSet.getInt("quantity"));
            share.setName(resultSet.getString("name"));
            return share;
        }
    }

    public static class  AnlaysisShareRowMapper implements RowMapper<Share>
    {
        @Override
        public Share mapRow(ResultSet resultSet, int i) throws SQLException {
            Share share = new Share();
            share.setShareId(resultSet.getString("shareId"));
            share.setCompanyId(resultSet.getString("companyId"));
            share.setOriginalPrice(resultSet.getInt("originalPrice"));
            share.setCurrentPrice(resultSet.getDouble("currentPrice"));
            share.setQuantity(resultSet.getInt("quantity"));
            share.setName(resultSet.getString("name"));
            share.setGainLoss(resultSet.getDouble("gainLoss"));
            return share;
        }
    }

    private static class ShareItemsRowMapper implements RowMapper<ShareItem> {

        @Override
        public ShareItem mapRow(ResultSet resultSet, int i) throws SQLException {
            ShareItem shareItem = new ShareItem();
            shareItem.setShareId(resultSet.getString("shareId"));
            shareItem.setCompanyId(resultSet.getString("companyId"));
            shareItem.setUserId(resultSet.getString("userId"));
            shareItem.setMarketDate(resultSet.getTimestamp("marketDate"));
            shareItem.setMarketPrice(resultSet.getInt("marketPrice"));
            return shareItem;
        }
    }

    @Override
    public List<Share> getAllShares() {

        List<Share> shareList = null;
        try {
            final String sql = "SELECT s.*,c.name FROM share s , company c where c.companyId=s.companyId";
            shareList = jdbcTemplate.query(sql, new ShareRepositoryService.ShareRowMapper());
            return shareList;
        }
        catch
        (Exception ex) {
            System.out.println(log);
        }
        return  shareList;
    }

    @Override
    public  List<ShareItem> getAllShareItems(String Company, String userId, String startDate, String endDate)
    {
        List<ShareItem> shareItems= null;
        try {
            final String sql = "SELECT * FROM stark.shareItems  where companyId=? and marketDate between ? AND ?";
            shareItems = jdbcTemplate.query(sql,new Object[]{Company,startDate,endDate}, new ShareItemsRowMapper());
            return shareItems;
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ Fetch ShareItems Repository @@@@@@@@@@" + ex.getMessage());
        }
        return shareItems;
    }
    @Override
    public void insertShareItemToDb(ShareItem shareItem) {
        try {
                GenerateCurrentDate timeDate= new GenerateCurrentDate();
                final String sql = "INSERT INTO shareItems (shareId,companyId,userId,marketDate,marketPrice) VALUES (?,?,?,?,?)";
                final String shareId = shareItem.getShareId();
                final String companyId = shareItem.getCompanyId();
                final String userId = shareItem.getUserId();
                final Date marketDate = timeDate.getCurrentDateTime();
                final int marketPrice= shareItem.getMarketPrice();
                jdbcTemplate.update(sql, new Object[]{shareId, companyId, userId,marketDate,marketPrice});


        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ share Repository @@@@@@@@@@" + e);
        }

    }
    @Override
    public void deleteShareItemToDb(String id)
    {
        try {
            final String sql = "delete from shareItems where shareId=id";
            jdbcTemplate.update(sql, new ShareRepositoryService.ShareItemsRowMapper());
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ share Repository @@@@@@@@@@" + e);
        }
    }
    @Override
    public void updateSharesToDb(Share share)
    {
        try {
            final String sql = "UPDATE share set quantity =? where companyId=?";
            final int quantity = share.getQuantity();
            final String companyId = share.getCompanyId();
            jdbcTemplate.update(sql, new Object[]{quantity, companyId});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ share Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public void setValues(String sector , int mp, int random) {
        try {
            List<Company> list=companyService.getSectorCompanies(sector);
            for(int i=0;i<list.size();i++) {
                final String sql1 = "SELECT s.*,c.name FROM share s , company c where c.companyId=? and s.companyId=c.companyId";
                Share share = jdbcTemplate.queryForObject(sql1,new Object[]{list.get(i).getCompanyId()}, new ShareRepositoryService.ShareRowMapper());
                GenerateCurrentDate timeDate = new GenerateCurrentDate();
                final String sql = "INSERT INTO shareItems (shareId,companyId,userId,marketDate,marketPrice) VALUES (?,?,?,?,?)";
                final String companyId = list.get(i).getCompanyId();
                final String userId =  "Stark";
                final String shareId = share.getShareId();
                final Date marketDate = timeDate.getCurrentDateTime();
                final int marketPrice = (int) (mp+share.getCurrentPrice()+random);
                jdbcTemplate.update(sql, new Object[]{shareId, companyId, userId, marketDate, marketPrice});
            }

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ share Repository @@@@@@@@@@" + e);
        }

    }

}
