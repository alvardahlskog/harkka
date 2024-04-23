package com.examplemoi.harkka;

import static java.lang.String.valueOf;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.examplemoi.harkka.fragments.InfoFragment;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

                buildInfo(new WeatherCallback() {
                    @Override
                    public void onWeatherInfoAvailable() {
                        switchToMunicipality(v);
                    }
                });

            }
        });
    }
    // Getting data and building the Info object
    public void buildInfo(WeatherCallback callback) {
        Context context = this;
        String municipalityUnedited = editName.getText().toString();
        if (municipalityUnedited.isEmpty()) {
            Toast.makeText(this, "Anna kunnan nimi!", Toast.LENGTH_SHORT).show();
            return;
        }
        String municipality = municipalityUnedited.substring(0, 1).toUpperCase() + municipalityUnedited.substring(1);
        Info info = new Info();
        //final String[] population = {"Väkilukua ei löydetty"};
        PopulationDataReciever pr = new PopulationDataReciever();



        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<PopulationDataBuilder> populationData = pr.getData(context, municipality);

                if (populationData == null) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s = "";
                        for (PopulationDataBuilder data : populationData) {
                            s = s + data.getYear() + ": " + data.getPopulation() + "\n";
                            Log.d("pop", s);
                        }
                    }
                });
            }
        });
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
                        info.weather = jsonObjectWeather.getString("main");
                        String descriptionSmall = jsonObjectWeather.getString("description");
                        String description = descriptionSmall.substring(0, 1).toUpperCase() + descriptionSmall.substring(1);

                        // Name from JSON response to capitalize displayed name
                        String name = jsonResponse.getString("name");
                        info.name = name;

                            // Adds the successful search to search history with capitalized name
                            searches.addSearch(name);

                        // Main weather info
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double tempDouble = jsonObjectMain.getDouble("temp") - 273.15;
                        int roundedTemperature = (int) Math.round(tempDouble);
                        info.temperature= roundedTemperature +"°C, " + description;

                        // Wind info
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        double windDouble = jsonObjectWind.getDouble("speed");
                        int roundedWind = (int) Math.round(windDouble);
                        info.wind = "Tuuli: "+ roundedWind +" m/s";
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
    }
}