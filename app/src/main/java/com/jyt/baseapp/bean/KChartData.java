package com.jyt.baseapp.bean;

/**
 * @author LinWei on 2017/10/12 10:11
 */
public class KChartData {
    public String open;//开盘价 0
    public String close;//收盘价
    public String high;//最高价
    public String low;//最低价
    public String vol;//成交量
    public String zdf;//涨跌幅
    public String time;//时间
    //时间表示为年月日
    public String getTimeY(){
        String num[]= time.split(" ");
        return num[0].substring(2,num[0].length());
    }
    //时间表示为时分
    public String getTimeD(){
        String num[]= time.split(" ");
        return num[1].substring(0,num[1].lastIndexOf(":"));
    }
}
