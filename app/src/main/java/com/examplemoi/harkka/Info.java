package com.examplemoi.harkka;

import android.widget.ImageView;

import java.io.Serializable;

public class Info implements Serializable {
    String name;
    int weatherIcon;
    String temperature, wind, population, licence, weather;

    public Info(String NameP){
        name = NameP;
        temperature = "200°C";
        wind = "23 m/s";
        population = "81 778";
        licence = "30 000";
        weather = "Clear";

        switch (weather) {
            case "Thunderstorm":
                weatherIcon = R.drawable.thunder;
                break;
            case "Drizzle":
                weatherIcon = R.drawable.rain;
                break;
            case "Rain":
                weatherIcon = R.drawable.rain;
                break;
            case "Snow":
                weatherIcon = R.drawable.snow;
                break;
            case "Clear":
                weatherIcon = R.drawable.clear;
                break;
            default:
                weatherIcon = R.drawable.fog;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWind() {
        return wind;
    }

    public String getPopulation() {
        return population;
    }

    public String getLicence() {
        return licence;
    }
}
