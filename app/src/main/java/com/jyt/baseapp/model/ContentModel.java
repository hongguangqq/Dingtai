package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.HintBean;
import com.jyt.baseapp.bean.PersonInfoBean;
import com.jyt.baseapp.util.HttpUtil;

/**
 * @author LinWei on 2017/9/4 13:56
 */
public class ContentModel {
    public void getHint(Context context, final String contentType, final ResultHintListener listener){
        HttpUtil.getHint(context, contentType, new HttpUtil.OnHttpListener<HintBean>() {
            @Override
            public void onResponse(BaseBean<HintBean> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            if ("0".equals(contentType)){
                                listener.ResultData(true,response.data.warnTips,response.data.dealRisk,response.data.notification,response.data.aboutUs);
                            }else if ("1".equals(contentType)){
                                listener.ResultData(true,response.data.content,null,null,null);
                            }else if ("2".equals(contentType)){
                                listener.ResultData(true,null,response.data.content,null,null);
                            }else if ("3".equals(contentType)){
                                listener.ResultData(true,null,null,response.data.content,null);
                            }else if ("4".equals(contentType)){
                                listener.ResultData(true,null,null,null,response.data.content);
                            }

                        }else {
                            listener.ResultData(false,null,null,null,null);
                        }
                    }else {
                        listener.ResultData(false,null,null,null,null);
                    }
                }
            }
        });
    }
    public interface ResultHintListener{
        void ResultData(boolean isSuccess,String warnTips,String dealRisk,String notification,String aboutUs);
    }

    public void getPersonInfo(Context context, final ResultPersonInfoListener listener){
        HttpUtil.getPersonInfo(context, new HttpUtil.OnHttpListener<PersonInfoBean>() {
            @Override
            public void onResponse(BaseBean<PersonInfoBean> response, boolean isSuccess, Exception e) {
                if (listener!=null){
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
            }
        });
    }

    public interface ResultPersonInfoListener{
        void ResultData(boolean isSuccess,PersonInfoBean data);
    }

}
