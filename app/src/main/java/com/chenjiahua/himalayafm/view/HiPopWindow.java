package com.chenjiahua.himalayafm.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.adapters.PlaybackListAdapter;
import com.chenjiahua.himalayafm.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;
import java.util.Set;

public class HiPopWindow extends PopupWindow {

    private final View mPopView;
    private View mPlayListCloseBtn;
    private RecyclerView mPlaybackListRv;
    private PlaybackListAdapter mPlaybackListAdapter;
    private PlayModeIconClickListener mPlayModeListener = null;
    private TextView mPlayModeTvShow;
    private ImageView mPlayModeIvShow;
    private View mPlayListContainer;

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
//      setFocusable();
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

        mPlayListContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:执行点击PlayMode进行改变
                if (mPlayModeListener != null) {
                    mPlayModeListener.playModeClick();
                }

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

        //找到播放列表里的播放模式textView显示
        mPlayModeTvShow = mPopView.findViewById(R.id.play_mode_tv_show);
        //找到播放列表里的播放模式ImageView显示
        mPlayModeIvShow = mPopView.findViewById(R.id.play_mode_iv_show);

        mPlayListContainer = mPopView.findViewById(R.id.play_list_controller_container);
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

    public void setPlayListItemClickListener(PlayListItemClickListener listener){
        mPlaybackListAdapter.setOnItemClickListener(listener);
    }

    public void updatePlayModeBtImg(XmPlayListControl.PlayMode currentMode) {
        updatePlayModeBtnImg(currentMode);
    }
    /**
     * 根据当前的状态，更新播放模式
     *
     * @param
     */
    private void updatePlayModeBtnImg(XmPlayListControl.PlayMode playMode) {
        int resId = R.drawable.selector_play_model_list;
        int resText = R.string.play_mode_list_text;
        switch(playMode){
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_play_model_list;
                resText = R.string.play_mode_list_text;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_play_model_random;
                resText = R.string.play_mode_random_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_play_model_list_loop;
                resText = R.string.play_mode_list_loop_text;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_play_single_loop;
                resText = R.string.play_mode_single_song_text;
                break;
        }
        mPlayModeIvShow.setImageResource(resId);
        mPlayModeTvShow.setText(resText);
    }
    public interface PlayListItemClickListener{
        void onItemClick(int position);
    }

    public void setOnPlayModeClickListener(PlayModeIconClickListener listener){
        this.mPlayModeListener = listener;
    }

    public interface PlayModeIconClickListener{
        void playModeClick();
    }

}
