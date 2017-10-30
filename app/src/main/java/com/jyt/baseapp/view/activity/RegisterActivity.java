package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.BankBean;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.util.BaseUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_down)
    ImageView mIvDown;
    @BindView(R.id.et_user)
    EditText mEtUser;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_repwd)
    EditText mEtrepwd;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_agent)
    EditText mEtAgent;
    @BindView(R.id.ll_step1)
    LinearLayout mLlStep1;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.et_numlocation)
    EditText mEtNumlocation;
    @BindView(R.id.tv_bank)
    TextView mTvBank;
    @BindView(R.id.et_branch)
    EditText mEtBranch;
    @BindView(R.id.et_bankNum)
    EditText mEtBankNum;
    @BindView(R.id.tv_file)
    TextView mTvFile;
    @BindView(R.id.ll_file)
    LinearLayout mLlFile;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_code)
    TextView mTv_code;
    @BindView(R.id.ll_step2)
    LinearLayout mLlStep2;
    @BindView(R.id.btn_step)
    Button mBtnStep;
    private boolean isSecond;
    private StringBuilder codeBuilder=new StringBuilder();
    private char[] codeArray="ABCDEFGHIJKLMNOPQRSTUVWSYZ".toCharArray();
    private String mCode;//生成的验证码
    private List<BankBean> mBankData;
    //上传的数据
    private String username;//账户名
    private String pwd;//密码
    private String repwd;//确认密码
    private String name;//用户名
    private String sex;//性别
    private String mobile;//手机号码
    private String agent;//代理商
    private String idCard;//身份证号码
    private String idCarAddress;//身份证地址
    private String bankName;//开户行
    private String bankBranch;//开户支行
    private String Contract;//合同下载地址
    private String bankNum;//银行卡号
    private String verifyCode;//验证码
    private RegisterModel mModel;
    private boolean isFemale;

    private PopupWindow mPopupWindow;
    private LinearLayout mLl_Popbank;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };



    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
        initPop();
        initListener();
    }

    private void initSetting() {
        mModel=new RegisterModel();
        mCode=CreateCode();
        mTv_code.setText(mCode);
        //获取银行数据
        mModel.GetBankData(this, new RegisterModel.BankDataResultListener() {
            @Override
            public void ResultData(final List<BankBean> data, final boolean isSuccess) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess){
                            for (int i = 0; i < data.size(); i++) {
                                final TextView bank=new TextView(BaseUtil.getContext());
                                bank.setText(data.get(i).bankName);
                                bank.setTextAppearance(BaseUtil.getContext(),R.style.bankText);//设置style
                                bank.setGravity(Gravity.CENTER_HORIZONTAL);
                                bank.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTvBank.setText(bank.getText().toString());
                                        bankName=bank.getText().toString();
                                        if (mPopupWindow.isShowing()){
                                            mPopupWindow.dismiss();
                                        }
                                    }
                                });
                                mLl_Popbank.addView(bank);
                            }
                        }else {

                        }
                    }
                });

            }
        });
    }

    private void initListener() {
        mTvSex.setOnClickListener(this);
        mBtnStep.setOnClickListener(this);
        mLlFile.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvDown.setOnClickListener(this);
    }

    private void initPop(){
        View popView=getLayoutInflater().inflate(R.layout.pop_bank,null);
        mLl_Popbank = (LinearLayout) popView.findViewById(R.id.ll_pop_bank);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (!isSecond){
                    finish();
                    overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                }else {
                    mBtnStep.setText("下一步");
                    mLlStep1.setVisibility(View.VISIBLE);
                    mLlStep2.setVisibility(View.GONE);
                    isSecond=false;
                }
                break;
            //切换性别
            case R.id.tv_sex:
                if (!isFemale){
                    mTvSex.setText("女");
                    isFemale=true;
                }else {
                    mTvSex.setText("男");
                    isFemale=false;
                }
                break;
            //选择银行
            case R.id.iv_down:
                mPopupWindow.showAsDropDown(findViewById(R.id.ll_bank),10,0);
                break;
            //选择合同文件
            case R.id.ll_file:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            //下一页OR注册
            case R.id.btn_step:
                if (!isSecond){
                    //当前处于第一注册页-跳到第二注册页
                    mBtnStep.setText("注册");
                    mLlStep1.setVisibility(View.GONE);
                    mLlStep2.setVisibility(View.VISIBLE);
                    isSecond=true;
                }else {
                    //当前处于第二注册页--联网提交数据
                    RegisterNewUser();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                File myFile=new File(path);
                Log.e("ZDLW",myFile.getAbsolutePath());
                mModel.UploadContract(this, myFile, new RegisterModel.UpLoadResult() {
                    @Override
                    public void ResultPath(final String url, boolean isSuccess) {
                        if (isSuccess){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTvFile.setText("已上传");
                                    Contract=url;
                                }
                            });
                        }else {
                            BaseUtil.makeText("上传失败，请重试");
                        }
                    }
                });
            }

        }

    }



    private void RegisterNewUser(){
        username=mEtUser.getText().toString().trim();
        pwd=mEtPwd.getText().toString().trim();
        repwd=mEtrepwd.getText().toString().trim();
        name=mEtName.getText().toString().trim();
        if (!isFemale){
            sex="男";
        }else {
            sex="女";
        }
        mobile=mEtPhone.getText().toString().trim();
        agent=mEtAgent.getText().toString().trim();
        idCard=mEtNum.getText().toString().trim();
        idCarAddress=mEtNumlocation.getText().toString().trim();
        Log.e("@#","ic="+idCarAddress);
        bankBranch=mEtBranch.getText().toString().trim();
        bankNum=mEtBankNum.getText().toString().trim();
        verifyCode=mEtCode.getText().toString().trim();
        mModel.RegisterNewUser(this, username, pwd, repwd, name, mobile, agent, idCard, idCarAddress, bankName, bankBranch, bankNum, Contract, sex, new RegisterModel.RegisterResult() {
            @Override
            public void ResultData(boolean isSuccess) {
                if (isSuccess){
                    finish();
                }
            }
        });
    }

    /**
     * 生成验证码
     * @return
     */
    private String CreateCode(){
        codeBuilder.setLength(0);
        for (int i = 0; i < 4; i++) {
            int index=(int)(Math.random()*26);
            codeBuilder.append(codeArray[index]);
        }
        return codeBuilder.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
    }
}
