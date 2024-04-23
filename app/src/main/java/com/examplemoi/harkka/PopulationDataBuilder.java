package com.examplemoi.harkka;

import java.util.ArrayList;

public class PopulationDataBuilder {
    private ArrayList<Info> muncipalityList = new ArrayList<>();
    private static PopulationDataBuilder populationDataBuilder = null;
    public static PopulationDataBuilder getInstance(){
        if(populationDataBuilder == null) {
            populationDataBuilder = new PopulationDataBuilder();
        }
        return populationDataBuilder;
    }
    public ArrayList<Info> getMunicipalities() {
        return muncipalityList;
    }
    public void addMunicipality(Info info) {
        muncipalityList.add(info);
    }
}
