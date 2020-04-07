package com.chenjiahua.himalayafm.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendCallBack {

    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 加载更多数据的结果
     */
    void onLoaderMore(List<Album> result);

    /**
     * 下拉刷新数据的结果
     */
    void onRefreshMore(List<Album> result);
}
