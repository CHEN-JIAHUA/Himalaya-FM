package com.chenjiahua.himalayafm.presenters;

import com.chenjiahua.himalayafm.base.BaseApplication;
import com.chenjiahua.himalayafm.interfaces.IPlayCallBack;
import com.chenjiahua.himalayafm.interfaces.IPlayPresenter;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerPresenterImpl implements IPlayPresenter {

    private static final String TAG = "PlayerPresenterImpl";
    private final XmPlayerManager mXmPlayerManager;

    public PlayerPresenterImpl() {
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());

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
    public void registerCallback(IPlayCallBack iPlayCallBack) {

    }

    @Override
    public void unRegisterCallback(IPlayCallBack iPlayCallBack) {

    }
}
