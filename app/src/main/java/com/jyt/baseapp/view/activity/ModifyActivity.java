package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.model.ModifyModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;

import butterknife.BindView;

public class ModifyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_modify_back)
    ImageView mIv_Back;
    @BindView(R.id.et_modify_oldpwd)
    EditText mEt_Oldpwd;
    @BindView(R.id.et_modify_newpwd)
    EditText mEt_Newpwd;
    @BindView(R.id.et_modify_repwd)
    EditText mEt_Repwd;
    @BindView(R.id.btn_modify_submit)
    Button mBtn_Submit;
    private ModifyModel mModel;
    private String oldpwd;
    private String pwd;
    private String repwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initListener();
    }

    private void init() {
        mModel = new ModifyModel();

    }

    private void initListener() {
        mIv_Back.setOnClickListener(this);
        mBtn_Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_modify_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;
            case R.id.btn_modify_submit:
                if (CheckParmas()){
                    mModel.ModifyPassWord(this, oldpwd, pwd, repwd, new ModifyModel.ResultModifyListener() {
                        @Override
                        public void ResultData(boolean isSuccess) {
                            if (isSuccess){
                                startActivity(new Intent(ModifyActivity.this,LoginActivity.class));
                                finish();
                            }
                        }
                    });
                }
                break;

            default:
                break;
        }
    }

    private boolean CheckParmas(){
        oldpwd=mEt_Oldpwd.getText().toString().trim();
        pwd=mEt_Newpwd.getText().toString().trim();
        repwd=mEt_Repwd.getText().toString().trim();
        int i=0;
        if (StringUtils.isEmpty(oldpwd)){
            i++;
            BaseUtil.makeText("请输入旧密码");
        }
        if (StringUtils.isEmpty(pwd)){
            i++;
            BaseUtil.makeText("请输入新密码");
        }
        if (StringUtils.isEmpty(repwd)){
            i++;
            BaseUtil.makeText("请输入确认密码");
        }
        if (i!=0){
            return false;
        }
        return true;
    }
}
