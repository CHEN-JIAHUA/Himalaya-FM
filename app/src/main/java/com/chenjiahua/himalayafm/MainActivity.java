package com.chenjiahua.himalayafm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.chenjiahua.himalayafm.adapters.IndicatorAdapter;
import com.chenjiahua.himalayafm.adapters.MainContentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CommonNavigator mCommonNavigator;
    private MagicIndicator magicIndicator;
    private ViewPager contentViewPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorClickListener(new IndicatorAdapter.OnIndicatorClickListener() {
            @Override
            public void onTabClick(int index) {
                Log.d(TAG,"click index is --- > " + index);
                if (contentViewPager != null) {

                    contentViewPager.setCurrentItem(index);
                }
            }
        });

    }

    private void initView() {
        magicIndicator = this.findViewById(R.id.main_indicator);
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.himalaya_color));
        //通用的指示器
        mCommonNavigator = new CommonNavigator(this);
//        创建Indicator的适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(mIndicatorAdapter);

//      设置title 数据
        mIndicatorAdapter.setTitleData();
//      ViewPager
        contentViewPager = this.findViewById(R.id.view_page);
//      创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mMainContentAdapter = new MainContentAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);  // ???
        contentViewPager.setAdapter(mMainContentAdapter);

        magicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(magicIndicator,contentViewPager);
    }
}
