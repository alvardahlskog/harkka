package com.examplemoi.harkka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.examplemoi.harkka.fragments.InfoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> searches = SearchHistory.getInstance().getSearches();

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
        recyclerView.setAdapter(new SearchHistoryAdapter(getApplicationContext(), searches));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchHistory.getInstance().addSearch(editName.getText().toString());
                buildInfo(v);

            }
        });
    }
    public void buildInfo(View v) {

        String municipality = editName.getText().toString();
        String countryCode = "FIN";
        final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";
        final String appid = "9f15c2db6185216b4715fc08c7632bef";

        if (municipality.equals("")) {
            Toast.makeText(this, "Kirjoita kunnan nimi tekstikenttään", Toast.LENGTH_SHORT).show();
            return; // Exit early if municipality is empty
        }

        String tempWeatherUrl = weatherUrl + "?q=" + municipality + ",FIN&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempWeatherUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Info info = new Info(municipality);
                            JSONObject jsonResponse = new JSONObject(response);

                            // Parsing weather
                            JSONArray weatherArray = jsonResponse.getJSONArray("weather");

                            JSONObject jsonObjectWeather = weatherArray.getJSONObject(0);
                            String weather = jsonObjectWeather.getString("main");

                                Log.d("weather", weather);


                            // Parsing main weather info
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double tempDouble = jsonObjectMain.getDouble("temp") - 273.15;
                            String temperature = Double.toString(tempDouble);

                            // Parsing wind info
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            Info info = new Info(municipality,temperature,wind,weather);
                            DataBuilder.getInstance().addMunicipality(info);


                        } catch (JSONException e) {
                            e.printStackTrace(); // Log JSON parsing errors
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "Virhe", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        switchToMunicipality(v);
    }
    public void switchToMunicipality(View view) {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        startActivity(intent);
    }
}