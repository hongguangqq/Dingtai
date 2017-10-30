package com.jyt.baseapp.bean;

import java.util.List;

/**订立挂单的编号和可用资金
 * @author LinWei on 2017/9/8 14:41
 */
public class MoneyBean {
    public String balance;
    public List<Data> goodsCode;

    public class Data{
        public String goodsCode;
        public String goodsId;
    }
}
