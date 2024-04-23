package com.examplemoi.harkka;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private SearchHistory searches = SearchHistory.getInstance();
    private EditText editName;
    private RecyclerView recyclerView;

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
                buildInfo();
            }
        });
    }

    // Getting data and building the Info object
    public void buildInfo() {
        String municipalityUnedited = editName.getText().toString();
        if (municipalityUnedited.isEmpty()) {
            Toast.makeText(this, "Anna kunnan nimi!", Toast.LENGTH_SHORT).show();
            return;
        }

        String municipality = municipalityUnedited.substring(0, 1).toUpperCase() + municipalityUnedited.substring(1);
        Info info = new Info();
        PopulationDataReciever pr = new PopulationDataReciever();
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<PopulationDataBuilder> populationData = pr.getData(MainActivity.this, municipality);

                if (populationData != null) {
                    for (PopulationDataBuilder data : populationData) {
                        if (data.getYear() == 2022) {
                            String population = String.valueOf(data.getPopulation());
                            info.population = "Väkiluku: " + population;
                            break; // Assuming only one population data for a specific year is needed
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Failed to retrieve population data", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                // Getting weather information for Info object

                final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";




                final String appid =

                ////////////   Insert your API code here//////////////////
                //                                                      //
                           "9f15c2db6185216b4715fc08c7632bef";
                //                                                      //
                //////////////////////////////////////////////////////////

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
                                info.temperature = roundedTemperature + "°C, " + description;

                                // Wind info
                                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                double windDouble = jsonObjectWind.getDouble("speed");
                                int roundedWind = (int) Math.round(windDouble);
                                info.wind = "Tuuli: " + roundedWind + " m/s";

                                // Pass the populated Info object to the next activity
                                DataBuilder.getInstance().addMunicipality(info);

                                // Switch to the next activity
                                switchToMunicipality();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Virhe", Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    public void switchToMunicipality() {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        searches = SearchHistory.getInstance();
        recyclerView.setAdapter(new SearchHistoryAdapter(getApplicationContext(), searches.getSearches()));
    }
}