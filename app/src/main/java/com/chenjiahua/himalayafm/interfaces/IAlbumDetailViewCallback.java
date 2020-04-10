package com.chenjiahua.himalayafm.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallback {

    /**
     * 专辑详情内容加载
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 把 Album 传给 UI使用
     * @param album
     */
    void onAlbumLoaded(Album album);
}
