package com.examplemoi.harkka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchViewHolder>{
    private Context context;
    private ArrayList<String> searches;

    public SearchHistoryAdapter(Context context, ArrayList<String> searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_search,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.textSearch.setText(searches.get(position));
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }
}
