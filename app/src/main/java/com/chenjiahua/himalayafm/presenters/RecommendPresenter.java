package com.chenjiahua.himalayafm.presenters;

import com.chenjiahua.himalayafm.interfaces.IRecommendCallBack;
import com.chenjiahua.himalayafm.interfaces.IRecommendPresenter;
import com.chenjiahua.himalayafm.utils.Constants;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";

    private List<IRecommendCallBack> mCallBacks = new ArrayList<>();
    private List<Album> mCurrentRecommend = null;
    private List<Album> albumList = null;

    private RecommendPresenter() {
    }

    /**
     * 单例模式 -- 懒汉式
     */
    private static RecommendPresenter sInstance = null;
    public static RecommendPresenter getInstance(){
        if (sInstance == null) {
            synchronized (RecommendPresenter.class){
                if(sInstance == null){
                    sInstance = new RecommendPresenter();
                }
            }
        }
            return sInstance;
    }

    /**
     * 获取推荐内容
     */
    @Override
    public void getRecommendList() {
        if (albumList != null && albumList.size() > 0) {
            LogUtils.d(TAG,"getRecommendList -- > from list");
            handleRecommendResult(albumList);
            return;
        }
        updateLoading();
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtils.d(TAG,"thread name --  > " + Thread.currentThread());
                if (gussLikeAlbumList != null) {
                    albumList = gussLikeAlbumList.getAlbumList();
                    LogUtils.d(TAG,"album list size --- > " + albumList.size());
//          TODO：数据回来，更新UI
                    LogUtils.d(TAG,"albumList  --- >" + albumList);
                    handleRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"ERROR CODE --- >" + i + "  ERROR INFO : " + s);
                handleError();
            }
        });
    }

    private void handleError() {
        if (mCallBacks != null) {
            for (IRecommendCallBack callBack : mCallBacks) {
                callBack.onNetworkError();
            }
        }
    }

    /**
     * 返回数据的结果
     * @param albumList
     */
    private void handleRecommendResult(List<Album> albumList) {
        //判断presenter的数据
        if (albumList != null) {   //如果不为空，则成功
            if (albumList.size() == 0) {
                for (IRecommendCallBack callBack : mCallBacks) {
                    callBack.onEmpty();
                }
            }else {
                for (IRecommendCallBack callBack : mCallBacks) {
                    callBack.onRecommendListLoaded(albumList);
                    LogUtils.d(TAG,"onRecommendListLoaded " + albumList.size());
                }
                this.mCurrentRecommend = albumList;
            }
        }else {
            LogUtils.d(TAG,"albumList  == null ");
        }
    }

    private void updateLoading(){
        for (IRecommendCallBack callBack : mCallBacks) {
            callBack.onLoading();
        }

    }


    @Override
    public void registerViewCallBack(IRecommendCallBack callBack) {
        //如果没有包含的话就添加
        if (mCallBacks != null && !mCallBacks.contains(callBack)) {
            mCallBacks.add(callBack);
        }
    }

    @Override
    public void unRegisterViewCallBack(IRecommendCallBack callBack) {
       //取消注册
        if (mCallBacks != null) {
            mCallBacks.remove(callBack);
        }
    }
}
