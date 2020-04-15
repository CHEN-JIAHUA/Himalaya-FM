package com.chenjiahua.himalayafm.base;

public interface IBasePresenter<T> {

    void registerCallback(T t);

    void unRegisterCallback(T t);

}
