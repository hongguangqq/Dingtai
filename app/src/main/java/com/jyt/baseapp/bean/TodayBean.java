package com.jyt.baseapp.bean;

import java.util.ArrayList;

/**
 * @author LinWei on 2017/10/16 10:34
 */
public class TodayBean {
    public String price;
    public String priceChangeRatio;
    public String inventory;
    public String totalPrice;
    public String closePrice;
    public String openPrice;
    public String showTopPrice;
    public String showFloorPrice;
    public String turnoverRate;
    public String inflows;
    public String bigInflows;
    public ArrayList<FiveData> transferFive;
    public ArrayList<FiveData> orderFive;
    public ArrayList<OrderData> orderList;
}
