package com.chenjiahua.himalayafm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chenjiahua.himalayafm.R;

public abstract class UILoader extends FrameLayout {

    private View loadingView;
    private View emptyView;
    private View networkErrorView;
    private View successView;


    public enum UIStatus {
        LOADING, SUCCESS, NETWORK_ERROR, EMPTY, NONE
    }

    /**
     * 设置默认状态为 NONE
     */
    public UIStatus mCurrentStatus = UIStatus.NONE;


    public UILoader(@NonNull Context context) {
        this(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        init();
    }

    /**
     * 初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
//LOADING
        if (loadingView != null) {
            loadingView = getLoadingView();
            addView(loadingView);
        }
        //根据状态设置是否可见
        loadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE);

// EMPTY
        if (emptyView != null) {
            emptyView = getEmptyView();
            addView(emptyView);
        }
        //根据状态设置是否可见
        loadingView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);

//NETWORK ERROR
        if (networkErrorView != null) {
            networkErrorView = getNetWorkErrorView();
            addView(networkErrorView);
        }
        //根据状态设置是否可见
        loadingView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROR ? VISIBLE : GONE);

// SUCCESS
        if (successView != null) {
            successView = getSuccessView();
            addView(successView);
        }
        //根据状态设置是否可见
        loadingView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);
    }

    protected abstract View getSuccessView();

    private View getNetWorkErrorView() {
        return null;
    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.empty_view_layout,this,false);
    }

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.loading_view_layout,this,false);
    }
}
