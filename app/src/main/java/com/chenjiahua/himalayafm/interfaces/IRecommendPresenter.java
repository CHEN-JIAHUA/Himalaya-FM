package com.chenjiahua.himalayafm.interfaces;

import com.chenjiahua.himalayafm.base.IBasePresenter;

public interface IRecommendPresenter extends IBasePresenter<IRecommendCallBack> {

    /**
     * 获取推荐内容
     */
    void getRecommendList();


}
