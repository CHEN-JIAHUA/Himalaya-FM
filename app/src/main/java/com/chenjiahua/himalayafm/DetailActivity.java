package com.chenjiahua.himalayafm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chenjiahua.himalayafm.base.BaseActivity;
import com.chenjiahua.himalayafm.interfaces.IAlbumDetailViewCallback;
import com.chenjiahua.himalayafm.presenters.AlbumDetailPresenterImpl;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView ivSmallCover;
    private ImageView ivLargeCover;
    private TextView trackTitle;
    private TextView trackAuthor;
    private AlbumDetailPresenterImpl albumDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        albumDetailPresenter = AlbumDetailPresenterImpl.getsInstance();
        albumDetailPresenter.registerViewCallback(this);

    }

    private void initView() {

        ivLargeCover = this.findViewById(R.id.iv_large_cover);
        ivSmallCover = this.findViewById(R.id.iv_small_cover);
        trackTitle = this.findViewById(R.id.track_title_text);
        trackAuthor = this.findViewById(R.id.track_author_name);
    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
        if (trackTitle != null) {
            trackTitle.setText(album.getAlbumTitle());
        }

        if (ivLargeCover != null) {
            Picasso.get().load(album.getCoverUrlLarge()).into(ivLargeCover);
        }

        if (ivSmallCover != null) {
            Picasso.get().load(album.getCoverUrlSmall()).into(ivSmallCover);
        }
    }
}
