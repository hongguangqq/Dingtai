package com.jyt.baseapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.OrderData;

import java.util.List;

/**
 * @author LinWei on 2017/10/16 15:26
 */
public class OrderAdapter extends android.widget.BaseAdapter {
    private List<OrderData> data;
    private Context context;
    public OrderAdapter(Context context,List<OrderData> data){
        this.context=context;
        this.data=data;
    }

    public void setData(List<OrderData> data){
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_detail,null);
            holder=new Holder();
            holder.createdTime= (TextView) convertView.findViewById(R.id.tv_detail_createdTime);
            holder.price= (TextView) convertView.findViewById(R.id.tv_detail_price);
            holder.num= (TextView) convertView.findViewById(R.id.tv_detail_num);
            holder.dealType= (TextView) convertView.findViewById(R.id.tv_detail_dealType);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        String[] time=data.get(position).createdTime.split(" ");
        holder.createdTime.setText(time[1]);
        holder.price.setText(data.get(position).price);
        holder.num.setText(data.get(position).num);
        if ("1".equals(data.get(position).dealType)){
            holder.dealType.setText("买入");
        }else {
            holder.dealType.setText("转让");
        }
        return convertView;
    }

    class Holder{
        public TextView createdTime;
        public TextView price;
        public TextView num;
        public TextView dealType;
    }
}
