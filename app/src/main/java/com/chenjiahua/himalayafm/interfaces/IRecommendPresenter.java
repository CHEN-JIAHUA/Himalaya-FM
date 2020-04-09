package com.chenjiahua.himalayafm.interfaces;

public interface IRecommendPresenter {

    /**
     * 获取推荐内容
     */
    void getRecommendList();

    /**
     * 用于注册UI的回调
     * @param callBack
     */
    void registerViewCallBack(IRecommendCallBack callBack);

    /**
     * 用于注销UI的回调
     * @param callBack
     */
    void unRegisterViewCallBack(IRecommendCallBack callBack);
}
