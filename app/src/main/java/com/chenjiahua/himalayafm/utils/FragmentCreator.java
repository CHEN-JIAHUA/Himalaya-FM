package com.chenjiahua.himalayafm.utils;

import com.chenjiahua.himalayafm.base.BaseFragment;
import com.chenjiahua.himalayafm.iu.fragment.HistoryFragment;
import com.chenjiahua.himalayafm.iu.fragment.RecommendFragment;
import com.chenjiahua.himalayafm.iu.fragment.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentCreator {
    private static Map<Integer, BaseFragment> sCache = new HashMap<>();


    public final static int INDEX_RECOMMEND = 0;
    public final static int INDEX_SUBSCRIPTION = 1;
    public final static int INDEX_HISTORY = 2;

    public final static int PAGE_COUNT = 3;

    public static BaseFragment getWhichPageFragment(int pageIndex) {

        BaseFragment baseFragment = sCache.get(pageIndex);
        //baseFragment存在的话就不创建，直接返回出去
        if (baseFragment != null) {
            return baseFragment;
        }

        switch (pageIndex) {
            case INDEX_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;
                default:return null;
        }
        //这里只是做一个缓存
        sCache.put(pageIndex, baseFragment);

        return baseFragment;
    }
}
