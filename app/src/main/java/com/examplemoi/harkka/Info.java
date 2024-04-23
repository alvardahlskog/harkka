package com.examplemoi.harkka;

import java.io.Serializable;

public class Info implements Serializable {
    String name;

    String temperature, wind, population, licence, weather;

    public Info(){
        name = "tyhjä";
        temperature = "tyhjä";
        wind = "tyhjä";
        population = "Väkiluku: 100 000";
        licence = "Ajokortillisia: 30 000";
        weather = "Clear";
    }

    public String getName() {
        return name;
    }

    public String getWeather() {
        return weather;
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
