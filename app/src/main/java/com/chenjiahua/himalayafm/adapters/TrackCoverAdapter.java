package com.chenjiahua.himalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import com.chenjiahua.himalayafm.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackCoverAdapter extends PagerAdapter {

    private List<Track> mData = new ArrayList<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_cover, container, false);
        container.addView(itemView);
        //设置数据
        //找到控件

        ImageView playTrackLargeCover = itemView.findViewById(R.id.playback_track_large_cover);
        Track track = mData.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();
        Picasso.get().load(coverUrlLarge).into(playTrackLargeCover);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);  //object 这里指的是itemView
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<Track> trackList) {
        mData.clear();
        mData.addAll(trackList);
        notifyDataSetChanged();

    }
}
