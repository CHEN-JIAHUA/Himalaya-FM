package com.chenjiahua.himalayafm.interfaces;


public interface IDetailPresenter {

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


    void registerViewCallback(IAlbumDetailViewCallback callback);

    void unRegisterViewCallback(IAlbumDetailViewCallback callback);
}
