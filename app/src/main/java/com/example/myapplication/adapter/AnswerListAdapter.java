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


public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.InnerHolder> {

    private List<Choice> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private Map<Integer, Boolean> map = new HashMap<>();
    private String answer_sheet="";
    private String answer_sheet_result="";
    private View itemView;
    private boolean onBind;
    private int checkedPosition = -1;
    private String type = "单选"; //题型
    private List<Choice> singleAnswerList = new ArrayList<>();
    private List<Choice> multipleAnswerList = new ArrayList<>();
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
    public AnswerListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        //找到View
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerListAdapter.InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        Choice choice = mData.get(position);
        String[] answer_sheet_result_split = answer_sheet_result.split(";");
        if(answer_sheet_result!=";"&&answer_sheet!=""){
            String[] answer_sheet_split = answer_sheet.split(";");
            for(int i =0;i<answer_sheet_result_split.length;i++){
                if(answer_sheet_result_split[i].split(":")[0].equals(String.valueOf(choice.getQuestId()))) {
                    for (int j = 0; j < answer_sheet_split.length; j++) {
                        if(answer_sheet_split[j].contains("-")){
                            String[] temp = answer_sheet_split[j].split("-");
                            String[] multiple_opts = temp[1].split(",");
                            for(int k=0;k<multiple_opts.length;k++){
                                if(multiple_opts[k].equals(String.valueOf(choice.getOptId()))){
                                    if (choice.getIsAnswer() == 0) {
                                        holder.choice_btn.setBackground(itemView.getResources().getDrawable(R.drawable.ic_fail));
                                        holder.linearLayout.setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.choice_red));
                                        break;
                                    } else {
                                        holder.choice_btn.setBackground(itemView.getResources().getDrawable(R.drawable.ic_checked));
                                        holder.linearLayout.setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.choice_green));
                                        break;
                                    }
                                }
                            }
                        }else {
                            String[] opts = answer_sheet_split[j].split(":");
                            if (opts[1].equals(String.valueOf(choice.getOptId()))) {
                                if (choice.getIsAnswer() == 0) {
                                    holder.choice_btn.setBackground(itemView.getResources().getDrawable(R.drawable.ic_fail));
                                    holder.linearLayout.setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.choice_red));
                                    break;
                                } else {
                                    holder.choice_btn.setBackground(itemView.getResources().getDrawable(R.drawable.ic_checked));
                                    holder.linearLayout.setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.choice_green));
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }else {
            holder.choice_btn.setBackground(itemView.getResources().getDrawable(R.drawable.ic_no_checked));
        }
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

    public void setData(Map<String,List<Choice>> map,String a1,String a2) {
        if (mData != null) {
            mData.clear();
            type = "";
            for(String s: map.keySet()){
                type = s;
            }
            mData.addAll(map.get(type));
        }
        if(answer_sheet!=null&&answer_sheet_result!=null){
            answer_sheet = "";
            answer_sheet_result = "";
            answer_sheet = a1;
            answer_sheet_result = a2;
        }

        //更新UI
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        ImageView choice_btn;
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
