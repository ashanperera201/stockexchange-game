package com.stockMarket.ucd.stockMarket.akka;

import com.stockMarket.ucd.stockMarket.domain.Bank;
import com.stockMarket.ucd.stockMarket.domain.Company;
import com.stockMarket.ucd.stockMarket.domain.Group;
import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.service.*;


public interface Messages {

    class RequestBankDetails {

        public String UserID;
        public int Month;
        public int Year;
        BankService bankService;

        public RequestBankDetails(String userID, int month, int year, BankService bankService) {
            this.UserID = userID;
            this.Month = month;
            this.Year = year;
            this.bankService = bankService;
        }
    }

    class AddBankDetails {
        public  Bank bank;
        BankService bankService;

        public AddBankDetails(Bank bank, BankService bankService) {
            this.bank=bank;
            this.bankService = bankService;
        }
    }

    class FetchCompanyDetails {

        CompanyService companyService;

        public FetchCompanyDetails(CompanyService companyService) {

            this.companyService = companyService;
        }
    }

    class FetchCompDetailsBySector {

        CompanyService companyService;
        String sectorId;

        public FetchCompDetailsBySector(CompanyService companyService,String sectorId) {
            this.sectorId=sectorId;
            this.companyService = companyService;
        }
    }

    class AddCompanyDetails{

        CompanyService companyService;
        Company company;

        public AddCompanyDetails(CompanyService companyService,Company company)
        {
            this.company=company;
            this.companyService = companyService;
        }
    }
    class AddGroupDetails{

        GroupService groupService;
        Group group;

        public AddGroupDetails(GroupService groupService,Group group)
        {
            this.groupService = groupService;
            this.group=group;
        }
    }

    class FetchGroupDetails{

        GroupService groupService;
        public FetchGroupDetails(GroupService groupService)
        {
            this.groupService = groupService;
        }
    }
    class DeleteGroupDetails{

        GroupService groupService;
        String group;
        public DeleteGroupDetails(GroupService groupService,String group)
        {
            this.groupService = groupService;
            this.group=group;
        }
    }
    class UpdateGroupDetails{

        GroupService groupService;
        Group group;

        public UpdateGroupDetails(GroupService groupService,Group group)
        {
            this.groupService = groupService;
            this.group=group;
        }
    }

    class FetchSectorDetails{

        SectorService sectorService;
        public FetchSectorDetails(SectorService sectorService)
        {
            this.sectorService = sectorService;
        }
    }

    class DeleteSectorDetails{

        SectorService sectorService;
        String name;
        public DeleteSectorDetails(SectorService sectorService , String name)
        {
            this.sectorService = sectorService;
            this.name=name;
        }
    }
    class DeleteSectorObject{

        SectorService sectorService;
        Sector sector;
        public DeleteSectorObject(SectorService sectorService ,Sector sector)
        {
            this.sectorService = sectorService;
            this.sector=sector;
        }
    }

    class RequestSectorSummary{

        SectorService sectorService;
        public RequestSectorSummary(SectorService sectorService )
        {
            this.sectorService = sectorService;
        }
    }

    class FetchHelpTextDetails {
        UserService userService;
        public FetchHelpTextDetails(UserService userService) {
            this.userService = userService;
        }
    }

    class deleteCompanyDetails{

        CompanyService companyService;
        String companyId;
        public deleteCompanyDetails(CompanyService companyService,String companyId)
        {
            this.companyService = companyService;
            this.companyId=companyId;
        }
    }

    class deleteBankDetails{

        BankService bankService;
        String userId;
        public deleteBankDetails(BankService bankService, String userId)
        {
            this.bankService = bankService;
            this.userId=userId;
        }
    }

    class getBankAmountDetails{

        BankService bankService;
        String userId;
        public getBankAmountDetails(BankService bankService, String userId)
        {
            this.bankService = bankService;
            this.userId=userId;
        }
    }
}