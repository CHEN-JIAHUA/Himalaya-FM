package com.chenjiahua.himalayafm.presenters;

import com.chenjiahua.himalayafm.base.BaseApplication;
import com.chenjiahua.himalayafm.interfaces.IPlayCallBack;
import com.chenjiahua.himalayafm.interfaces.IPlayPresenter;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenterImpl implements IPlayPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private List<IPlayCallBack> mIPlayCallBacks = new ArrayList<>();
    private static final String TAG = "PlayerPresenterImpl";
    private final XmPlayerManager mXmPlayerManager;

    private PlayerPresenterImpl() {
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关的接口
        mXmPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关的接口
        mXmPlayerManager.addPlayerStatusListener(this);
    }

    private static PlayerPresenterImpl sPlayerPresenter;

    public static PlayerPresenterImpl getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenterImpl.class){
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenterImpl();
                }
            }
        }
        return sPlayerPresenter;
    }


    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> trackList,int index){
        if (mXmPlayerManager != null) {
            mXmPlayerManager.setPlayList(trackList,index);
            LogUtils.d(TAG,"set playList Successful");
            isPlayListSet = true;
        }else {
            LogUtils.d(TAG,"mXmPlayerManager is null");
        }
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mXmPlayerManager.play();
            LogUtils.d(TAG,"正在播放");
        }

    }

    @Override
    public void pause() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.pause();
        }

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public boolean isPlay() {
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void registerCallback(IPlayCallBack iPlayCallBack) {
        if (!mIPlayCallBacks.contains(iPlayCallBack)) {
            mIPlayCallBacks.add(iPlayCallBack);
        }
    }

    @Override
    public void unRegisterCallback(IPlayCallBack iPlayCallBack) {
        if (mIPlayCallBacks.contains(iPlayCallBack)) {
            mIPlayCallBacks.remove(iPlayCallBack);
        }

    }

//===================  广告相关的回调  start ========================

    /**
     *
     */
    @Override
    public void onStartGetAdsInfo() {
        LogUtils.d(TAG,"onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtils.d(TAG,"onGetAdsInfo");

    }

    @Override
    public void onAdsStartBuffering() {
        LogUtils.d(TAG,"onAdsStartBuffering");

    }

    @Override
    public void onAdsStopBuffering() {
        LogUtils.d(TAG,"onAdsStopBuffering");

    }

    @Override
    public void onStartPlayAds(Advertis ad, int position) {
        LogUtils.d(TAG,"onStartPlayAds --> " + ad.toString() + "  position --- > " + position);
    }

    @Override
    public void onCompletePlayAds() {
        LogUtils.d(TAG,"onCompletePlayAds");

    }

    @Override
    public void onError(int what, int extra) {
        LogUtils.d(TAG,"onError : what ===> "+ what + "  extra ===>" + extra);

    }

    //===================  广告相关的回调  end ========================

//===================  播放状态相关的接口  start ========================

    @Override
    public void onPlayStart() {
        LogUtils.d(TAG,"onPlayStart...");
        for (IPlayCallBack iPlayCallBack : mIPlayCallBacks) {
            iPlayCallBack.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtils.d(TAG,"onPlayPause...");
        for (IPlayCallBack iPlayCallBack : mIPlayCallBacks) {
            iPlayCallBack.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtils.d(TAG,"onPlayStop...");
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtils.d(TAG,"onSoundPlayComplete...");
    }

    @Override
    public void onSoundPrepared() {
        LogUtils.d(TAG,"onSoundPrepared...");
    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        LogUtils.d(TAG,"onSoundSwitch...");
    }

    @Override
    public void onBufferingStart() {
        LogUtils.d(TAG,"onBufferingStart...");
    }

    @Override
    public void onBufferingStop() {
        LogUtils.d(TAG,"onBufferingStop...");
    }

    /**
     * 缓冲进度回调
     * @param percent
     */
    @Override
    public void onBufferProgress(int percent) {
        LogUtils.d(TAG,"onBufferProgress ==> " + percent);
    }

    /**
     * 播放进度回调
     * @param currPos
     * @param duration
     */
    @Override
    public void onPlayProgress(int currPos, int duration) {
        //单位是 毫秒
        for (IPlayCallBack playCallBack : mIPlayCallBacks) {
            playCallBack.onPlayProgress(currPos,duration);
        }
        LogUtils.d(TAG,"onPlayProgress currPos -- > "+ currPos + "   duration ==> " + duration );
        //TODO: 保存当前的播放时间，跟声音时长 回调？
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtils.d(TAG,"onError info -- > " + e.getMessage());
        return false;
    }
    //===================  播放状态相关的接口 end ========================

}
