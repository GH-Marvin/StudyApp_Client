package com.example.myapplication.adapter;



import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activity.ScoreActivity;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.util.XToastUtils;

import java.util.ArrayList;
import java.util.List;




public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.InnerHolder> {

    private List<AnswerForm> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ScoreListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListAdapter.InnerHolder holder, final int position) {
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

    public void setData(List<AnswerForm> answerFormList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(answerFormList);
        }
        //更新UI
        notifyDataSetChanged();
    }
    public AnswerForm getData(int position){
        return mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView check_btn;
        String form_id;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(AnswerForm answerForm) {
            //TODO
            check_btn = itemView.findViewById(R.id.check_btn);
            form_id = answerForm.getFormId();
            TextView saveTime_tv = itemView.findViewById(R.id.saveTime_tv);
            TextView score_tv = itemView.findViewById(R.id.score_tv);
            Log.e("link",answerForm.getSaveTime());
            saveTime_tv.setText(" "+answerForm.getSaveTime());
            score_tv.setText(String.valueOf(answerForm.getScore())+"分");
        }

    }
}
