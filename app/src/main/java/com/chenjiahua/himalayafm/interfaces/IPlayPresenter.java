package com.chenjiahua.himalayafm.interfaces;

import com.chenjiahua.himalayafm.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayPresenter extends IBasePresenter<IPlayCallBack> {

    /**
     * 开始播放
     */
    void play();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 播放上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目的指定位置进行播放
     * @param index 节目在列表的位置
     */
    void playByIndex(int index);


    /**
     * 播放指定进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 当前播放器是否正在播放音乐
     * @return
     */
    boolean isPlay();


}
