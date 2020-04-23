package com.chenjiahua.himalayafm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.adapters.DetailListAdapter;
import com.chenjiahua.himalayafm.base.BaseActivity;
import com.chenjiahua.himalayafm.interfaces.IAlbumDetailViewCallback;
import com.chenjiahua.himalayafm.interfaces.IPlayCallBack;
import com.chenjiahua.himalayafm.presenters.AlbumDetailPresenterImpl;
import com.chenjiahua.himalayafm.presenters.PlayerPresenterImpl;
import com.chenjiahua.himalayafm.utils.ImageBlur;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, DetailListAdapter.OnItemClickListener, IPlayCallBack {

    private static final String TAG = "DetailActivity";
    private ImageView ivSmallCover;
    private ImageView ivLargeCover;
    private TextView trackTitle;
    private TextView trackAuthor;
    private AlbumDetailPresenterImpl albumDetailPresenter;
    private TextView mBookBtn;
    private RecyclerView mDetailRv;
    private DetailListAdapter detailListAdapter;
    private ImageView mDetailPlayControlIv;
    private TextView mDetailPlayControlTv;
    private PlayerPresenterImpl mPlayerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        //这个是播放专辑详情的presenter
        albumDetailPresenter = AlbumDetailPresenterImpl.getsInstance();
        albumDetailPresenter.registerCallback(this);

        //播放器的presenter
        mPlayerPresenter = PlayerPresenterImpl.getPlayerPresenter();
        mPlayerPresenter.registerCallback(this);
        updatePlayState(mPlayerPresenter.isPlay());
        initEvent();
    }

    private void updatePlayState(boolean play) {
        if (mDetailPlayControlIv != null && mDetailPlayControlTv != null) {
            mDetailPlayControlIv.setImageResource(play ? R.drawable.selector_detail_pause_control : R.drawable.selector_detail_play_control);
            mDetailPlayControlTv.setText(play? R.string.string_play_now : R.string.string_pause_now);
        }
    }

    private void initEvent() {
        mBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "订阅成功！", Toast.LENGTH_SHORT).show();
            }
        });

        mDetailPlayControlIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(TAG,"mDetailPlayControlIv Clicked ...");
                //TODO:执行改变播放状态
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                }else {
                    mPlayerPresenter.play();
                }
            }
        });

    }

    private void initView() {

        ivLargeCover = this.findViewById(R.id.iv_large_cover);
        ivSmallCover = this.findViewById(R.id.iv_small_cover);
        trackTitle = this.findViewById(R.id.track_title_text);
        trackAuthor = this.findViewById(R.id.track_author_name);
        mBookBtn = this.findViewById(R.id.button_2_book);

        //play control imageView TextView at detail layout
        mDetailPlayControlIv = this.findViewById(R.id.detail_play_control_iv);
        mDetailPlayControlTv = this.findViewById(R.id.detail_play_control_tv);

        mDetailRv = this.findViewById(R.id.detail_content_view);
        //recyclerView 的使用步骤
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDetailRv.setLayoutManager(linearLayoutManager);
        //创建适配器
        detailListAdapter = new DetailListAdapter();
        mDetailRv.setAdapter(detailListAdapter);
        mDetailRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = UIUtil.dip2px(view.getContext(), 3);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 3);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        detailListAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        if (detailListAdapter != null) {
            detailListAdapter.setData(tracks);
        }


    }

    @Override
    public void onAlbumLoaded(Album album) {
        //获取专辑的详情内容
        albumDetailPresenter.getAlbumDetail((int) album.getId(), 1);


        if (trackTitle != null) {
            trackTitle.setText(album.getAlbumTitle());
        }

        if (ivLargeCover != null) {
            Picasso.get().load(album.getCoverUrlLarge()).into(ivLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = ivLargeCover.getDrawable();
                    if (drawable != null) {
                        ImageBlur.makeBlur(ivLargeCover, DetailActivity.this);
                        LogUtils.d(TAG, "实现了毛玻璃的效果");
                    }
                }

                @Override
                public void onError(Exception e) {
                    LogUtils.d(TAG, "On Error === >" + e.getMessage());
                }

            });
        }

        if (ivSmallCover != null) {
            Picasso.get().load(album.getCoverUrlSmall()).into(ivSmallCover);
        }
    }

    @Override
    public void onItemClick(List<Track> data, int position) {
        skip2PlaybackView();
        setPlayListData(data, position);
    }

    private void setPlayListData(List<Track> data, int position) {
        //设置播放器的数据
        PlayerPresenterImpl playerPresenter = PlayerPresenterImpl.getPlayerPresenter();
        playerPresenter.setPlayList(data, position);
    }

    private void skip2PlaybackView() {

        //TODO:跳转到播放页面
        Intent intent = new Intent(this, PlaybackActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayStart() {
        //修改图标为暂停的状态，文字修改为正在播放
        updatePlayState(true);
    }

    @Override
    public void onPlayPause() {
        updatePlayState(false);
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
    public void onPlayProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinish() {

    }

    @Override
    public void onUpdateTrack(Track track, int index) {

    }

    @Override
    public void updatePlayListOrder(boolean isOrder) {

    }
}
