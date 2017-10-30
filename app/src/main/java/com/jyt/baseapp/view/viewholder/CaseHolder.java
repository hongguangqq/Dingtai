package com.jyt.baseapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.CaseBean;

/**
 * @author LinWei on 2017/9/8 10:35
 */
public class CaseHolder  extends BaseViewHolder<CaseBean>{
    TextView tv_effectiveTime;
    TextView tv_goodsName;
    TextView tv_goodsCode;
    TextView tv_goodsType;
    TextView tv_num;
    TextView tv_price;
    public CaseHolder(View itemView) {
        super(itemView);
        tv_effectiveTime= (TextView) itemView.findViewById(R.id.tv_effectiveTime);
        tv_goodsName= (TextView) itemView.findViewById(R.id.tv_goodsName);
        tv_goodsCode= (TextView) itemView.findViewById(R.id.tv_goodsCode);
        tv_goodsType= (TextView) itemView.findViewById(R.id.tv_goodsType);
        tv_num= (TextView) itemView.findViewById(R.id.tv_num);
        tv_price= (TextView) itemView.findViewById(R.id.tv_price);
    }

    @Override
    public void onBindViewHolder(CaseBean data, int position) {
        super.onBindViewHolder(data, position);
        tv_effectiveTime.setText(data.createdTime);
        tv_goodsName.setText(data.goodsName);
        tv_goodsCode.setText("订单编号："+data.orderNo);
        if ("1".equals(data.goodsType)){
            tv_goodsType.setText("白盘");
        }else {
            tv_goodsType.setText("晚盘");
        }
        tv_num.setText(data.price+"x"+data.num);
        tv_price.setText(data.totalPrice);
    }
}
