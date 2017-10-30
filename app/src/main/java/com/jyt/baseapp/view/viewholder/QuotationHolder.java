package com.jyt.baseapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.QuotationBean;
import com.jyt.baseapp.util.BaseUtil;

/**
 * @author LinWei on 2017/8/31 17:00
 */
public class QuotationHolder extends BaseViewHolder<QuotationBean> {
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

    public QuotationHolder(View itemView) {
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

    @Override
    public void onBindViewHolder(QuotationBean data, int position) {
        super.onBindViewHolder(data, position);
        if ("1".equals(data.isView)){
            tv_goodsName.setText(data.goodsName);
            tv_goodsCode.setText(data.goodsCode);
            tv_goodsCode.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_line));
            if (data.increase>0){
                tv_increase.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.red));
                tv_increase.setText("↑"+data.increase+"");
            }else {
                tv_increase.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_green1));
                tv_increase.setText("↓"+data.increase+"");
            }

            tv_price.setText(data.price+"");
            tv_price.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.red));
            if (data.decline>0){
                tv_decline.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.red));
                tv_decline.setText("↑"+data.decline+"");
            }else {
                tv_decline.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_green1));
                tv_decline.setText("↓"+data.decline+"");
            }
            tv_presentQuantity.setText(data.presentQuantity+"");
            tv_presentQuantity.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.red));
            tv_todayTotalAmount.setText((Integer.valueOf(data.todayTotalAmount)/10000)+"万");
            tv_todayTotalAmount.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_line));
            tv_showTopPrice.setText(data.showTopPrice+"");
            tv_showTopPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.red));
            tv_showFloorPrice.setText(data.showFloorPrice+"");
            tv_showFloorPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_green1));
            tv_openPrice.setText(data.openPrice+"");
            tv_openPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_green1));
            tv_closePrice.setText(data.closePrice+"");
            tv_inventory.setText((BaseUtil.getDecimaToFloat("0.00",(Float.valueOf(data.inventory)/100000000)))+"亿");
            tv_inventory.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_line));
            tv_yearlyCapacity.setText((Integer.valueOf(data.yearlyCapacity)/10000)+"万");
            tv_goodsUnit.setText(data.goodsUnit+"");
            if ("1".equals(data.goodsType)){
                tv_goodsType.setText("白盘");
            }else {
                tv_goodsType.setText("晚盘");
            }
            tv_goodsArea.setText(data.goodsArea);

        }else {
            tv_goodsName.setText(data.goodsName);
            tv_goodsCode.setText("-");
            tv_goodsCode.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_increase.setText("-");
            tv_increase.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_price.setText("-");
            tv_price.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_decline.setText("-");
            tv_decline.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_presentQuantity.setText("-");
            tv_presentQuantity.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_todayTotalAmount.setText("-");
            tv_todayTotalAmount.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_showTopPrice.setText("-");
            tv_showTopPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_showFloorPrice.setText("-");
            tv_showFloorPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_openPrice.setText("-");
            tv_openPrice.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_closePrice.setText("-");
            tv_inventory.setText("-");
            tv_inventory.setTextColor(BaseUtil.getContext().getResources().getColor(R.color.color_b1));
            tv_yearlyCapacity.setText("-");
            tv_goodsUnit.setText("-");
            tv_goodsType.setText("-");
            tv_goodsArea.setText("-");
        }

    }
}
