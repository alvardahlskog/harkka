package com.examplemoi.harkka;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView textSearch;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        textSearch = itemView.findViewById(R.id.editMunicipalityName);
    }
}