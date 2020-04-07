package com.chenjiahua.himalayafm.iu.fragment;

import android.graphics.Rect;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.adapters.RecommendListAdapter;
import com.chenjiahua.himalayafm.base.BaseFragment;
import com.chenjiahua.himalayafm.utils.Constants;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView recommendRv;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {

//        加载完成
        mRootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        //recyclerView 的使用
        // 1. 找到对应的控件
        recommendRv = mRootView.findViewById(R.id.recommend_content_list);
        // 2. 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = UIUtil.dip2px(getContext(),5);
                outRect.bottom = UIUtil.dip2px(getContext(),5);
                outRect.left = UIUtil.dip2px(getContext(),5);
                outRect.right = UIUtil.dip2px(getContext(),5);
            }
        });
        recommendRv.setLayoutManager(linearLayoutManager);
        //创建适配器
        mRecommendListAdapter = new RecommendListAdapter();
        //设置适配器
        recommendRv.setAdapter(mRecommendListAdapter);
//       去获取数据

        getRecommendData();

//        返回View 给界面显示
        return mRootView;
    }

    /**
     * 获取推荐内容 ：其实就是猜你喜欢这个接口
     */
    private void getRecommendData() {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtils.d(TAG,"thread name --  > " + Thread.currentThread());
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                  LogUtils.d(TAG,"album list size --- > " + albumList.size());
//          TODO：数据回来，更新UI
                    updateRecommendUi(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"ERROR CODE --- >" + i + "  ERROR INFO : " + s);
            }
        });
    }

    /**
     * 将数据更新到UI上
     * @param albumList
     */
    private void updateRecommendUi(List<Album> albumList) {
            //把数据设置给适配器，并且更新UI
        mRecommendListAdapter.setData(albumList);
    }
}
