package com.jyt.baseapp.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.baseapp.bean.QuotationBean;

import java.util.List;

/**
 * @author LinWei on 2017/9/5 11:39
 */
public class TestAdapter extends RecyclerView.Adapter<TesstHolder> {
    private Context context;
    private List<TesstHolder> data;
    public TestAdapter(Context context,List<QuotationBean> data){

    }
    @Override
    public TesstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TesstHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
