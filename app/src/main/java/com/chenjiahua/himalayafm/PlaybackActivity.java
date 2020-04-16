package com.chenjiahua.himalayafm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chenjiahua.himalayafm.base.BaseActivity;
import com.chenjiahua.himalayafm.interfaces.IPlayCallBack;
import com.chenjiahua.himalayafm.presenters.PlayerPresenterImpl;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlaybackActivity extends BaseActivity implements IPlayCallBack {

    private ImageView mPlayOrPauseBt;
    private PlayerPresenterImpl mPlayerPresenter;

    private SimpleDateFormat mMinSimpleDateFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourSimpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    private String mTotal;
    private TextView mDurationTx;
    private TextView mCurPosTx;
    private String mCurPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        mPlayerPresenter = PlayerPresenterImpl.getPlayerPresenter();
        mPlayerPresenter.registerCallback(this);
        initView();
        initEvent();
        startPlay();

        //TODO:测试一下播放
//        PlayerPresenterImpl playerPresenter = PlayerPresenterImpl.getPlayerPresenter();
//        playerPresenter.play();
    }

    private void startPlay() {
        if (mPlayerPresenter != null) {

            mPlayerPresenter.play();
        }
    }

    private void initEvent() {
        mPlayOrPauseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter.isPlay()) {
                    mPlayOrPauseBt.setImageResource(R.mipmap.stop_normal);
                    mPlayerPresenter.pause();
                }else {
                    mPlayOrPauseBt.setImageResource(R.mipmap.play_normal);
                    mPlayerPresenter.play();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterCallback(this);
            mPlayerPresenter = null;
        }
    }

    private void initView() {
        mPlayOrPauseBt = this.findViewById(R.id.play_or_pause_bt);
        mDurationTx = this.findViewById(R.id.track_duration);
        mCurPosTx = this.findViewById(R.id.current_position);
    }

    @Override
    public void onPlayStart() {

    }

    @Override
    public void onPlayPause() {

    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayError(XmPlayerException exception) {

    }

    @Override
    public void onPlayPre(Track track) {

    }

    @Override
    public void onPlayNext(Track track) {

    }

    @Override
    public void onPlayListLoaded(List<Track> trackList) {

    }

    @Override
    public void onSwitchPlayMode(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgress(long currentProgress, long total) {
        //
        if(total>1000 * 60 * 60){
            mTotal = mHourSimpleDateFormat.format(total);
            mCurPos = mHourSimpleDateFormat.format(currentProgress);
        }else {
            mTotal = mMinSimpleDateFormat.format(total);
            mCurPos = mMinSimpleDateFormat.format(currentProgress);
        }

        if (mDurationTx != null) {
            mDurationTx.setText(mTotal);
        }
//        更新进度
        if(mCurPosTx != null){
            mCurPosTx.setText(mCurPos);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinish() {

    }
}
