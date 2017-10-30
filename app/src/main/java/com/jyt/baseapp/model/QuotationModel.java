package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.QuotationBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/4 13:32
 */
public class QuotationModel {
    private Context context;

    public QuotationModel(Context context){
        this.context=context;

    }

    public void getQuotationData(String goodsType, final ResultQuotationDataListener listener){
        HttpUtil.getQuotationData(context, goodsType, new HttpUtil.OnHttpListener<List<QuotationBean>>() {
            @Override
            public void onResponse(BaseBean<List<QuotationBean>> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            listener.ResultData(true,response.data);
                        }else {
                            BaseUtil.makeText("行情数据获取失败，"+response.forUser);
                            listener.ResultData(false,null);
                        }
                    }else {
                        listener.ResultData(false,null);
                    }
                }
            }
        });
    }

    public interface ResultQuotationDataListener{
        void ResultData(boolean isSuccess,List<QuotationBean> data);
    }


}
