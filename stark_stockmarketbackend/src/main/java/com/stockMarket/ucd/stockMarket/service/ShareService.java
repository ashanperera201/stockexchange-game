package com.stockMarket.ucd.stockMarket.service;

import com.stockMarket.ucd.stockMarket.domain.*;
import com.stockMarket.ucd.stockMarket.service.daoService.ShareDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShareService {
    private final Logger log = LoggerFactory.getLogger(ShareService.class);
    @Autowired
    @Qualifier("mysqlSRP")
    private ShareDaoService shareDaoService;

    public List<ShareItem> getshareItems(String Company, String userId, String startDate, String endDate) throws IOException {

        List<ShareItem> results = this.shareDaoService.getAllShareItems(Company, userId, startDate, endDate);
        log.info("@@@@@@@@@@@@ Share Item details @@@@@@@@@@@" + results);
        return results;
    }

    public ShareItem getShareitems(ShareItem shareItem) {

        this.shareDaoService.insertShareItemToDb(shareItem);
        return shareItem;

    }

    public int deleteShareItem(String id) {

        this.shareDaoService.deleteShareItemToDb(id);
        return 1;
    }

    public List<Share> findAll() throws IOException {

        List<Share> results = this.shareDaoService.getAllShares();
        log.info("@@@@@@@@@@@@ ArrayList @@@@@@@@@@@" + results);
        return results;
    }

    public Share updateShare(Share share) {
        this.shareDaoService.updateSharesToDb(share);
        return share;
    }

    public boolean addShareItem(String sector , int mp,int random) {
        this.shareDaoService.setValues(sector,mp,random);
        return true;
    }

}
