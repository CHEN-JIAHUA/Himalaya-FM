package com.chenjiahua.himalayafm.presenters;

import com.chenjiahua.himalayafm.interfaces.IAlbumDetailViewCallback;
import com.chenjiahua.himalayafm.interfaces.IDetailPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenterImpl implements IDetailPresenter {

    List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private static AlbumDetailPresenterImpl sInstance = null;
    private Album mTargetAlbum = null;

    private AlbumDetailPresenterImpl() {
    }

    public static AlbumDetailPresenterImpl getsInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenterImpl.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenterImpl();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int categoryId, int page) {

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mTargetAlbum != null) {
                callback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback callback) {
            mCallbacks.remove(callback);
    }


    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }
}
