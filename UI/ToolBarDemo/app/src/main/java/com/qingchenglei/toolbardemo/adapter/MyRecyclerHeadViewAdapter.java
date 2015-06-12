package com.qingchenglei.toolbardemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchenglei.toolbardemo.R;

/**
 * Created by Zech on 2015/5/7.
 * <p/>
 * recyclerview adapter
 */
public class MyRecyclerHeadViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private String[] titles;

    private View headView;
    public MyRecyclerHeadViewAdapter(Context context,View headView){
        this.context = context;
        titles = context.getResources().getStringArray(R.array.titles);
        this.headView = headView;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            return new HeadViewHolder(headView);
        } else {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).textView.setText(titles[position -1]);
        }
    }



    @Override
    public int getItemCount() {
        if (headView == null){
            return titles.length;
        }else {
            return titles.length + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_rv_item);
        }
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder{

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
