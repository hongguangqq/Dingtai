package com.jyt.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.InfoBean;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.InfoHolder;

import java.util.List;

/**
 * @author LinWei on 2017/9/13 10:21
 */
public class InfoAdapter extends BaseAdapter<InfoBean> {

    public InfoAdapter(List<InfoBean> data, Context context) {
        super(data, context);
    }

    @Override
    public BaseViewHolder<InfoBean> getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_info,parent,false);
        InfoHolder holder=new InfoHolder(itemView);
        holder.setOnViewHolderClickListener(mListener);
        return holder;
    }
}
