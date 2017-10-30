package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;

/**
 * @author LinWei on 2017/9/7 11:19
 */
public class GuaItem extends RelativeLayout {

    private TextView mTv_title;
    private TextView mTv_msg;
    private ImageView mIv_pop;
    private EditText mEt_input;
    private RelativeLayout mRl_view;

    public GuaItem(Context context) {
        super(context);
        init(context);
    }

    public GuaItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.GuaItem);
        String title=typedArray.getString(R.styleable.GuaItem_title);
        String msg=typedArray.getString(R.styleable.GuaItem_msg);
        String input=typedArray.getString(R.styleable.GuaItem_input);
        int inputType=typedArray.getInt(R.styleable.GuaItem_inputtype, InputType.TYPE_CLASS_NUMBER);
        boolean isShow=typedArray.getBoolean(R.styleable.GuaItem_isshow,true);
        mTv_title.setText(title);
        mTv_msg.setText(msg);
        mEt_input.setText(input);
        mEt_input.setInputType(inputType);
        if (isShow){
            mIv_pop.setVisibility(VISIBLE);
            mTv_msg.setVisibility(VISIBLE);
            mEt_input.setVisibility(GONE);
        }else {
            mIv_pop.setVisibility(GONE);
            mTv_msg.setVisibility(GONE);
            mEt_input.setVisibility(VISIBLE);
        }
        //默认限制输入为整数、小数且不允许小数点为开头
        mEt_input.setKeyListener(DigitsKeyListener.getInstance("1234567890."));
        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt=s.toString();
                if (txt.length()==1 && ".".equals(txt)){
                    s.clear();
                }
            }
        });
    }

    public GuaItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View root=View.inflate(context, R.layout.view_guaitem,this);
        mTv_title = (TextView) this.findViewById(R.id.tv_title);
        mTv_msg = (TextView) this.findViewById(R.id.tv_msg);
        mRl_view = (RelativeLayout) this.findViewById(R.id.rl_view);
        mIv_pop = (ImageView) this.findViewById(R.id.iv_pop);
        mEt_input = (EditText) this.findViewById(R.id.et_input);
        mRl_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.OnClick(mRl_view);
                }
            }
        });
    }

    public void SetTitle(String txt){
       if (txt!=null){
           mTv_title.setText(txt);
       }
    }

    public void SetMsg(String txt){
        if (txt!=null){
            mTv_msg.setText(txt);
        }
    }

    public void setInput(String txt){
        if (txt!=null){
            mEt_input.setText(txt);
        }
    }

    public String getMsg(){
        return mTv_msg.getText().toString().trim();
    }

    public String getInput(){
        return mEt_input.getText().toString().trim();
    }

    public void setInpuType(int type){
        mEt_input.setInputType(type);
    }

    /**
     * 禁止输入
     * @param enable
     */
    public void setInputEnable(boolean enable){
        mEt_input.setFocusable(false);
        mEt_input.setFocusableInTouchMode(false);
        mEt_input.setEnabled(false);
    }

    public EditText getEt_input(){
        return mEt_input;
    }


    /**
     * popupwindow的点击弹出
     */
    private OnGuaPopClickListener mListener;
    public void setOnGuaPopClickListener(OnGuaPopClickListener listener){
        mListener=listener;
    }
    public interface OnGuaPopClickListener{
        void OnClick(View v);
    }

    public View getRlView(){
        return mRl_view;
    }
}
