package com.chenjiahua.himalayafm.interfaces;


import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;


/**
 * 播放器回调
 */
public interface IPlayCallBack {

    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 暂停播放
     */
    void onPlayPause();

    /**
     * 停止播放
     */
    void onPlayStop();

    /**
     * 播放器错误
     */
    void onPlayError(XmPlayerException exception);

    void onPlayPre(Track track);

    void onPlayNext(Track track);

    void onPlayListLoaded(List<Track> trackList);

    void onSwitchPlayMode(XmPlayListControl.PlayMode playMode);

    /**
     * 进度条的改变
     * @param currentProgress
     * @param total
     */
    void onPlayProgressChange(int currentProgress, int total );


    /**
     * 广告加载
     */
    void onAdLoading();

    /**
     * 广告结束
     */
    void onAdFinish();

    /**
     * 更新播放页面的专辑主题
     * @param track
     */
    void onUpdateTrack(Track track ,int index);

}
