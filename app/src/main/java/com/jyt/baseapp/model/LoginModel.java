package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.LoginBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

/**
 * @author LinWei on 2017/8/30 11:15
 */
public class LoginModel {
    private Context context;

    public LoginModel(Context context){
        this.context=context;

    }
    public void Login(final String username, final String pwd, final ResultLoginListener listener){
        HttpUtil.Login(context, username, pwd, new HttpUtil.OnHttpListener<LoginBean>() {
            @Override
            public void onResponse(BaseBean<LoginBean> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            BaseUtil.makeText("登陆成功");
                            BaseUtil.setSpString(Const.Username,username);//记录用户名
                            BaseUtil.setSpString(Const.Name,response.data.name);//记住姓名
                            BaseUtil.setSpString(Const.Pwd,pwd);//记住密码
                            BaseUtil.setSpBoolean(Const.IsLogin,true);//标识为登录状态
                            BaseUtil.setSpString(Const.TokenSession,response.data.tokenSession);//记住标识符
                            listener.LoginData(true);
                        }else {
                            BaseUtil.makeText("登陆失败，"+response.forUser);
                            listener.LoginData(false);
                        }
                    }else {
                        BaseUtil.makeText("登陆失败，请重试");
                        listener.LoginData(false);
                    }
                }

            }
        });
    }
    public interface ResultLoginListener{
        void LoginData(boolean isSuccess);
    }

}
