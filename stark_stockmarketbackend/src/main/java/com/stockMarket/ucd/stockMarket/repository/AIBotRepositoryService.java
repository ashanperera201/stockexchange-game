package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.CompanyService;
import com.stockMarket.ucd.stockMarket.service.GameService;
import com.stockMarket.ucd.stockMarket.service.ShareService;
import com.stockMarket.ucd.stockMarket.service.daoService.AIBotDaoService;
import com.stockMarket.ucd.stockMarket.service.daoService.GameDaoService;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository("mysqlAI")
public class AIBotRepositoryService implements AIBotDaoService {
    private final Logger log = LoggerFactory.getLogger(AIBotRepositoryService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Lazy
    private GameService gameService;

    @Autowired
    @Lazy
    private ShareService shareService;

    @Autowired
    @Lazy
    private CompanyService companyService;

    @Override
    public int executeBot(Game game) {
        updateGame(game);
        return 0;
    }

    public void setBot(Game game) {
        String userBot=game.getUserId().concat("BOT");
        try {
            User user = new User();
            user.setUserId(userBot);
            user.setUserName(userBot);
            user.setEmail(userBot);
            //Add the dynamic BOT as a User,Begin
            final String sqlUser = "INSERT INTO users (userId,username,email) VALUES (?,?,?)";
            jdbcTemplate.update(sqlUser, new Object[]{user.getUserId(),user.getUserName(),user.getEmail()});
            //Add the dynamic BOT as a User,End
            final String sql = "INSERT INTO game (groupName,userId,roundNo,amount,photoURL,initialValue) VALUES (?,?,?,?,?,?)";
            final String groupName = game.getGroupName();
            final String userId = userBot;
            final int round = 1;
            final int amount = 1000;
            final String image = game.getPhotoURL();
            jdbcTemplate.update(sql, new Object[]{groupName, userId, round, amount, image, amount});
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void insertGamesToDb(Game game) {
        Bank bankList = null;
        try {
            final String dumysql = "SELECT amount, transDate FROM stark.bank where userId=? and countNo= (select max(countNo) FROM stark.bank where userId=?) ";
            bankList = jdbcTemplate.queryForObject(dumysql, new Object[]{game.getUserId(), game.getUserId()}, new BankRepositoryService.BankRowMapper());
            final String sql = "INSERT INTO game (groupName,userId,roundNo,amount,photoURL,initialValue) VALUES (?,?,?,?,?,?)";
            final String groupName = game.getGroupName();
            final String userId = game.getUserId();
            final int round = 1;
            final int amount = game.getAmount();
            final String image = game.getPhotoURL();
            jdbcTemplate.update(sql, new Object[]{groupName, userId, round, amount, image, bankList.getAmount()});

            //Adding Bot to Game
            setBot(game);
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Game BOT Repository @@@@@@@@@@" + e);
        }
    }

    public UserPortfolio setUserPorfolioValues(Game game,Share shareList,int quantity,String type,double accBal) {
        UserPortfolio userPortfolio = new UserPortfolio();
        userPortfolio.setQuantity(quantity);
        userPortfolio.setMarketPrice(shareList.getCurrentPrice());
        userPortfolio.setUserId(game.getUserId().concat("BOT"));
        userPortfolio.setType(type);
        userPortfolio.setUserName(game.getUserId().concat("BOT"));
        userPortfolio.setCompanyId(shareList.getCompanyId());
        userPortfolio.setAccBalance(accBal);
        userPortfolio.setTotalAmount(50);
        userPortfolio.setProfitLoss(0.0);
        userPortfolio = gameService.setUserPortfolio(userPortfolio);
       return userPortfolio;
    }

    @Override
    public void updateGame(Game game) {
        String user=game.getUserId().concat("BOT");;
        try {
            //BOT logic Begin

            //Data Class Objects Initialization,Begin
            final String sql      = "UPDATE game set roundNo=? ,amount =? where userId =? and groupName=?";
            final String sqlShare = "SELECT s.* ,c.name FROM share s , company c where  s.companyId=c.companyId AND s.companyId=?";
            UserPortfolio userPortfolio= new UserPortfolio();
            Share shareListBuy = null;
            Share shareListSell = null;
            //Data Class Objects Initialization,End

            //Fetch Basic data,Begin
            final String query="SELECT h.*,u.userName,u.email FROM game h , users u where u.userId=h.userId and h.userId=? and groupName=?";
            final Game gameData = jdbcTemplate.queryForObject(query,new Object[]{user,game.getGroupName()}, new GameRepositoryService.GameRowMapper());
            //Fetch Basic data,End

            //Fetch Random company,Begin
            String randomCompany    = null;
            List<Company> companies = companyService.getAllCompanies();
            Random companyTrend     = new Random();
            int company             = companyTrend.nextInt(9) + 1;
            randomCompany           = companies.get(company).getCompanyId();
            //Fetch Random company, End

            //Random values generation,Begin
            Random botChoice = new Random();
            int botRandomChoice = botChoice.nextInt(2) + 0;
            //Random values generation,End

            //Random Quantity,Begin
            Random randomQuantity = new Random();
            int quantityVal = randomQuantity.nextInt(10) + 1;
            //Random Quantity,End

            if(botRandomChoice==0)//Buy
            {
                final String sqlDummy = "SELECT s.* ,c.name FROM share s , company c where  s.companyId=c.companyId AND s.companyId=?";
                shareListBuy = jdbcTemplate.queryForObject(sqlDummy,new Object[]{randomCompany}, new ShareRepositoryService.ShareRowMapper());
                if(shareListBuy.getQuantity()>quantityVal){
                    shareListBuy.setQuantity(shareListBuy.getQuantity()-quantityVal);   }
                else
                { shareListBuy.setQuantity(0); }
                shareService.updateShare(shareListBuy);
                userPortfolio = setUserPorfolioValues(game,shareListBuy,quantityVal,"BUY",gameData.getAmount());
            }
            else if(botRandomChoice==1)//Sell
            {
                final String sqlDummy = "SELECT c.name, s.* , q.userName FROM userPortfolio s ,company c , users q where   \n" +
                        "s.companyId =(select b.companyId from share b where\n" +
                        "b.currentPrice = (select MAX(x.currentPrice) as currentPrice from share x\n" +
                        "where x.companyId in (select companyId from userPortfolio z where z.userId=?)) limit 1)\n" +
                        "and s.companyId=c.companyId and s.userId=? And q.userId=s.userId";
                userPortfolio = jdbcTemplate.queryForObject(sqlDummy, new Object[]{user,user},new GameRepositoryService.UserPortfolioRowMapper());
                shareListBuy  = jdbcTemplate.queryForObject(sqlShare,new Object[]{userPortfolio.getCompanyId()}, new ShareRepositoryService.ShareRowMapper());
                if(userPortfolio==null)
                {
                    //add empty values validation
                }
                if(quantityVal>userPortfolio.getQuantity())
                {
                    quantityVal = userPortfolio.getQuantity();
                }
                shareListBuy.setQuantity(shareListBuy.getQuantity()+ quantityVal);
                shareService.updateShare(shareListBuy);
                userPortfolio=setUserPorfolioValues(game,shareListBuy,quantityVal,"SELL",gameData.getAmount());
            }
            final int roundNo = game.getRoundNo();
            final double amount = userPortfolio.getAccBalance();
            final String userId = user;
            final String groupName = game.getGroupName();
            //BOT logic ,End
            jdbcTemplate.update(sql, new Object[]{roundNo, amount, userId, groupName});
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ BOT Repository @@@@@@@@@@" + e);
        }
    }

    @Override
    public boolean isPortfolioExit(String userId)
    {
        //dont delete this code
        boolean result = false;
        final String sqldummy = "SELECT count(*) FROM userPortfolio WHERE userId=?";
        int count = (Integer) jdbcTemplate.queryForObject(sqldummy, new Object[]{userId}, Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }
    @Override
    public boolean deleteBotToDb(String Bot) {
        try {
            if(isPortfolioExit(Bot))
            {
                final String sql = "delete FROM stark.userPortfolio where userId=?";
                jdbcTemplate.update(sql, new Object[]{Bot});
            }
            final String sqlUser = "delete FROM users where userId=?";
            jdbcTemplate.update(sqlUser, new Object[]{Bot});
            return true;
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ delete BOT Repository @@@@@@@@@@" + e);
        }
        return false;
    }
}
