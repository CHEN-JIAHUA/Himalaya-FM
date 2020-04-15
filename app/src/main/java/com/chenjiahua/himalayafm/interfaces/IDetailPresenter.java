package com.chenjiahua.himalayafm.interfaces;


import com.chenjiahua.himalayafm.base.IBasePresenter;

public interface IDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

    /**
     * 下拉刷新内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多数据
     */
    void loadMore();

    /**
     * 获取专辑详情数据
     * @param categoryId
     * @param page
     */
    void getAlbumDetail(int categoryId,int page);



}
