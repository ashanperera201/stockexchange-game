package com.stockMarket.ucd.stockMarket.other;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateCurrentDate {

    public  Date getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return new Date(dateFormat.format(date));
    }
    public  Date getCurrentDateTime()
    {
        Date date = new Date();
        return date;
    }
}
