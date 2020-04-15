package com.chenjiahua.himalayafm.presenters;

import com.chenjiahua.himalayafm.interfaces.IAlbumDetailViewCallback;
import com.chenjiahua.himalayafm.interfaces.IDetailPresenter;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenterImpl implements IDetailPresenter {

    private static final String TAG = "AlbumDetailPresenterImpl";
    List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private static AlbumDetailPresenterImpl sInstance = null;
    private Album mTargetAlbum = null;
    private List<Track> mCurrentDetail = null;
    private List<Track> mTracks;

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
        //根据Id 来获取
        Map<String,String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, categoryId+"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                LogUtils.d(TAG,"thread name --  > " + Thread.currentThread());
                if (trackList != null) {
                    mTracks = trackList.getTracks();
                    LogUtils.d(TAG,"tracks size --- > " + mTracks.size());
//          TODO：数据回来，更新UI
                    LogUtils.d(TAG,"tracks  --- >" + mTracks);
                    handleDetailResult(mTracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"ERROR CODE -- > " + i + "   ERROR INFO --- > " + s );
            }
        });

    }

    private void handleDetailResult(List<Track> tracks) {
        if(tracks != null){
            if(tracks.size()!= 0){
                //获得的数据不为0
                for (IAlbumDetailViewCallback callback : mCallbacks) {
                    callback.onDetailListLoaded(tracks);
                    LogUtils.d(TAG,"tracks size --- > " + tracks.size());
                }
                this.mCurrentDetail = tracks;
            }else {
                //TODO:执行数据为0的
            }
        }

    }

    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }

    @Override
    public void registerCallback(IAlbumDetailViewCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mTargetAlbum != null) {
                callback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterCallback(IAlbumDetailViewCallback callback) {
        mCallbacks.remove(callback);
    }
}
