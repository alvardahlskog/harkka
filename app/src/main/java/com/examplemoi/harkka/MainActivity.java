package com.examplemoi.harkka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examplemoi.harkka.fragments.InfoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Search> searches = new ArrayList<>();

    public EditText getEditName() {
        return editName;
    }

    private EditText editName;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editMunicipalityName);
        Button button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.rvSearches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchListAdapter(getApplicationContext(), searches));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info info = new Info(editName.getText().toString());
                searchMunicipality(editName.toString());
                DataBuilder.getInstance().addGrocery(info);
                switchToMunicipality(v);


            }

        });
    }

    public void buildInfo(){

    }

    public void searchMunicipality(String municipality){
        Search search = new Search();
        search.name=editName.getText().toString();
        if(searches == null) {
            searches = new ArrayList<Search>();
        }
        searches.add(search);
        System.out.println(editName.toString());


    }

    public void switchToMunicipality(View view) {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        startActivity(intent);
    }



}