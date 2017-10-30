package com.jyt.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.TransactionBean;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.TransactionHolder;

import java.util.List;

/**
 * @author LinWei on 2017/9/8 10:34
 */
public class TransactionAdapter extends BaseAdapter<TransactionBean> {
    public TransactionAdapter(List<TransactionBean> data, Context context) {
        super(data, context);
    }

    @Override
    public BaseViewHolder<TransactionBean> getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_case,parent,false);
        TransactionHolder holder=new TransactionHolder(itemView);
        holder.setOnViewHolderClickListener(mListener);
        return holder;
    }
}
