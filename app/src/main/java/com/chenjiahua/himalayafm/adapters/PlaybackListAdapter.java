package com.chenjiahua.himalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.base.BaseApplication;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackListAdapter.InnerHolder> {
    private static final String TAG = "PlaybackListAdapter";
    private List<Track> mPlaybackTrackData = new ArrayList<>();
    private View mItemView;
    private TextView mPlayListTrackTitle;
    private ImageView mPlayIcon;
    private int mCurPlayTrackPos = 0;
    @NonNull
    @Override
    public PlaybackListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playback_list, parent, false);

        return new InnerHolder(mItemView);
    }

    //这里好像调用了好多次
    @Override
    public void onBindViewHolder(@NonNull PlaybackListAdapter.InnerHolder holder, int position) {
        Track track = mPlaybackTrackData.get(position);
        LogUtils.d(TAG,"mPlaybackTrackData  ----  >  +" + mPlaybackTrackData +"mPlayListTrackTitle  --- > " + mPlayListTrackTitle);
        mPlayListTrackTitle = holder.itemView.findViewById(R.id.play_list_track_title);
        mPlayListTrackTitle.setText(track.getTrackTitle());
        //设置字体的颜色
        mPlayListTrackTitle.setTextColor(BaseApplication.getAppContext().getResources().getColor(mCurPlayTrackPos == position ? R.color.himalaya_color : R.color.play_list_text_color));

        //找播放状态的图标
        //        mCurPlayTrackPos = position;
        mPlayIcon = holder.itemView.findViewById(R.id.play_status_icon);
        mPlayIcon.setVisibility(mCurPlayTrackPos == position?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return mPlaybackTrackData.size();
        //TODO:
    }

    public void setData(List<Track> data) {
        //先清空
        mPlaybackTrackData.clear();
        //吧数据全部添加
        mPlaybackTrackData.addAll(data); //size -- 0
        LogUtils.d(TAG,"data loaded -- > " + data);
        //
        notifyDataSetChanged();
    }

    public void setCurPlayPos(int index) {
        mCurPlayTrackPos = index;
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
