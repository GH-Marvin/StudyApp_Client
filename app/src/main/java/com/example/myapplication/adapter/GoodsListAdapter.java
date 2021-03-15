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
import com.example.myapplication.entity.Goods;
import com.example.myapplication.util.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.InnerHolder> {

    private List<Goods> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private View itemView;
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public GoodsListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        SharedPreferences sharedPre = mParent.getContext().getSharedPreferences("config",MODE_PRIVATE);
        String user_id = sharedPre.getString("user_id","");
        holder.buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id!=null&&user_id!=""){
                    Intent intent = new Intent(mParent.getContext(), GoodsDetailActivity.class);
                    intent.putExtra("goods_id",String.valueOf(mData.get(position).getGoodsId()));
                    mParent.getContext().startActivity(intent);
                }else {
                    new MaterialDialog.Builder(itemView.getContext())
                            .content("你还未登录，无法进行购买操作")
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
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

    public void setData(List<Goods> goodsList) {
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
        TextView goodsName_tv,goodsDesc_tv,goodsTag_tv1,goodsTag_tv2,goodsPrice_tv;
        Button buy_btn;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Goods goods) {
            //TODO
            goodIcon_iv = itemView.findViewById(R.id.goodIcon_iv);
            goodsName_tv = itemView.findViewById(R.id.goodsName_tv);
            goodsDesc_tv = itemView.findViewById(R.id.goodsDesc_tv);
            goodsTag_tv1 = itemView.findViewById(R.id.goodsTag_tv1);
            goodsTag_tv2 = itemView.findViewById(R.id.goodsTag_tv2);
            goodsPrice_tv = itemView.findViewById(R.id.goodsPrice_tv);
            buy_btn = itemView.findViewById(R.id.buy_btn);
            Glide.with(mParent.getContext()).load(goods.getGoodsIcon()).into(goodIcon_iv);
            goodsName_tv.setText(goods.getGoodsName());
            goodsDesc_tv.setText(goods.getGoodsDescription());
            String[] tags = goods.getGoodsTag().split("&");
            goodsTag_tv1.setText(tags[0]);
            goodsTag_tv2.setText(tags[1]);
            goodsPrice_tv.setText(String.valueOf(goods.getGoodsPrice())+".");

        }

    }
}
