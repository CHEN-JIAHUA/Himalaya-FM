package com.chenjiahua.himalayafm;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.chenjiahua.himalayafm.adapters.TrackCoverAdapter;
import com.chenjiahua.himalayafm.base.BaseActivity;
import com.chenjiahua.himalayafm.interfaces.IPlayCallBack;
import com.chenjiahua.himalayafm.presenters.PlayerPresenterImpl;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.chenjiahua.himalayafm.view.HiPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class PlaybackActivity extends BaseActivity implements IPlayCallBack, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlaybackActivity";
    private static final int BG_ANIM_DURATION = 200;
    private ImageView mPlayOrPauseBt;

    private PlayerPresenterImpl mPlayerPresenter;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mMinSimpleDateFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourSimpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    private String mTotal;
    private TextView mDurationTx;
    private TextView mCurPosTx;
    private String mCurPos;
    private SeekBar mSeekBar;
    private int mCurProgress = 0;
    private boolean isUserTouch = false;
    private ImageView mPlayPreControl;
    private ImageView mPlayNextControl;
    private TextView mPlaybackTrackTitle;
    private String mTrackTitleText;
    private TrackCoverAdapter mTrackCoverAdapter;
    private Track mTrack;
    private ViewPager mTrackCoverVp;
    private boolean mIsUserSlidePage = false;
    private ImageView mPlayModeBt;
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayModeMap = new HashMap<>();
    private XmPlayListControl.PlayMode mCurrentMode = PLAY_MODEL_LIST;

    static{
        sPlayModeMap.put(PLAY_MODEL_LIST,PLAY_MODEL_LIST_LOOP);
        sPlayModeMap.put(PLAY_MODEL_LIST_LOOP,PLAY_MODEL_RANDOM);
        sPlayModeMap.put(PLAY_MODEL_RANDOM,PLAY_MODEL_SINGLE_LOOP);
        sPlayModeMap.put(PLAY_MODEL_SINGLE_LOOP,PLAY_MODEL_LIST);
    }

    private ImageView mPlayListShowOrHide;
    private HiPopWindow mHiPopWindow;
    private ValueAnimator mEnterBgAnim;
    private ValueAnimator mExitBgAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        mPlayerPresenter = PlayerPresenterImpl.getPlayerPresenter();
        initView();
        mPlayerPresenter.registerCallback(this);
        mPlayerPresenter.getPlayList();
        initEvent();
        initAnimator();

        //TODO:测试一下播放
//        PlayerPresenterImpl playerPresenter = PlayerPresenterImpl.getPlayerPresenter();
//        playerPresenter.play();

    }

    private void initAnimator() {
        mEnterBgAnim = ValueAnimator.ofFloat(1.0f,0.7f);
        mEnterBgAnim.setDuration(BG_ANIM_DURATION);
        //设置一个监听器
        mEnterBgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                LogUtils.d(TAG,"VALUE --- > " + value);
                updateBgAlpha(value);
            }
        });
        //退出的
        mExitBgAnim = ValueAnimator.ofFloat(0.7f,1.0f);
        mExitBgAnim.setDuration(200);
        //设置监听器
        mExitBgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                updateBgAlpha(value);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mPlayOrPauseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter.isPlay()) {
                    mPlayOrPauseBt.setImageResource(R.drawable.selector_play_stop);
                    mPlayerPresenter.pause();
                } else {
                    mPlayOrPauseBt.setImageResource(R.drawable.selector_player_play);
                    mPlayerPresenter.play();
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    LogUtils.d(TAG,"is From User --- >" + fromUser);
                    mCurProgress = progress;
                    LogUtils.d(TAG,"progress --- > " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserTouch = false;
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.seekTo(mCurProgress);
                }
            }
        });

        mPlayPreControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });

        mPlayNextControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });

        mTrackCoverVp.addOnPageChangeListener(this);

        mTrackCoverVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePage = true;
                        break;
                }
                return false;
            }
        });

        mPlayModeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理播放模式的切换   PlayMode
                //默认播放器模式是         PLAY_MODEL_LIST            列表循环
                //                       PLAY_MODEL_SINGLE_LOOP     单曲循环播放
                //                       PLAY_MODEL_LIST_LOOP       列表循环
                //                       PLAY_MODEL_RANDOM          随机播放
                //执行播放状态的切换
                //根据当前的 Mode 获取另一个Mode
                switchPlayMode();
            }
        });

        mPlayListShowOrHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TEST
            //Toast.makeText(v.getContext(),"CLICK SHOW OR HIDE TRACK LIST...",Toast.LENGTH_SHORT).show();
            //TODO：显示播放列表   HiPopWindow
                mHiPopWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                //处理一下背景
                mEnterBgAnim.start();
            }
        });

        mHiPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //POP窗体消失后，恢复透明度
//                updateBgAlpha(1.0f);
                mExitBgAnim.start();
            }
        });
        //TODO:   tip：需要进行学习
        //点击歌单列表进行播放
        mHiPopWindow.setPlayListItemClickListener(new HiPopWindow.PlayListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playByIndex(position);
                }
            }
        });

        mHiPopWindow.setOnPlayModeClickListener(new HiPopWindow.PlayModeIconClickListener() {
            @Override
            public void playModeClick() {
                switchPlayMode();

            }
        });
    }

    private void switchPlayMode() {
        XmPlayListControl.PlayMode playMode = sPlayModeMap.get(mCurrentMode);
        //修改播放模式
        if (mPlayerPresenter != null) {
            mPlayerPresenter.switchPlayMode(playMode);
            mCurrentMode = playMode;
            updatePlayModeBtnImg();
        }
    }

    private void updateBgAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    /**
     * 根据当前的状态，更新播放模式
     *
     * @param
     */
    private void updatePlayModeBtnImg() {
        int resId = R.drawable.selector_play_model_list;
        switch(mCurrentMode){
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_play_model_list;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_play_model_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_play_model_list_loop;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_play_single_loop;
                break;
        }
        mPlayModeBt.setImageResource(resId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterCallback(this);
            mPlayerPresenter = null;
        }
    }

    @Override
    public void onPlayStart() {

    }

    private void initView() {
        mPlayOrPauseBt = this.findViewById(R.id.play_or_pause_bt);
        mDurationTx = this.findViewById(R.id.track_duration);
        mCurPosTx = this.findViewById(R.id.current_position);
        mSeekBar = this.findViewById(R.id.track_seek_bar);
        mPlayPreControl = this.findViewById(R.id.play_pre_bt);
        mPlayNextControl = this.findViewById(R.id.play_next_bt);
        mPlaybackTrackTitle = this.findViewById(R.id.track_playback_title);
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mPlaybackTrackTitle.setText(mTrackTitleText);
        }

        mTrackCoverVp = this.findViewById(R.id.track_playback_cover);
        //创建适配器
        mTrackCoverAdapter = new TrackCoverAdapter();
        //设置适配器
        mTrackCoverVp.setAdapter(mTrackCoverAdapter);

        //切换播放模式的按钮
        mPlayModeBt = this.findViewById(R.id.switch_play_mode_bt);
        //显示或者隐藏播放歌单列表
        mPlayListShowOrHide = this.findViewById(R.id.play_list_show_or_hide_bt);
        mHiPopWindow = new HiPopWindow();

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
//对这个Adapter 进行判空
        if (mTrackCoverAdapter != null) {
            mTrackCoverAdapter.setData(trackList);
        }
        if (mHiPopWindow != null) {
            mHiPopWindow.setListData(trackList);
        }

//        LogUtils.d(TAG,"trackList ---- > " + trackList);
    }

    @Override
    public void onSwitchPlayMode(XmPlayListControl.PlayMode playMode) {
        //更新播放模式，并且修改UI
        mCurrentMode = playMode;

        //更新pop里的playMode
        mHiPopWindow.updatePlayModeBtImg(mCurrentMode);
        updatePlayModeBtnImg();

    }



    @Override
    public void onPlayProgressChange(int currentProgress, int total) {
        //
        mSeekBar.setMax(total);
        if (total > 1000 * 60 * 60) {
            mTotal = mHourSimpleDateFormat.format(total);
            mCurPos = mHourSimpleDateFormat.format(currentProgress);
        } else {
            mTotal = mMinSimpleDateFormat.format(total);
            mCurPos = mMinSimpleDateFormat.format(currentProgress);
        }

        if (mDurationTx != null) {
            mDurationTx.setText(mTotal);
        }

        if (mCurPosTx != null) {
            mCurPosTx.setText(mCurPos);
        }
        mCurProgress = currentProgress;
//        更新进度
//        计算当前的进度
        if (!isUserTouch) {
            mSeekBar.setProgress(mCurProgress);
//            LogUtils.d(TAG,"User Touch to change current Progress --- > " + mCurProgress);
        }


    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinish() {

    }

    @Override
    public void onUpdateTrack(Track track,int index) {
        this.mTrackTitleText = track.getTrackTitle();
        if (mPlaybackTrackTitle != null) {
            mPlaybackTrackTitle.setText(mTrackTitleText);
//            LogUtils.d(TAG,"onUpdateTrack -- > " + mPlaybackTrackTitle.getText());
        }
        //当节目改变的时候，我们就获取当前的播放的位置
        //TODO：
        if (mTrackCoverVp != null) {
                mTrackCoverVp.setCurrentItem(index,true);
        }
        //设置播放列表里相对应的当前播放位置
        if (mHiPopWindow != null) {
            mHiPopWindow.setCurPlayPos(index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.d(TAG,"POSITION --- > " + position);
        if (mPlayerPresenter != null && mIsUserSlidePage) {
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSlidePage = false;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
