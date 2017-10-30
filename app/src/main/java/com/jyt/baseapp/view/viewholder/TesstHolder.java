package com.jyt.baseapp.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;

/**
 * @author LinWei on 2017/9/5 11:39
 */
public class TesstHolder extends RecyclerView.ViewHolder {
    public TextView tv_goodsName;
    public TextView tv_goodsCode;
    public TextView tv_increase;
    public TextView tv_price;
    public TextView tv_decline;
    public TextView tv_presentQuantity;
    public TextView tv_todayTotalAmount;
    public TextView tv_showTopPrice;
    public TextView tv_showFloorPrice;
    public TextView tv_openPrice;
    public TextView tv_closePrice;
    public TextView tv_inventory;
    public TextView tv_yearlyCapacity;
    public TextView tv_goodsUnit;
    public TextView tv_goodsType;
    public TextView tv_goodsArea;
    public TesstHolder(View itemView) {
        super(itemView);
        tv_goodsName= (TextView) itemView.findViewById(R.id.tv_goodsName);
        tv_goodsCode= (TextView) itemView.findViewById(R.id.tv_goodsCode);
        tv_increase= (TextView) itemView.findViewById(R.id.tv_increase);
        tv_price= (TextView) itemView.findViewById(R.id.tv_price);
        tv_decline= (TextView) itemView.findViewById(R.id.tv_decline);
        tv_presentQuantity= (TextView) itemView.findViewById(R.id.tv_presentQuantity);
        tv_todayTotalAmount= (TextView) itemView.findViewById(R.id.tv_todayTotalAmount);
        tv_showTopPrice= (TextView) itemView.findViewById(R.id.tv_showTopPrice);
        tv_showFloorPrice= (TextView) itemView.findViewById(R.id.tv_showFloorPrice);
        tv_openPrice= (TextView) itemView.findViewById(R.id.tv_openPrice);
        tv_closePrice= (TextView) itemView.findViewById(R.id.tv_closePrice);
        tv_inventory= (TextView) itemView.findViewById(R.id.tv_inventory);
        tv_yearlyCapacity= (TextView) itemView.findViewById(R.id.tv_yearlyCapacity);
        tv_goodsUnit= (TextView) itemView.findViewById(R.id.tv_goodsUnit);
        tv_goodsType= (TextView) itemView.findViewById(R.id.tv_goodsType);
        tv_goodsArea= (TextView) itemView.findViewById(R.id.tv_goodsArea);
    }
}
