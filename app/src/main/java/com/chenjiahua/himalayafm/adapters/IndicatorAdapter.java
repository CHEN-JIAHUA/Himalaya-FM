package com.chenjiahua.himalayafm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.chenjiahua.himalayafm.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

public class IndicatorAdapter extends CommonNavigatorAdapter {

    private String[] mTitles = null;

    public IndicatorAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.main_content);
    }

    @Override
    public int getCount() {
        if (mTitles != null) {
            return mTitles.length;
        }
        return 0;
    }

    /**
     * 获取UI界面
     * @param context
     * @param index
     * @return
     */
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        //设置通常的颜色
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        //设置选中的颜色  -- 黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
        colorTransitionPagerTitleView.setTextSize(18);

//        ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
        colorTransitionPagerTitleView.setText(mTitles[index]);
        colorTransitionPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
//        colorTransitionPagerTitleView.setClipColor(Color.WHITE);
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mViewPager.setCurrentItem(index);
//                TODO: 修改页面
            }
        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }


    public void setTitleData() {

    }
}
