package com.chenjiahua.himalayafm.iu.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.base.BaseFragment;

public class RecommendFragment extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {

        View inflate = inflater.inflate(R.layout.fragment_recommend, container, false);
        return inflate;
    }
}