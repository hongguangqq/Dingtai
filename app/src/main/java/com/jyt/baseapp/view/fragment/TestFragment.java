package com.jyt.baseapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.util.BaseUtil;

/**
 * @author LinWei on 2017/9/9 15:40
 */
public class TestFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView test=new TextView(BaseUtil.getContext());
        test.setText("测试用");
        test.setTextColor(Color.BLACK);
        return test;
    }
}
