package com.examplemoi.harkka.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examplemoi.harkka.Info;
import com.examplemoi.harkka.MainActivity;
import com.examplemoi.harkka.MunicipalityActivity;
import com.examplemoi.harkka.R;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompareFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, container, false);
        TextView txtName1 = view.findViewById(R.id.txtComName1);
        TextView txtWeather1 = view.findViewById(R.id.txtComWeather1);
        TextView txtPopulation1 = view.findViewById(R.id.txtCompPop1);
        TextView txtLicence1 = view.findViewById(R.id.txtCompLis1);

        if (getArguments() != null) {
            Serializable infoSerializable = getArguments().getSerializable("dataID");

// Check if the serializable object is not null and is of the expected type
            if (infoSerializable instanceof Info) {
                // Cast the serializable object to the appropriate type
                Info info = (Info) infoSerializable;

                // Set the name to the TextView
                txtName1.setText(info.getName());
                txtWeather1.setText(info.getTemperature());
                txtPopulation1.setText(info.getPopulation());
                txtLicence1.setText(info.getLicence());

            }


        }
        return view;
    }
}

