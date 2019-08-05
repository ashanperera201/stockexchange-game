package com.stockMarket.ucd.stockMarket.service.daoService;

import com.stockMarket.ucd.stockMarket.domain.Share;
import com.stockMarket.ucd.stockMarket.domain.ShareItem;

import java.util.List;

public interface ShareDaoService {
    List<Share> getAllShares();
    List<ShareItem> getAllShareItems(String Company,String userId, String startDate, String endDate);
    void insertShareItemToDb(ShareItem shareItem);
    void deleteShareItemToDb(String email);
    void updateSharesToDb(Share share);
    void setValues(String sector , int mp , int random);
}
