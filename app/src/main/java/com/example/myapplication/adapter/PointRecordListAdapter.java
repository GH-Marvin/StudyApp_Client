package com.example.myapplication.adapter;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.example.myapplication.entity.PointRecord;
import com.example.myapplication.util.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class PointRecordListAdapter extends RecyclerView.Adapter<PointRecordListAdapter.InnerHolder> {

    private List<PointRecord> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public PointRecordListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PointRecordListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<PointRecord> pointRecordList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(pointRecordList);
        }
        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView eventName_tv,time_tv,gainPoint_tv;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(PointRecord pointRecord) {

            eventName_tv = itemView.findViewById(R.id.eventName_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            gainPoint_tv = itemView.findViewById(R.id.gainPoint_tv);
            eventName_tv.setText(pointRecord.getEventName());
            time_tv.setText(pointRecord.getCreateTime());
            if(pointRecord.getGainPoint()>=0){
                gainPoint_tv.setText("+"+pointRecord.getGainPoint());
            }else {
                gainPoint_tv.setText(String.valueOf(pointRecord.getGainPoint()));
            }


        }

    }
}
