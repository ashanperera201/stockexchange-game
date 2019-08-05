package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.AIBotService;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.service.ShareService;
import com.stockMarket.ucd.stockMarket.service.daoService.GameDaoService;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("mysqlGS")
public class GameRepositoryService implements GameDaoService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AIBotService aiBotService;

    @Autowired
    ShareService shareService;

    public static class UserPortfolioRowMapper implements RowMapper<UserPortfolio> {

        @Override
        public UserPortfolio mapRow(ResultSet resultSet, int i) throws SQLException {
            UserPortfolio user = new UserPortfolio();
            user.setUserId(resultSet.getString("userId"));
            user.setUserName(resultSet.getString("userName"));
            user.setCompanyId(resultSet.getString("companyId"));
            user.setCompanyName(resultSet.getString("name"));
            user.setQuantity(resultSet.getInt("quantity"));
            user.setMarketPrice(resultSet.getDouble("marketPrice"));
            user.setTotalAmount(resultSet.getDouble("totalAmount"));
            user.setProfitLoss(resultSet.getDouble("profitLoss"));
            return user;
        }
    }

    public static class GameRowMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet resultSet, int i) throws SQLException {
            Game game = new Game();
            game.setGroupName(resultSet.getString("groupName"));
            game.setUserId(resultSet.getString("userId"));
            game.setRoundNo(resultSet.getInt("roundNo"));
            game.setAmount(resultSet.getInt("amount"));
            game.setPhotoURL(resultSet.getString("photoURL"));
            game.setInitialValue(resultSet.getDouble("initialValue"));
            game.setUserName(resultSet.getString("userName"));
            game.setEmail(resultSet.getString("email"));
            return game;
        }
    }

    @Override
    public void insertGamesToDb(List<Game> game)
    {
        Bank bankList= null;
        try {

            for(int i =0; i< game.size();i++) {
                final String  dumysql= "SELECT amount, transDate FROM stark.bank where userId=? and countNo= (select max(countNo) FROM stark.bank where userId=?) ";
                bankList = jdbcTemplate.queryForObject(dumysql,new Object[]{game.get(i).getUserId(),game.get(i).getUserId()}, new BankRepositoryService.BankRowMapper());
                final String sql = "INSERT INTO game (groupName,userId,roundNo,amount,photoURL,initialValue) VALUES (?,?,?,?,?,?)";
                final String groupName = game.get(i).getGroupName();
                final String userId = game.get(i).getUserId();
                final int round = 1;
                final int amount = game.get(i).getAmount();
                final String image = game.get(i).getPhotoURL();
                jdbcTemplate.update(sql, new Object[]{groupName, userId, round, amount, image,bankList.getAmount()});
            }

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Game Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public Triplet<List<Company>, List<Sector>,Bank> getBasicData( String userId) {
        List<Company> companyList= null;
        List<Sector> sectorList= null;
        Bank bankList= null;
        try {
            final String sqlCompany = "SELECT * FROM company ";
            companyList = jdbcTemplate.query(sqlCompany, new CompanyRepositoryService.CompanyRowMapper());

            final String sqlSector = "SELECT * FROM sector";
            sectorList = jdbcTemplate.query(sqlSector, new SectorRepositoryService.SectorRowMapper());

            final String sqlBank = "SELECT amount, transDate FROM stark.bank where userId=? and countNo= (select max(countNo) FROM stark.bank where userId=?) ";
            bankList = jdbcTemplate.queryForObject(sqlBank,new Object[]{userId,userId}, new BankRepositoryService.BankRowMapper());

            org.javatuples.Triplet<List<Company>, List<Sector>,Bank> TripleVal = org.javatuples.Triplet.with(companyList,sectorList,bankList);

            return TripleVal;
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ Fetch Game Repository @@@@@@@@@@" + ex);
        }
        return null;
    }
    @Override
    public Calculator calculateProfit(Calculator calc)
    {
        calc.setTotalInvest(((calc.getPurchPrice()* calc.getQuantity())+calc.getPurchComm()));
        calc.setGrossReturn(((calc.getSoldPrice()*calc.getQuantity())-calc.getSoldComm()));
        calc.setProfit(calc.getGrossReturn()-calc.getTotalInvest());
        return  calc;
    }

    @Override
    public List<UserPortfolio> getUserPortfolio(String userId)
    {
        final String sql = "SELECT c.name, u.* , s.userName FROM userPortfolio u ,company c , users s where c.companyId = u.companyId And u.userId=? And s.userId=u.userId";
        List<UserPortfolio> userPortList = jdbcTemplate.query(sql, new Object[]{userId},new UserPortfolioRowMapper());
        return userPortList;
    }

    @Override
    public boolean isUserPortfolioExit(String companyId,String userId)
    {
        //dont delete this code
        boolean result = false;
        final String sqldummy = "SELECT count(*) FROM userPortfolio WHERE companyId = ? and userId=?";
        int count = (Integer) jdbcTemplate.queryForObject(sqldummy, new Object[]{companyId,userId}, Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public UserPortfolio modifyUserPortFolio(UserPortfolio userPortfolio)
    {
        try {
            //Logic for User portfolio Don't not change
            if (isUserPortfolioExit(userPortfolio.getCompanyId(),userPortfolio.getUserId())) {
                String sql=null;
                if(userPortfolio.getType().equalsIgnoreCase("Buy")) {
                    sql = "UPDATE userPortfolio SET quantity=quantity + ? ,marketPrice =? ,totalAmount=(totalAmount+ (?*marketPrice)), accountBalance = ? where companyId=? and userId=?";
                }
                else if (userPortfolio.getType().equalsIgnoreCase("Sell")) {
                    sql = "UPDATE userPortfolio SET quantity=quantity - ? ,marketPrice =? ,profitLoss=profitLoss + ((accountBalance + (?*marketPrice))- accountBalance), accountBalance =? where companyId=?and userId=?";
                }
                final String userId = userPortfolio.getUserId();
                final String companyId = userPortfolio.getCompanyId();
                final int quantity = userPortfolio.getQuantity();
                final double marketPrice=userPortfolio.getMarketPrice();
                final double profitLoss = userPortfolio.getProfitLoss();
                final  double totalAmount=userPortfolio.getTotalAmount();
                if(userPortfolio.getType().equalsIgnoreCase("Buy")) {
                    double checkVal = (userPortfolio.getAccBalance() - (userPortfolio.getQuantity() * userPortfolio.getMarketPrice()));
                    if (checkVal <= 0) {
                        checkVal = 0;
                        userPortfolio.setAccBalance(checkVal);
                    } else {
                        userPortfolio.setAccBalance(checkVal);
                    }
                    //Calculating Account Balance ,begin
                    final double accBalance  = userPortfolio.getAccBalance();
                    jdbcTemplate.update(sql, new Object[]{quantity, marketPrice,quantity,accBalance,companyId,userId});}
                else if (userPortfolio.getType().equalsIgnoreCase("Sell")) {
                    userPortfolio.setAccBalance(userPortfolio.getAccBalance() + (userPortfolio.getQuantity() * userPortfolio.getMarketPrice()));
                    //Calculating Account Balance ,end
                    final double accBalance  = userPortfolio.getAccBalance();
                    jdbcTemplate.update(sql, new Object[]{quantity, marketPrice,quantity,accBalance,companyId,userId});
                }
            }
            else {

                if(userPortfolio.getType().equalsIgnoreCase("Buy")) {//if any error in buy and sell look at this
                    final String sql = "INSERT INTO userPortfolio (userId,companyId,quantity,marketPrice,totalAmount,profitLoss,accountBalance) VALUES (?,?,?,?,quantity*marketPrice,?,?)";
                    final String useId = userPortfolio.getUserId();
                    final String companyId = userPortfolio.getCompanyId();
                    final int quantity = userPortfolio.getQuantity();
                    final double marketPrice = userPortfolio.getMarketPrice();
                    final double profitLoss = userPortfolio.getProfitLoss();
                    final double totalAmount = userPortfolio.getTotalAmount();

                    //Calculating Account Balance ,Begin
                    if (userPortfolio.getType().equalsIgnoreCase("Buy")) {
                        double checkVal = (userPortfolio.getAccBalance() - (userPortfolio.getQuantity() * userPortfolio.getMarketPrice()));
                        if (checkVal <= 0) {
                            checkVal = 0;
                            userPortfolio.setAccBalance(checkVal);
                        } else {
                            userPortfolio.setAccBalance(checkVal);
                        }
                    } else if (userPortfolio.getType().equalsIgnoreCase("Sell")) {
                        userPortfolio.setAccBalance(userPortfolio.getAccBalance() + (userPortfolio.getQuantity() * userPortfolio.getMarketPrice()));
                    }
                    //Calculating Account Balance ,End
                    final double accBalance = userPortfolio.getAccBalance();
                    jdbcTemplate.update(sql, new Object[]{useId, companyId, quantity, marketPrice, profitLoss, accBalance});
                }
                else
                {
                    System.out.println("");
                }
            }

        } catch (Exception ex) {
            log.info("log info @@@@@@@@@@ UserPortFolio modify in Game Repository @@@@@@@@@@" + ex);
        }
        return  userPortfolio;
    }

    @Override
    public void updateGame(Game game) {
        try {
            final String sql = "UPDATE game set roundNo=? ,amount =? where userId =? and groupName=?";
            final int roundNo = game.getRoundNo();
            final double amount = game.getAmount();
            final String userId = game.getUserId();
            final String groupName = game.getGroupName();
            jdbcTemplate.update(sql, new Object[]{roundNo, amount, userId, groupName});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Game Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public List<Game> getGameSummmary(String groupName) {
        List<Game> gameList= null;
        try {
            final String sql = "SELECT h.*,u.userName,u.email FROM game h , users u where u.userId=h.userId and groupName=? order by (h.amount-h.initialValue) DESC ";
            gameList = jdbcTemplate.query(sql,new Object[]{groupName}, new GameRepositoryService.GameRowMapper());
            return  gameList;
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ Fetch Game Repository @@@@@@@@@@" + ex);
        }
        return null;
    }

    @Override
    public void updateMarketValue(String sector , int marketValue, int randomRoundValue,int ratio) {
        try {
            final String sql = "update share s, company c  set s.currentPrice=s.currentPrice+?+?\n" +
                    "where c.sectorId = ? and s.companyId=c.companyId;\n";
            jdbcTemplate.update(sql, new Object[]{marketValue, randomRoundValue, sector});
            shareService.addShareItem(sector,marketValue,randomRoundValue);//shareItems insertion
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Game Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public List<Share> fetchSectorValues() {
        List<Share> shareList=null;
        try {
            final String sql = "select s.*,c.name from share s , company c \n" +
                    "where s.companyId=c.companyId and c.sectorId IN (select sectorId from sector)";
            shareList = jdbcTemplate.query(sql, new ShareRepositoryService.ShareRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetchSectorValues Repository @@@@@@@@@@" + ex);
        }

        return shareList;
    }

    @Override
    public List<Company> fetchCompanyValuesPerSector(String sector) {
        List<Company> companyList=null;
        try {
            final String sql = "select c.* from company c where c.sectorId = ?";
            companyList = jdbcTemplate.query(sql, new Object[]{sector}, new CompanyRepositoryService.CompanyRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetch Company Values Per Sector Repository @@@@@@@@@@" + ex);
        }

        return companyList;
    }

    @Override
    public List<Share> fetchCompanyValues() {
        List<Share> shareList=null;
        try {
            final String sql = "select s.*,c.name from share s, company c where s.companyId=c.companyId";
            shareList = jdbcTemplate.query(sql, new ShareRepositoryService.ShareRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetchCompanyValues Repository @@@@@@@@@@" + ex);
        }

        return shareList;
    }

    @Override
    public List<Share> fetchTopCompanyGainers() {
        List<Share> topCompanyList = null;
        try{
            final String sql="select s.*,c.name,(s.currentPrice-originalPrice) as gainLoss from share s, company c where s.companyId=c.companyId order by gainLoss DESC;";
            topCompanyList=jdbcTemplate.query(sql,new ShareRepositoryService.AnlaysisShareRowMapper());

        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetch Top Company Gainers Repository @@@@@@@@@@" + ex);
        }
        return topCompanyList;
    }

    @Override
    public List<Share> fetchTopCompanyLoosers() {
        List<Share> topCompanyList = null;
        try{
            final String sql="select s.*,c.name,(s.currentPrice-originalPrice) as gainLoss from share s, company c where s.companyId=c.companyId order by gainLoss ASC;";
            topCompanyList=jdbcTemplate.query(sql,new ShareRepositoryService.AnlaysisShareRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetch Top Company Loosers Repository @@@@@@@@@@" + ex);
        }
        return topCompanyList;
    }

    @Override
    public List<Share> fetchTopShares() {
        List<Share> topShareList = null;
        try{
            final String sql="select s.*,c.name,(s.currentPrice*s.quantity) as gainLoss from share s, company c where s.companyId=c.companyId order by gainLoss DESC;";
            topShareList=jdbcTemplate.query(sql,new ShareRepositoryService.AnlaysisShareRowMapper());
        }
        catch(Exception ex)
        {
            log.info("log info @@@@@@@@@@ fetch Top Shares @@@@@@@@@@" + ex);
        }
        return topShareList;
    }

    @Override
    public UserPortfolio fetchUserPortfolioExit(String companyId,String userId)
    {
        final String sqldummy = "SELECT * FROM userPortfolio WHERE companyId = ? and userId=?";
        UserPortfolio userPortfolio = jdbcTemplate.queryForObject(sqldummy, new Object[]{companyId,userId},new UserPortfolioRowMapper());
        return userPortfolio;
    }

    @Override
    public boolean isGameGroupExit(String groupName) {
        //dont delete this code
        boolean result = false;
        final String sqldummy = "SELECT count(*) FROM game WHERE groupName = ?";
        int count = (Integer) jdbcTemplate.queryForObject(sqldummy, new Object[]{groupName}, Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public void deleteGame(String groupName)
    {
        try {
            if (isGameGroupExit(groupName)) {
                final String sqldummy = "DELETE FROM game WHERE groupName = ?";
                jdbcTemplate.update(sqldummy, new Object[]{groupName});
                //Delete BOT,Begin
                if(groupName.endsWith("BOT")){
                    aiBotService.obseleteAIBot(groupName);}
                //Delete BOT,End
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public int deleteGameUser(String groupName,String user)
    {
        int returnVal=0;
        try {
            if (isGameGroupExit(groupName)) {
                final String sqldummy = "DELETE FROM game WHERE groupName = ? and userId=?";
                returnVal=jdbcTemplate.update(sqldummy, new Object[]{groupName,user});
                //Delete BOT,Begin
                if(groupName.endsWith("BOT")){
                    final String sqlBot = "DELETE FROM game WHERE groupName = ?";
                    returnVal=jdbcTemplate.update(sqlBot, new Object[]{groupName});
                    aiBotService.obseleteAIBot(groupName);}
                //Delete BOT,End
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return  returnVal;
    }
}
