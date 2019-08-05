package com.stockMarket.ucd.stockMarket.other;

import com.stockMarket.ucd.stockMarket.config.Constants;

import java.util.Random;

public class SequenceGenerator {
    public static String generateSequence(String Class){
        Random rnd = new Random();
        if(Class.equalsIgnoreCase("user"))
        {
            return Constants.AUTO_USER_SEQUENCE .concat(String.valueOf(rnd.nextInt(1000)));
        }
        if(Class.equalsIgnoreCase("sector"))
        {
            return Constants.AUTO_SECTOR_SEQUENCE .concat(String.valueOf(rnd.nextInt(1000)));
        }
        if(Class.equalsIgnoreCase("market"))
        {
            for(int i =0; i < 100; i++) {
                return (String.valueOf(rnd.nextInt(10) + 1));
            }
        }
        return"ID";
    }
}
