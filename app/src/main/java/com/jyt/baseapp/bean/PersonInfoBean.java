package com.jyt.baseapp.bean;

import java.io.Serializable;

/**
 * @author LinWei on 2017/9/5 18:06
 */
public class PersonInfoBean implements Serializable {
    public String totalAssets;//总资产
    public String balance;//余额
    public String depositCash;//保证金
    public String procedurePrice;//手续费总和
    public String bankName;//银行名称
    public String bankNum;//银行卡号
    public String createdTime;//注册时间
    public String trueEarn;//实际盈亏
    public String transferEarn;//转让盈亏
    public String name;//用户姓名
    public String username;//用户账号
    public String userImg;//用户头像
    public String logo;//银行logo
}
