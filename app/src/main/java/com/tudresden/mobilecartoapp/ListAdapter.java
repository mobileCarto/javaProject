package com.tudresden.mobilecartoapp;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Locations> values;

    public ListAdapter(List<Locations> myDataset) {
        values = myDataset;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.firstLine.setText(Integer.toString(values.get(position).getID()));
        holder.secondLine.setText(values.get(position).getTime());
        holder.thirdLine.setText(values.get(position).getLatitude());
        holder.forthLine.setText(values.get(position).getLongitude());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstLine;
        public TextView secondLine;
        public TextView thirdLine;
        public TextView forthLine;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            firstLine = v.findViewById(R.id.firstLine);
            secondLine = v.findViewById(R.id.secondLine);
            thirdLine = v.findViewById(R.id.thirdLine);
            forthLine = v.findViewById(R.id.forthLine);
        }
    }
}
