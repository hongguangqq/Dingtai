package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.InfoBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/13 10:15
 */
public class InfoModel {

    public void getInfomationData(Context context , final ResultInfoDataListener listener){
        HttpUtil.getInfomationData(context, new HttpUtil.OnHttpListener<List<InfoBean>>() {
            @Override
            public void onResponse(BaseBean<List<InfoBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        listener.ResultData(false,null);
                        BaseUtil.makeText("商品资讯获取失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("商品资讯获取失败");
                }
            }
        });
    }

    public interface ResultInfoDataListener{
        void ResultData(boolean isSuccess, List<InfoBean> data);
    }
}
