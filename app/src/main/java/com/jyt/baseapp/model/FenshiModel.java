package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.TimeBean;
import com.jyt.baseapp.bean.TodayBean;
import com.jyt.baseapp.util.HttpUtil;

/**
 * @author LinWei on 2017/10/17 13:58
 */
public class FenshiModel {

    public void  getFenshiData(Context context, String goodsCode, String goodsId,final ResultFenshiListener listener){
        HttpUtil.getFenshiData(context, goodsCode, goodsId, new HttpUtil.OnFenshiListener() {
            @Override
            public void onResponse(TimeBean response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    listener.ResultData(true,response);
                }
            }
        });
    }



    public interface ResultFenshiListener{
        void ResultData(boolean isSuccess,TimeBean data);
    }

    /**
     * 行情当日定理/转让详情
     * @param context
     * @param goodsId 查询订单的ID
     * @param count 查询订单的列表的数量，默认20
     * @param listener
     */
    public void getTodayData(Context context, String goodsId, String count, final ResultTodayListener listener){
        HttpUtil.getTodayData(context, goodsId, count, new HttpUtil.OnHttpListener<TodayBean>() {
            @Override
            public void onResponse(BaseBean<TodayBean> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);

                    }else {
                        listener.ResultData(false,null);

                    }
                }else {
                    listener.ResultData(false,null);

                }
            }
        });
    }
    public interface ResultTodayListener{
        void ResultData(boolean isSuccess,TodayBean data);
    }
}
