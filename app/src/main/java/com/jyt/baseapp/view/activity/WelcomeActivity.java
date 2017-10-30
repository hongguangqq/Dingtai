package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.util.BaseUtil;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //登录状态，直接进入主界面
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if (BaseUtil.getSpBoolean(Const.IsLogin)){
                   startActivity(new Intent(WelcomeActivity.this,ContentActivity.class));
               }else {
                   startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
               }
               finish();
           }
       },2000);
    }
}
