package com.chenjiahua.himalayafm.iu.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.DetailActivity;
import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.adapters.RecommendListAdapter;
import com.chenjiahua.himalayafm.base.BaseFragment;
import com.chenjiahua.himalayafm.interfaces.IRecommendCallBack;
import com.chenjiahua.himalayafm.presenters.AlbumDetailPresenterImpl;
import com.chenjiahua.himalayafm.presenters.RecommendPresenter;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.chenjiahua.himalayafm.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;


import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendCallBack, UILoader.OnRetryClickListener, RecommendListAdapter.OnItemClickListener {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView recommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter recommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater inflater, ViewGroup container) {
       LogUtils.d(TAG,"onSubViewLoaded");
        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                LogUtils.d(TAG,"getSuccessView...");
                return createSuccessView(inflater,container);

            }
        };

//       获取到逻辑层的对象
        recommendPresenter = RecommendPresenter.getInstance();
        recommendPresenter.registerViewCallBack(this);
        //获取推荐列表
        recommendPresenter.getRecommendList();
//        返回View 给界面显示

        //跟父类解绑
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        mUiLoader.setOnRetryClickListener(this);

        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater inflater, ViewGroup container) {
        LogUtils.d(TAG,"createSuccessView...");
        mRootView = inflater.inflate(R.layout.fragment_recommend,container,false);

        //recyclerView 的使用
        // 1. 找到对应的控件
        recommendRv = mRootView.findViewById(R.id.recommend_content_list);
        // 2. 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommendRv.setLayoutManager(linearLayoutManager);
        recommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });
        //创建适配器
        mRecommendListAdapter = new RecommendListAdapter();
        LogUtils.d(TAG,"创建适配器");
        //设置适配器
        recommendRv.setAdapter(mRecommendListAdapter);
        mRecommendListAdapter.setOnItemClickListener(this);
        return mRootView;
    }



    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtils.d(TAG," result --- > " + result);  // 数据是存在的
        //把获取到的数据更新给Adapter
         mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        mRecommendListAdapter.setData(result);

        LogUtils.d(TAG," ================= ");


    }

    @Override
    public void onNetworkError() {
        LogUtils.d(TAG,"onNetworkError");
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        LogUtils.d(TAG,"onEmpty");
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtils.d(TAG,"onLoading");
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //todo:取消接口的注册，避免内存泄漏
        if (recommendPresenter != null) {
            recommendPresenter.unRegisterViewCallBack(this);
        }

    }

    @Override
    public void onRetryClick() {
        //重新获取推荐列表
        if (recommendPresenter != null) {
            recommendPresenter.getRecommendList();
            LogUtils.d(TAG,"成功获取推荐列表");
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenterImpl.getsInstance().setTargetAlbum(album);
        //执行点击事件
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
