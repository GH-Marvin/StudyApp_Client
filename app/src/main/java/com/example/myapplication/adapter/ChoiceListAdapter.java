package com.example.myapplication.adapter;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.Choice;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.util.XToastUtils;
import com.xuexiang.xui.widget.button.SmoothCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChoiceListAdapter extends RecyclerView.Adapter<ChoiceListAdapter.InnerHolder> {

    private List<Choice> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private Map<Integer, Boolean> map = new HashMap<>();
    private View itemView;
    private boolean onBind;
    private int checkedPosition = -1;
    private String type = "单选"; //题型
    private List<Choice> singleChoiceList = new ArrayList<>();
    private List<Choice> multipleChoiceList = new ArrayList<>();
    ViewGroup mParent;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //得到当前选中的位置
    public int getCheckedPosition() {
        return checkedPosition;
    }
    @NonNull
    @Override
    public ChoiceListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        Choice thisChoice = mData.get(position);
        if(type.equals("单选")){
            holder.choice_btn.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    if (isChecked) {
                        map.clear();
                        map.put(position, true);
                        singleChoiceList.clear();
                        singleChoiceList.add(mData.get(position));
                        Log.e("list",String.valueOf(singleChoiceList.get(0).getOptId()));
                    } else {
                        singleChoiceList.remove(mData.get(position));
                        map.remove(position);
                    }
                    if (!onBind) {
                        notifyDataSetChanged();
                    }
                }
            });
            onBind = true;
            if (map != null && map.containsKey(position)) {
                holder.choice_btn.setChecked(true);
                holder.linearLayout.setBackgroundDrawable( itemView.getResources().getDrawable(R.drawable.choice_blue));

            } else {
                holder.choice_btn.setChecked(false);
                holder.linearLayout.setBackgroundDrawable( itemView.getResources().getDrawable(R.drawable.choice));
            }
            onBind = false;

        }else if(type.equals("多选")){
            holder.choice_btn.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    if(isChecked){
                        multipleChoiceList.add(mData.get(position));
                        Log.e("list",String.valueOf(multipleChoiceList.size()));
                        holder.linearLayout.setBackgroundDrawable( itemView.getResources().getDrawable(R.drawable.choice_blue));
                    }else {
                        multipleChoiceList.remove(mData.get(position));
                        Log.e("list",String.valueOf(multipleChoiceList.size()));
                        holder.linearLayout.setBackgroundDrawable( itemView.getResources().getDrawable(R.drawable.choice));
                    }
                }
            });
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                    holder.getData(mData.get(position));
                    Log.e("position",String.valueOf(getCheckedPosition()));
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

    public List<Choice> getSingle(){
        return singleChoiceList;
    }

    public List<Choice> getMultipleChoiceList(){
        return multipleChoiceList;
    }

    public void setData(Map<String,List<Choice>> map) {
        if (mData != null) {
            mData.clear();
            type = "";
            for(String s: map.keySet()){
                type = s;
            }
            mData.addAll(map.get(type));
        }
        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox choice_btn;
        LinearLayout linearLayout;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Choice choice) {
            //TODO
            linearLayout = itemView.findViewById(R.id.choice_linearView);
            choice_btn = itemView.findViewById(R.id.choice_btn);
            TextView choice_tv = itemView.findViewById(R.id.choice_tv);
            choice_tv.setText(choice.getName());
        }
        public void getData(Choice choice){
            Log.e("choice",String.valueOf(choice.getOptId()));
        }
    }
}
