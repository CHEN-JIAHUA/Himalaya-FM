package com.chenjiahua.himalayafm.iu.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.base.BaseFragment;
import com.chenjiahua.himalayafm.utils.Constants;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {

//        加载完成
        View inflate = inflater.inflate(R.layout.fragment_recommend, container, false);
//        去获取数据
        getRecommendData();

//        返回View 给界面显示

        return inflate;
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
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        for (Album album : albumList) {
                            LogUtils.d(TAG,"album title ---->" + album.getAlbumTitle());
                            LogUtils.d(TAG,"album intro ---->" + album.getAlbumIntro());
                            LogUtils.d(TAG,"album  --- > " + album.getCoverUrlSmall());
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"ERROR CODE --- >" + i + "  ERROR INFO : " + s);
            }
        });
    }
}
