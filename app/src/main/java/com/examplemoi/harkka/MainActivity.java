package com.examplemoi.harkka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    private SearchHistory searches = SearchHistory.getInstance();
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
    // Getting data and building the Info object
    public void buildInfo(View v, WeatherCallback callback) {
        String municipalityUnedited = editName.getText().toString();
        String municipality = municipalityUnedited.substring(0, 1).toUpperCase() + municipalityUnedited.substring(1);
        // Population data
        //https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px

        CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://pxdata.stat.fi/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px");
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(url);

            } catch (IOException e) {
                Log.e("Error", "Failed to fetch data", e);
                return null;
            }

        }).thenAcceptAsync(areas -> {
            if (areas != null) {
                Log.d("areas", areas.toPrettyString());

                ArrayList<String> keys = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                for (JsonNode node : areas.get("variables").get(1).get("values")){
                    values.add(node.asText());
                }
                for (JsonNode node : areas.get("variables").get(1).get("valueTexts")){
                    keys.add(node.asText());
                }
                HashMap<String, String> municipalityCodes = new HashMap<>();
                for(int i = 0; i<keys.size(); i++){
                    municipalityCodes.put(keys.get(i), values.get(i) );
                }


                String code = municipalityCodes.get(municipality);
                Log.d("Code",code);


            } else {
                Log.e("Error", "Failed to fetch data");
            }
        });


        // Single municipality population
        //https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px


        // Getting weather information for Info object


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
                response -> {
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
                        String wind = "Tuuli: "+String.valueOf(roundedWind)+" m/s";
                        Info info = new Info(name,temperature,wind,weather);
                        DataBuilder.getInstance().addMunicipality(info);

                        callback.onWeatherInfoAvailable();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                volleyError ->
                        Toast.makeText(MainActivity.this, "Virhe", Toast.LENGTH_SHORT).show());


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

        editName.setText("");
    }
}