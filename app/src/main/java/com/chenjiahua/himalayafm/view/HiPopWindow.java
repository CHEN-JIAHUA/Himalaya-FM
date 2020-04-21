package com.chenjiahua.himalayafm.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.base.BaseApplication;

public class HiPopWindow extends PopupWindow {

    private final View mPopView;

    public HiPopWindow() {

        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置setOutsideTouchable前要先设 SetBackgroundDrawable
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        //加载View
        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.track_list_pop_window_layout,null);
        //设置内容
        setContentView(mPopView);
//        setFocusable();

    }
}
