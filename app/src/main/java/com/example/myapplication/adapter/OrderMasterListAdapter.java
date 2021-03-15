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
import com.example.myapplication.entity.OrderMaster;
import com.example.myapplication.util.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class OrderMasterListAdapter extends RecyclerView.Adapter<OrderMasterListAdapter.InnerHolder> {

    private List<OrderMaster> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public OrderMasterListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderMasterListAdapter.InnerHolder holder, final int position) {
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

    public void setData(List<OrderMaster> goodsList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(goodsList);
        }
        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        ImageView goodIcon_iv;
        TextView goodsName_tv,goodsPrice_tv,pay_price,total_price,time_tv;
        Button buy_btn;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(OrderMaster orderMaster) {
            //TODO
            goodIcon_iv = itemView.findViewById(R.id.goodIcon_iv);
            goodsName_tv = itemView.findViewById(R.id.goodsName_tv);
            goodsPrice_tv = itemView.findViewById(R.id.goodsPrice_tv);
            pay_price = itemView.findViewById(R.id.pay_price);
            total_price = itemView.findViewById(R.id.total_price);
            time_tv = itemView.findViewById(R.id.time_tv);
            Glide.with(mParent.getContext()).load(orderMaster.getGoodsIcon()).into(goodIcon_iv);
            goodsName_tv.setText(orderMaster.getGoodsName());
            goodsPrice_tv.setText(String.valueOf(orderMaster.getGoodsPrice())+".");
            pay_price.setText(String.valueOf(orderMaster.getGoodsPrice()));
            total_price.setText(String.valueOf(orderMaster.getGoodsPrice()));
            time_tv.setText("订单成交时间："+orderMaster.getCreateTime());
        }

    }
}
