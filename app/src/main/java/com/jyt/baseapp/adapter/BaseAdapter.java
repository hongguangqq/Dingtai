package com.jyt.baseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/16 16:44
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    public List<T> data;
    public Context context;
    private LayoutInflater mInflater;
    private BaseViewHolder<T> mHolder;
    protected BaseViewHolder.OnViewHolderClickListener<T> mListener;


    public BaseAdapter(List<T> data, Context context){
        this.data=data;
        this.context=context;
        mInflater= LayoutInflater.from(context);
    }



    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mHolder=getHolder(mInflater,parent,viewType);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindViewHolder(data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size()+getMorePosition(0);
    }

    /**
     * 刷新数据
     * @param data
     */
    public void  notifyData(List<T> data){
        this.data=data;
        if (this.data!=null){
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多数据
     * @param data
     */
    public void  LoadMoreData(List<T> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public int getMorePosition(int position){
        return position;
    }

    public abstract BaseViewHolder<T> getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType);

    public void setonViewHolderClickListener(BaseViewHolder.OnViewHolderClickListener<T> Listener){
        mListener=Listener;
    }

}
