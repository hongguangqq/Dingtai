package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.ContractBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.io.File;

/**
 * @author LinWei on 2017/9/5 18:23
 */
public class PersonModel {

    /**
     * 修改头像
     * @param context
     * @param file
     * @param listener
     */
    public void ModifyPic(Context context, File file, final ResultModifyPicListener listener){

        HttpUtil.UploadContract(context, file,true, new HttpUtil.OnHttpListener<ContractBean>() {
            @Override
            public void onResponse(BaseBean<ContractBean> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            listener.ResultData(true,response.data.url);
                        }else {
                            listener.ResultData(false,null);
                            BaseUtil.makeText("头像修改失败，"+response.forUser);
                        }

                    }else {
                        listener.ResultData(false,null);
                        BaseUtil.makeText("头像修改失败，请重试");
                    }
                }
            }
        });
    }


    public interface ResultModifyPicListener{

        void ResultData(boolean isSuccess,String url);
    }
}
