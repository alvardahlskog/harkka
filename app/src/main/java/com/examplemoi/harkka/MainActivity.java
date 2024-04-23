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
    private SearchHistory searches = SearchHistory.getInstance();
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
        recyclerView.setAdapter(new SearchHistoryAdapter(getApplicationContext(), searches.getSearches()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildInfo(v, new WeatherCallback() {
                    @Override
                    public void onWeatherInfoAvailable() {
                        switchToMunicipality(v);
                    }
                });

            }
        });
    }
    public void buildInfo(View v, WeatherCallback callback) {

        String municipality = editName.getText().toString();
        String countryCode = "FIN";
        final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";



        //***********************  YOUR API key here  ********************* //
        //                                                                  //
              final String appid = "9f15c2db6185216b4715fc08c7632bef";
        //                                                                  //
        //******************************************************************//


        //  Display error and exit early if name is empty
        if (municipality.isEmpty()) {
            Toast.makeText(this, "Anna kunnan nimi!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tempWeatherUrl = weatherUrl + "?q=" + municipality + ",FIN&lang=fi&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempWeatherUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Parsing the JSON response
                            JSONArray weatherArray = jsonResponse.getJSONArray("weather");

                            JSONObject jsonObjectWeather = weatherArray.getJSONObject(0);
                            String weather = jsonObjectWeather.getString("main");
                            String descriptionSmall = jsonObjectWeather.getString("description");
                            String description = descriptionSmall.substring(0, 1).toUpperCase() + descriptionSmall.substring(1);

                            // Name from JSON response to capitalize displayed name
                            String name = jsonResponse.getString("name");

                            // Adds the successful search to search history with capitalized name
                            searches.addSearch(name);

                            // Main weather info
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double tempDouble = jsonObjectMain.getDouble("temp") - 273.15;
                            int roundedTemperature = (int) Math.round(tempDouble);
                            String temperature = String.valueOf(roundedTemperature)+"°C, " + description;

                            // Wind info
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            double windDouble = jsonObjectWind.getDouble("speed");
                            int roundedWind = (int) Math.round(windDouble);
                            String wind = String.valueOf(roundedWind)+" m/s";
                            Info info = new Info(name,temperature,wind,weather);
                            DataBuilder.getInstance().addMunicipality(info);

                            callback.onWeatherInfoAvailable();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
    }
    public interface WeatherCallback {
        void onWeatherInfoAvailable();
    }
    public void switchToMunicipality(View view) {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        searches = SearchHistory.getInstance();
        recyclerView.setAdapter(new SearchHistoryAdapter(getApplicationContext(), searches.getSearches()));
    }
}