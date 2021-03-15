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
import com.example.myapplication.entity.Comment;
import com.example.myapplication.util.XToastUtils;

import java.util.ArrayList;
import java.util.List;




public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.InnerHolder> {

    private List<Comment> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public CommentListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                    XToastUtils.toast("您点击了评论"+String.valueOf(position+1)+"!");
                    holder.getData(mData.get(position));
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

    public void setData(List<Comment> commentList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(commentList);
        }
        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Comment comment) {
            //TODO
            ImageView iv_head = itemView.findViewById(R.id.item_head_iv);
            TextView tv_name = itemView.findViewById(R.id.item_username_tv);
            TextView tv_time = itemView.findViewById(R.id.item_time_tv);
            TextView tv_comment = itemView.findViewById(R.id.item_content_tv);
            TextView tv_goodNum = itemView.findViewById(R.id.item_goodNum_tv);
            Glide.with(mParent.getContext()).load(comment.getHead()).into(iv_head);
            tv_name.setText(comment.getName());
            tv_time.setText(comment.getTime());
            tv_comment.setText(comment.getContent());
            tv_goodNum.setText(String.valueOf(comment.getGoodNum()));


        }
        public void getData(Comment comment){
            Log.e("comment",comment.getContent());
        }

    }
}
