package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.TransactionBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/12 10:33
 */
public class TransactionModel {

    public void getFlowingWaterData(Context context, String searchTime, String endTime, final ResultFwDataListener listener){
        HttpUtil.getFlowingWaterData(context, searchTime, endTime, new HttpUtil.OnHttpListener<List<TransactionBean>>() {
            @Override
            public void onResponse(BaseBean<List<TransactionBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    public interface ResultFwDataListener{
        void ResultData(boolean isSuccess, List<TransactionBean> data);
        void LoseLogin();
    }
}
