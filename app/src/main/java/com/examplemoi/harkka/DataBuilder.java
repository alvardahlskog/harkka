package com.examplemoi.harkka;

import java.util.ArrayList;

public class DataBuilder {
    private ArrayList<Info> muncipalityList = new ArrayList<>();
    private static DataBuilder dataBuilder = null;
    public static DataBuilder getInstance(){
        if(dataBuilder == null) {
            dataBuilder = new DataBuilder();
        }
        return dataBuilder;
    }
    public ArrayList<Info> getMunicipalities() {
        return muncipalityList;
    }
    public void addMunicipality(Info info) {
        muncipalityList.add(info);
    }
}
