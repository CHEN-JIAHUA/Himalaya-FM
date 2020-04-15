package com.chenjiahua.himalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private SimpleDateFormat mDetailDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDetailDuration = new SimpleDateFormat("mm:ss");
    private static final String TAG = "DetailListAdapter";
    private List<Track> mData = new ArrayList<>();
    private View mItemView;
    private OnItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public DetailListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_view, parent, false);
        return new InnerHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.InnerHolder holder, final int position) {
            //找到对应的控件，设置数据
            View itemView = holder.itemView;
//            顺序Id
            TextView orderTv = itemView.findViewById(R.id.order_text);
//            标题
            TextView detailItemTrackTitle = itemView.findViewById(R.id.detail_item_track_title);
//            播放次数
            TextView detailItemPlayCount = itemView.findViewById(R.id.detail_item_play_count);
//            持续时间
            TextView detailItemDuration  = itemView.findViewById(R.id.detail_item_duration);
//            更新时间
            TextView detailItemUpdateTime = itemView.findViewById(R.id.detail_item_update_time);

//            设置数据
        Track track = mData.get(position);
        orderTv.setText(track.getOrderNum() + 1 +"");
        detailItemTrackTitle.setText(track.getTrackTitle());
        detailItemPlayCount.setText(track.getPlayCount()+"");

        int durationMil = track.getDuration() * 1000;
        String duration = mDetailDuration.format(durationMil);
        detailItemDuration.setText(duration);
        String format = mDetailDate.format(track.getUpdatedAt());
        detailItemUpdateTime.setText(format);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击了"+ (position+1),Toast.LENGTH_SHORT).show();
//                TODO:详情页面的点击事件
                if (mItemClickListener != null) {
                    //参数需要有列表和位置
                    mItemClickListener.onItemClick(mData,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Track> tracks) {

        mData.clear();
        mData.addAll(tracks);
        LogUtils.d(TAG,"tracks dataList size -- > " + mData.size());
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(List<Track> data, int position);
    }
}
