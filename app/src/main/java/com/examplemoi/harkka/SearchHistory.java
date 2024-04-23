package com.examplemoi.harkka;

import java.util.ArrayList;

public class SearchHistory {
    private static SearchHistory instance;
    private ArrayList<String> searches = new ArrayList<>();

    public SearchHistory() {};

    public static SearchHistory getInstance() {
        if (instance == null) {
            instance = new SearchHistory();
        }
        return instance;
    }

    public ArrayList<String> getSearches() {
        return searches;
    }

    public void addSearch(String name) {
        searches.add(0,name);
    }
}
