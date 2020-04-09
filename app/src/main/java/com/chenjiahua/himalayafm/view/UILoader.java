package com.chenjiahua.himalayafm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View loadingView;
    private View emptyView;
    private View networkErrorView;
    private View successView;
    private LinearLayout networkErrorWidget;
    private OnRetryClickListener mOnRetryClickListener = null;


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

    public void updateStatus(UIStatus status){
        mCurrentStatus = status;
        //更新UI一定要在主线程上
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }


    /**
     * 初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
//LOADING
        if (loadingView == null) {
            loadingView = getLoadingView();
            addView(loadingView);
        }
        //根据状态设置是否可见
            loadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE);

// EMPTY
        if (emptyView == null) {
            emptyView = getEmptyView();
            addView(emptyView);
        }
        //根据状态设置是否可见
        emptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);

//NETWORK ERROR
        if (networkErrorView == null) {
            networkErrorView = getNetWorkErrorView();
            addView(networkErrorView);
        }
        //根据状态设置是否可见
        networkErrorView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROR ? VISIBLE : GONE);


// SUCCESS
        if (successView == null) {
            successView = getSuccessView(this);
            addView(successView);
        }
        //根据状态设置是否可见
        successView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);

    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getNetWorkErrorView() {
        View networkError = LayoutInflater.from(getContext()).inflate(R.layout.network_error_view_layout, this, false);
        networkErrorWidget = networkError.findViewById(R.id.network_error_widget);
        networkErrorWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"重新加载...",Toast.LENGTH_SHORT).show();
                //TODO: 重新获取数据
                if(mOnRetryClickListener != null){
                    mOnRetryClickListener.onRetryClick();
                }

            }
        });
        return networkError;
    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.empty_view_layout,this,false);
    }

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.loading_view_layout,this,false);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener){
        this.mOnRetryClickListener = listener;
    }

    public interface OnRetryClickListener{
        void onRetryClick();
    }
}
