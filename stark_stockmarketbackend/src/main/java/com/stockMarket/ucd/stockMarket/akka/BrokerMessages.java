package com.stockMarket.ucd.stockMarket.akka;

import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.*;
import com.stockMarket.ucd.stockMarket.service.daoService.BasicDataDaoService;

import java.util.List;

public interface BrokerMessages {
    class RequestshareDetails {
        public String companyId;
        public String userId;
        public String startDate;
        public String endDate;
        ShareService shareService;
        public RequestshareDetails(String companyId, String userId, String startDate, String endDate, ShareService shareService) {
            this.companyId = companyId;
            this.userId = userId;
            this.startDate=startDate;
            this.endDate =endDate;
            this.shareService = shareService;
        }
    }

    class AddShareDetails {
        ShareItem shareItem;
        ShareService shareService;
        public AddShareDetails(ShareItem shareItem, ShareService shareService) {
            this.shareItem = shareItem;
            this.shareService = shareService;
        }
    }
    class DeleteShareDetails {
        String id;
        ShareService shareService;
        public DeleteShareDetails(String id, ShareService shareService) {
            this.id = id;
            this.shareService = shareService;
        }
    }

    class FetchShareDetails {
        ShareService shareService;
        public FetchShareDetails(ShareService shareService) {
            this.shareService = shareService;
        }
    }

    class UpdateShareDetails {
        ShareService shareService;
        Share share;
        public UpdateShareDetails(Share share,ShareService shareService) {
            this.shareService = shareService;
            this.share=share;
        }
    }

    class FetchUserDetails {
        UserService userService;
        public FetchUserDetails(UserService userService) {
            this.userService = userService;
        }
    }

    class AddUserDetails {
        User user;
        UserService userService;
        BasicDataService basicDataService;
        public AddUserDetails(User user, UserService userService,BasicDataService basicDataService) {
            this.user = user;
            this.userService = userService;
            this.basicDataService=basicDataService;
        }
    }

    class EmailVerification {
        UserService userService;
        String email;
        public EmailVerification(UserService userService,String email) {
            this.userService = userService;
            this.email=email;
        }
    }
    class UpdateUserDetails {
        UserService userService;
        User user;
        public UpdateUserDetails(UserService userService, User user) {
            this.userService = userService;
            this.user=user;
        }
    }

    class DeleteUserDetails {
        String email;
        UserService userService;
        public DeleteUserDetails(UserService userService,String email) {
            this.email = email;
            this.userService = userService;
        }
    }

    class LogoutDetails {
        UserService userService;
        User user;
        public LogoutDetails(UserService userService,User user) {
            this.user = user;
            this.userService = userService;
        }
    }

    class GameRegister {
        GameService gameService;
        List<Game> game;
        GroupService groupService;

        public GameRegister(List<Game> game, GameService gameService,GroupService groupService) {
            this.game = game;
            this.gameService = gameService;
            this.groupService= groupService;
        }
    }

    class FetchBasicData {
        GameService gameService;
        String userId;
        public FetchBasicData(String userId,GameService gameService) {
            this.userId = userId;
            this.gameService = gameService;
        }
    }


    class GetCalculator {
        GameService gameService;
        Calculator calc;
        public GetCalculator(  Calculator calc,GameService gameService) {
            this.calc = calc;
            this.gameService = gameService;
        }
    }

    class FetchUserPortfolioDetails {
        GameService gameService;
        String userId;
        public FetchUserPortfolioDetails(String userId,GameService gameService) {
            this.gameService = gameService;
            this.userId=userId;
        }
    }
    class AddUserPortfolioDetails {
        GameService gameService;
        UserPortfolio userPortfolio;
        public AddUserPortfolioDetails( UserPortfolio userPortfolio,GameService gameService) {
            this.gameService = gameService;
            this.userPortfolio=userPortfolio;
        }
    }
    class ModifyGame{
        GameService gameService;
        BankService bankService;
        Game game;
        public ModifyGame( Game game,GameService gameService,BankService bankService) {
            this.gameService = gameService;
            this.game=game;
            this.bankService=bankService;
        }
    }

    class GenerateMarketBoom{
        GameService gameService;
        SectorService sectorService;
        int round;
        public GenerateMarketBoom( int round,GameService gameService,SectorService sectorService) {
            this.gameService = gameService;
            this.sectorService=sectorService;
            this.round=round;
        }
    }

    class GenerateGameSummary{
        GameService gameService;
        String groupName;
        public GenerateGameSummary( String groupName,GameService gameService) {
            this.gameService = gameService;
            this.groupName=groupName;
        }
    }

    class FetchSectorCompany{
        GameService gameService;
        String sector;
        public FetchSectorCompany( String sector,GameService gameService) {
            this.gameService = gameService;
            this.sector=sector;
        }
    }

    class FetchShareSummarySector{
        GameService gameService;
        public FetchShareSummarySector(GameService gameService) {
            this.gameService = gameService;
        }
    }
    class FetchShareSummaryCompany{
        GameService gameService;
        public FetchShareSummaryCompany(GameService gameService) {
            this.gameService = gameService;
        }
    }
    class FetchTopCompanyGainers{
        GameService gameService;
        public FetchTopCompanyGainers(GameService gameService) {
            this.gameService = gameService;
        }
    }
    class FetchTopCompanyLoosers{
        GameService gameService;
        public FetchTopCompanyLoosers(GameService gameService) {
            this.gameService = gameService;
        }
    }
    class FetchTopShares{
        GameService gameService;
        public FetchTopShares(GameService gameService) {
            this.gameService = gameService;
        }
    }
    class DeleteGame{
        GameService gameService;
        String groupName;
        public DeleteGame(GameService gameService, String groupName) {
            this.gameService = gameService;
            this.groupName= groupName;
        }
    }

    class LoginDetails {
        UserService userService;
        User user;
        public LoginDetails(UserService userService,User user) {
            this.user = user;
            this.userService = userService;
        }
    }

    class BotPlayer {
        AIBotService aiBotService;
        Game game;
        public BotPlayer(AIBotService aiBotService,Game game) {
            this.game = game;
            this.aiBotService = aiBotService;
        }
    }

    class AddBotPlayergame {
        AIBotService aiBotService;
        Game game;
        public AddBotPlayergame(AIBotService aiBotService,Game game) {
            this.game = game;
            this.aiBotService = aiBotService;
        }
    }

    class DeleteBotPlayergame {
        AIBotService aiBotService;
        String bot;
        public DeleteBotPlayergame(AIBotService aiBotService,String bot) {
            this.bot = bot;
            this.aiBotService = aiBotService;
        }
    }

    class DeleteGameUser{
        GameService gameService;
        String groupName;
        String user;
        public DeleteGameUser(GameService gameService, String groupName,String user) {
            this.gameService = gameService;
            this.groupName= groupName;
            this.user=user;
        }
    }
}
