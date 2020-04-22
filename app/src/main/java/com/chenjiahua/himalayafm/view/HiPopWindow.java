package com.chenjiahua.himalayafm.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.adapters.PlaybackListAdapter;
import com.chenjiahua.himalayafm.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class HiPopWindow extends PopupWindow {

    private final View mPopView;
    private View mPlayListCloseBtn;
    private RecyclerView mPlaybackListRv;
    private PlaybackListAdapter mPlaybackListAdapter;

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
        //设置动画
        setAnimationStyle(R.style.pop_animation);
//        setFocusable();
        initView();
        initEvent();
    }

    private void initEvent() {
        mPlayListCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiPopWindow.this.dismiss();
            }
        });
    }

    private void initView() {
        mPlayListCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
        //先找到控件
        mPlaybackListRv = mPopView.findViewById(R.id.playback_list_rv);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mPlaybackListRv.setLayoutManager(layoutManager);
        //创建适配器
        mPlaybackListAdapter = new PlaybackListAdapter();
        //设置适配器
        mPlaybackListRv.setAdapter(mPlaybackListAdapter);
    }


    public void setListData(List<Track> data){
        if (mPlaybackListAdapter != null) {
            mPlaybackListAdapter.setData(data);
        }
    }

    public void setCurPlayPos(int index){
        if (mPlaybackListAdapter != null) {
            mPlaybackListAdapter.setCurPlayPos(index);
            mPlaybackListRv.scrollToPosition(index);
        }
    }
}
