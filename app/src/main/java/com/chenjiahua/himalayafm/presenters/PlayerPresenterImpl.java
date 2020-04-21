package com.chenjiahua.himalayafm.presenters;



import android.content.Context;
import android.content.SharedPreferences;

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
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class PlayerPresenterImpl implements IPlayPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private List<IPlayCallBack> mIPlayCallBacks = new ArrayList<>();
    private static final String TAG = "PlayerPresenterImpl";
    private final XmPlayerManager mXmPlayerManager;
    private String mTrackTitle;
    private Track mTrack;
    private int playIndex = 0;
    private final SharedPreferences mPlayModeSp;

    public static final int PLAY_MODEL_LIST_INT = 0;
    public static final int PLAY_MODEL_SINGLE_LOOP_INT = 1;
    public static final int PLAY_MODEL_LIST_LOOP_INT= 2;
    public static final int PLAY_MODEL_RANDOM_INT= 3;

//    SharePreferences key and name
    public static String PLAY_MODEL_SP_NAME = "playMode";
    public static String PLAY_MODEL_SP_KEY = "playKey";
    private XmPlayListControl.PlayMode mCurPlayMode = PLAY_MODEL_LIST;


    private PlayerPresenterImpl() {
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关的接口
        mXmPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关的接口
        mXmPlayerManager.addPlayerStatusListener(this);
        //需要记录当前的播放模式 -- sharePreference
        mPlayModeSp = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODEL_SP_NAME, Context.MODE_PRIVATE);
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
        LogUtils.d(TAG,"setPlayList === " + mTrackTitle);
        if (mXmPlayerManager != null) {
            mXmPlayerManager.setPlayList(trackList,index);
            this.playIndex = index;
            LogUtils.d(TAG,"set playList Successful");
            isPlayListSet = true;
            mTrack = trackList.get(index);
//            mTrackTitle = track.getTrackTitle();

        }else {

            LogUtils.d(TAG,"mXmPlayerManager is null");
        }
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mXmPlayerManager.play();
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
        mXmPlayerManager.playPre();

    }

    @Override
    public void playNext() {
        mXmPlayerManager.playNext();
    }

    /**
     * 修改播放状态
     * @param mode
     */
    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        this.mCurPlayMode = mode;
        if (mXmPlayerManager != null) {
            mXmPlayerManager.setPlayMode(mCurPlayMode);
            //通知UI更新播放模式
            for (IPlayCallBack playCallBack : mIPlayCallBacks) {
                playCallBack.onSwitchPlayMode(mCurPlayMode);
            }

            SharedPreferences.Editor editor = mPlayModeSp.edit();
            editor.putInt(PLAY_MODEL_SP_KEY, getIntByPlayMode(mCurPlayMode));
            editor.commit();
        }
    }
    private int getIntByPlayMode(XmPlayListControl.PlayMode mode){
        switch (mode){
            case PLAY_MODEL_SINGLE_LOOP:
                return PLAY_MODEL_SINGLE_LOOP_INT;
            case PLAY_MODEL_LIST_LOOP:
                return PLAY_MODEL_LIST_LOOP_INT;
            case PLAY_MODEL_RANDOM:
                return PLAY_MODEL_RANDOM_INT;
            case PLAY_MODEL_LIST:
                return PLAY_MODEL_LIST_INT;
        }
        return PLAY_MODEL_LIST_INT;
    }

    private XmPlayListControl.PlayMode getPlayModeByInt(int modeIndex){
        switch (modeIndex){
            case PLAY_MODEL_SINGLE_LOOP_INT:
                return PLAY_MODEL_SINGLE_LOOP;
            case PLAY_MODEL_LIST_LOOP_INT:
                return PLAY_MODEL_LIST_LOOP;
            case PLAY_MODEL_RANDOM_INT:
                return PLAY_MODEL_RANDOM;
            case PLAY_MODEL_LIST_INT:
                return PLAY_MODEL_LIST;
        }
        return PLAY_MODEL_LIST;
    }

    @Override
    public void getPlayList() {
        if (mXmPlayerManager != null) {
            List<Track> playList = mXmPlayerManager.getPlayList();
            for (IPlayCallBack playCallBack : mIPlayCallBacks) {
                playCallBack.onPlayListLoaded(playList);
            }
        }


    }

    @Override
    public void playByIndex(int index) {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        mXmPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void registerCallback(IPlayCallBack iPlayCallBack) {
        iPlayCallBack.onUpdateTrack(mTrack, playIndex);
        int modeInt = mPlayModeSp.getInt(PLAY_MODEL_SP_KEY, PLAY_MODEL_LIST_INT);
        mCurPlayMode = getPlayModeByInt(modeInt);
        iPlayCallBack.onSwitchPlayMode(mCurPlayMode);
        if (!mIPlayCallBacks.contains(iPlayCallBack)) {
            mIPlayCallBacks.add(iPlayCallBack);
        }
//        LogUtils.d(TAG,"registerCallback --- > " + mTrack.getTrackTitle());
    }

    @Override
    public void unRegisterCallback(IPlayCallBack iPlayCallBack) {
            mIPlayCallBacks.remove(iPlayCallBack);
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
        mXmPlayerManager.setPlayMode(mCurPlayMode);
        if (mXmPlayerManager.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
            mXmPlayerManager.play();
        }
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        if (lastModel != null) {
            LogUtils.d(TAG,"onSoundSwitch..." + lastModel.getKind());
        }
        LogUtils.d(TAG,"onSoundSwitch..." + curModel.getKind());

        this.playIndex = mXmPlayerManager.getCurrentIndex();

        if (curModel instanceof Track) {
            Track curTrack = (Track) curModel;
            mTrack = curTrack;
            for (IPlayCallBack playCallBack : mIPlayCallBacks) {
                playCallBack.onUpdateTrack(curTrack, playIndex);
            }
        }



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
            playCallBack.onPlayProgressChange(currPos,duration);
        }
        LogUtils.d(TAG,"onPlayProgressChange currPos -- > "+ currPos + "   duration ==> " + duration );
        //TODO: 保存当前的播放时间，跟声音时长 回调？
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtils.d(TAG,"onError info -- > " + e.getMessage());
        return false;
    }
    //===================  播放状态相关的接口 end ========================

}
