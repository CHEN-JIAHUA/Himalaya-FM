package com.chenjiahua.himalayafm;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chenjiahua.himalayafm.base.BaseActivity;
import com.chenjiahua.himalayafm.presenters.PlayerPresenterImpl;

public class PlaybackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        //TODO:测试一下播放
        PlayerPresenterImpl playerPresenter = PlayerPresenterImpl.getPlayerPresenter();
        playerPresenter.play();
    }
}
