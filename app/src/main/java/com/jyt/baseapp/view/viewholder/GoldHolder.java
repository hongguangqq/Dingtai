package com.jyt.baseapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.GoldBean;

/**
 * @author LinWei on 2017/9/12 14:26
 */
public class GoldHolder extends BaseViewHolder<GoldBean> {
    TextView tv_effectiveTime;
    TextView tv_goodsName;
    TextView tv_goodsCode;
    TextView tv_goodsType;
    TextView tv_price;

    public GoldHolder(View itemView) {
        super(itemView);
        tv_effectiveTime= (TextView) itemView.findViewById(R.id.tv_effectiveTime);
        tv_goodsName= (TextView) itemView.findViewById(R.id.tv_goodsName);
        tv_goodsCode= (TextView) itemView.findViewById(R.id.tv_goodsCode);
        tv_goodsType= (TextView) itemView.findViewById(R.id.tv_goodsType);
        tv_price= (TextView) itemView.findViewById(R.id.tv_price);
    }

    @Override
    public void onBindViewHolder(GoldBean data, int position) {
        super.onBindViewHolder(data, position);
        tv_effectiveTime.setText(data.createdTime);
        tv_goodsName.setText(data.goodsName);
        tv_goodsCode.setText("订单编号："+data.cashNo);
        if ("1".equals(data.cashType)){
            tv_goodsType.setText("入金");
        }else if ("2".equals(data.cashType)){
            tv_goodsType.setText("出金");
        } else{
            tv_goodsType.setText("赠送");
        }
        tv_price.setText(data.cash);
    }
}
