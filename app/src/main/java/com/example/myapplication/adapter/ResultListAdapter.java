package com.example.myapplication.adapter;



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
import com.example.myapplication.entity.AnswerResult;
import com.example.myapplication.util.XToastUtils;

import java.util.ArrayList;
import java.util.List;




public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.InnerHolder> {

    private List<AnswerResult> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ResultListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        holder.tv_title.setText(String.valueOf(position+1)+"、"+mData.get(position).getQuestion().getTitle()+" ("+mData.get(position).getQuestion().getScore()+"分)");
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                    XToastUtils.toast("您点击了题目"+String.valueOf(position+1)+"!");
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

    public void setData(List<AnswerResult> answerResultList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(answerResultList);
        }
        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_type;
        TextView tv_score;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(AnswerResult answerResult) {
            //TODO
            ImageView iv_checked = itemView.findViewById(R.id.checked_iv);
            tv_title = itemView.findViewById(R.id.title_tv);
            tv_type = itemView.findViewById(R.id.quest_type);
            tv_type.setText(answerResult.getQuestion().getType());
            if(answerResult.getQuestion().getType().equals("单选")){
            }else {
                tv_type.setBackgroundDrawable( itemView.getResources().getDrawable(R.drawable.multiple));
                tv_type.setTextColor(Color.rgb(239, 99, 93));
            }

            if(answerResult.getChecked()==1){
                iv_checked.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_checked));
            }else {
                iv_checked.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_fail));
            }


        }

    }
}
