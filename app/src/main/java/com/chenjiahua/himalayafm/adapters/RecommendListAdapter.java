package com.chenjiahua.himalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjiahua.himalayafm.R;
import com.chenjiahua.himalayafm.utils.LogUtils;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    private static final String TAG = "RecommendListAdapter";
    private List<Album> mData = new ArrayList<>();
    private View mItemView;
    private ImageView albumCoverIv;
    private TextView albumTitleTv;
    private TextView albumIntroTv;
    private TextView albumPlayCount;
    private TextView albumTrackCount;
    private OnItemClickListener mItemListener = null;


    @NonNull
    @Override
    public RecommendListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_content,parent,false);
        return new InnerHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendListAdapter.InnerHolder holder, int position) {
        //设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.onItemClick((Integer) v.getTag());
                }
                LogUtils.d(TAG,"click item position --- > " + v.getTag());
            }
        });
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
//        更新一下UI
        notifyDataSetChanged();

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到各个控件
            albumCoverIv = itemView.findViewById(R.id.recommend_url_icon_large);
            albumTitleTv = itemView.findViewById(R.id.text_title);
            albumIntroTv = itemView.findViewById(R.id.text_intro);
            albumPlayCount = itemView.findViewById(R.id.text_play_count);
            albumTrackCount = itemView.findViewById(R.id.text_track_count);

            //设置数据
            albumTitleTv.setText(album.getAlbumTitle());
            albumIntroTv.setText(album.getAlbumIntro());
            albumPlayCount.setText(album.getPlayCount()+"");
            albumTrackCount.setText(album.getEstimatedTrackCount()+"");

            Picasso.get().load(album.getCoverUrlLarge())
                    .into(albumCoverIv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
