package com.jyt.baseapp.view.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.view.fragment.BaseFragment;
import com.jyt.baseapp.view.fragment.TestFragment;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2017/9/7 11:12
 */
public class TestActivity extends BaseActivity {

    private PopupWindow mSecondPOPWindow, mFirstPOPWindow = null;
    private View parientView;
    private WindowManager.LayoutParams mParams;
    private AlertDialog mDialog;
    private QuotationDialog mDialog1;
    private ViewPager mVp;
    private TabLayout mTab;
    private List<String> tabTitle;
    private List<BaseFragment> flist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        initVp();



    }

    private void initVp(){
        mVp = (ViewPager) findViewById(R.id.vp_transfer_container);
        mTab = (TabLayout) findViewById(R.id.tab_transfer_tab);
        tabTitle=new ArrayList<>();
        flist=new ArrayList<>();
        tabTitle.add("可转让");
        tabTitle.add("转让中");
        flist.add(new TestFragment());
        flist.add(new TestFragment());
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.addTab(mTab.newTab().setText(tabTitle.get(0)));//加标题
        mTab.addTab(mTab.newTab().setText(tabTitle.get(1)));//加标题
        FragmentViewPagerAdapter adapter=new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragments(flist);
        adapter.setTitles(tabTitle);
        mVp.setAdapter(adapter);
        mTab.setupWithViewPager(mVp);
        mTab.setTabsFromPagerAdapter(adapter);
    }


    private void initPopSecond(){
        View popSecView = getLayoutInflater().inflate(
                R.layout.dialog_case, null);
        mSecondPOPWindow = new PopupWindow(popSecView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mSecondPOPWindow.setTouchable(true);
        mSecondPOPWindow.setOutsideTouchable(true);
        mSecondPOPWindow.setFocusable(true);
        mSecondPOPWindow.setBackgroundDrawable(new BitmapDrawable());

    }



}
