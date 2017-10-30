package com.jyt.baseapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.InfoBean;

/**
 * @author LinWei on 2017/9/13 10:18
 */
public class InfoHolder extends BaseViewHolder<InfoBean> {
    private TextView tv_goodsName,tv_goodsDesc,tv_updateTime;
    public InfoHolder(View itemView) {
        super(itemView);
        tv_goodsName= (TextView) itemView.findViewById(R.id.tv_iteminfo_goodsName);
        tv_goodsDesc= (TextView) itemView.findViewById(R.id.tv_iteminfo_goodsDesc);
        tv_updateTime= (TextView) itemView.findViewById(R.id.tv_iteminfo_updateTime);
    }

    @Override
    public void onBindViewHolder(InfoBean data, int position) {
        super.onBindViewHolder(data, position);
        tv_goodsName.setText(data.goodsName);
        tv_goodsDesc.setText(data.goodsDesc);
        tv_updateTime.setText(data.updateTime);
    }


}
