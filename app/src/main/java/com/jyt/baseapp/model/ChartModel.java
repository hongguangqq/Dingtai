package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.KChartBean;
import com.jyt.baseapp.bean.TodayBean;
import com.jyt.baseapp.util.HttpUtil;

/**
 * @author LinWei on 2017/9/27 14:53
 */
public class ChartModel {


    /**
     * 获取K线图数据
     * @param context
     * @param goodsId
     * @param type  1:日线 2:分钟线
     * @param listener
     */
    public void getKData(Context context, String goodsId, String type,final  ResultKListener listener){
        HttpUtil.getKData(context, goodsId, type, new HttpUtil.OnHttpListener<KChartBean>() {
            @Override
            public void onResponse(BaseBean<KChartBean> response, boolean isSuccess, Exception e) {
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
    public interface ResultKListener{
        void ResultData(boolean isSuccess , KChartBean data);
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
