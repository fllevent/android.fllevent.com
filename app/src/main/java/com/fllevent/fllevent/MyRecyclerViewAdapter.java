package com.fllevent.fllevent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<String> myDataset;
    private LayoutInflater mInflater;
    public MyRecyclerViewAdapter(Context context, ArrayList<String> myDataset) {
        this.mInflater = LayoutInflater.from(context);
        this.myDataset = myDataset;
    }
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerviewrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String data = myDataset.get(position);
        holder.textView.setText(data);
    }

    @Override
    public int getItemCount() {
        try {
            return myDataset.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textRow);
        }
    }
}
