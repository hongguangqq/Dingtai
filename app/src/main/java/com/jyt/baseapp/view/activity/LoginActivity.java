package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import java.io.File;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.civ_login_head)
    CircleImageView mCiv_head;
    @BindView(R.id.et_login_user)
    EditText mEt_user;
    @BindView(R.id.et_login_pwd)
    EditText mEt_pwd;
    @BindView(R.id.btn_login_login)
    Button mBtn_login;
    @BindView(R.id.btn_login_news)
    Button mBtn_news;
    @BindView(R.id.cb_login_remember)
    CheckBox mCb_remember;
    @BindView(R.id.ll_login_remember)
    LinearLayout mLl_remember;
    @BindView(R.id.tv_login_register)
    TextView mTv_register;
    private File pictureFile;
    private LoginModel mModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFile();
        initSetting();
        initListener();
    }

    private void initFile(){
        pictureFile =new File(getCacheDir(),"zdlwlw.jpg");
    }

    private void initSetting() {
        String picHead=BaseUtil.getSpString(Const.PicHead);
        if (picHead!=null){
            if (pictureFile.exists()){
                Glide.with(this)
                        .load(picHead)
                        .error(getResources().getDrawable(R.mipmap.icon_head))
                        .into(mCiv_head);
            }
        }
        mModel=new LoginModel(this);
        mEt_user.setText(BaseUtil.getSpString("r_user"));
        mEt_user.setSelection(mEt_user.length());
        if (BaseUtil.getSpBoolean("remember")){
            mEt_pwd.setText(BaseUtil.getSpString("r_pwd"));
            mCb_remember.setChecked(true);
        }

        //如果用户之前登录成功，且记住密码，下次打开时需要能使用点击功能，确认长度，打开点击功能
        if (mEt_user.getText().toString().trim().length()>0 && mEt_pwd.getText().toString().trim().length()>0){
            mBtn_login.setEnabled(true);
        }else {
            mBtn_login.setEnabled(false);
        }

        mEt_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    mCiv_head.setImageDrawable(getResources().getDrawable(R.mipmap.icon_head));
                }
                if (mEt_pwd.getText().toString().length()>0 && s.toString().length()>0){
                    mBtn_login.setEnabled(true);
                }else {
                    mBtn_login.setEnabled(false);
                }
            }
        });

        mEt_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEt_user.getText().toString().length()>0&&s.toString().length()>0){
                    mBtn_login.setEnabled(true);
                }else {
                    mBtn_login.setEnabled(false);
                }
            }
        });
    }

    private void initListener() {
        mBtn_login.setOnClickListener(this);
        mBtn_news.setOnClickListener(this);
        mLl_remember.setOnClickListener(this);
        mTv_register.setOnClickListener(this);
        mEt_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_login_user:
                mEt_user.setText("");
                break;
            case R.id.btn_login_login:
                Login();
                break;
            case R.id.btn_login_news:
                startActivity(new Intent(LoginActivity.this,ContentActivity.class));
                finish();
                break;
            case R.id.ll_login_remember:
                if (!mCb_remember.isChecked()){
                    mCb_remember.setChecked(true);
                }else {
                    mCb_remember.setChecked(false);
                }
              break;
            case R.id.tv_login_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                break;
        }
    }

    /**
     * 登录并且判断是否记住密码
     */
    private void Login(){
        String user=mEt_user.getText().toString().trim();
        String pwd=mEt_pwd.getText().toString().trim();
        BaseUtil.setSpBoolean("remember",false);//默认为没有勾选
        if (mCb_remember.isChecked()){
            //如果勾选了记住密码，下次打开时要自动勾选记住密码且填写密码
            BaseUtil.setSpBoolean("remember",true);
            BaseUtil.setSpString("r_pwd",pwd);
        }
        BaseUtil.setSpString("r_user",user);
        mModel.Login(user, pwd, new LoginModel.ResultLoginListener() {
            @Override
            public void LoginData(boolean isSuccess) {
                if (isSuccess){
                    startActivity(new Intent(LoginActivity.this,ContentActivity.class));
                    finish();
                }
            }
        });

    }

    /**
     * 双击退出
     */
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}
