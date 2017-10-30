package com.jyt.baseapp.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public  class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    T data;
    OnViewHolderClickListener<T> onViewHolderClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewHolderClickListener!=null){
                    onViewHolderClickListener.onClick(BaseViewHolder.this,data,getPosition());
                }
            }
        });
    }




    public interface OnViewHolderClickListener<T>{
        void onClick(BaseViewHolder holder,T data ,int position);
    }

    public void setOnViewHolderClickListener(OnViewHolderClickListener onViewHolderClickListener){
        this.onViewHolderClickListener=onViewHolderClickListener;
    }

    public void onBindViewHolder(T data,int position){
        this.data=data;
    }
}
