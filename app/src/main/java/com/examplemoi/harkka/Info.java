package com.examplemoi.harkka;

import android.widget.ImageView;

import java.io.Serializable;

public class Info implements Serializable {
    String name;
    ImageView weatherIcon;
    String temperature;
    String wind;
    String population;
    String licence;

    public Info(String NameP){
        name = NameP;
        temperature = "200°C";
        wind = "23 m/s";
        population = "81 778";
        licence = "30 000";
    }

    public String getName() {
        return name;
    }

    public ImageView getWeatherIcon() {
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
