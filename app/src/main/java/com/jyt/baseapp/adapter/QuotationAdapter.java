package com.jyt.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.QuotationBean;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.QuotationHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/31 17:57
 */
public class QuotationAdapter extends BaseAdapter<QuotationBean> {
    public QuotationAdapter(List<QuotationBean> data, Context context) {
        super(data, context);
    }

    @Override
    public BaseViewHolder<QuotationBean> getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_quotation,parent,false);
        QuotationHolder holder=new QuotationHolder(itemView);
        holder.setOnViewHolderClickListener(mListener);
        return holder;

    }
}
