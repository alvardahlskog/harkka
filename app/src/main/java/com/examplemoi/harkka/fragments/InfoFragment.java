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
public class InfoFragment extends Fragment {
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtWeather = view.findViewById(R.id.txtWeather);
        TextView txtWind = view.findViewById(R.id.txtWind);
        TextView txtPopulation = view.findViewById(R.id.txtPopulation);
        TextView txtLicence = view.findViewById(R.id.txtLicense);

        if (getArguments() != null) {
            Serializable infoSerializable = getArguments().getSerializable("dataID");

// Check if the serializable object is not null and is of the expected type
            if (infoSerializable instanceof Info) {
                // Cast the serializable object to the appropriate type
                Info info = (Info) infoSerializable;

                // Set the name to the TextView
                txtName.setText(info.getName());
                txtWeather.setText(info.getTemperature());
                txtWind.setText(info.getWind());
                txtPopulation.setText(info.getPopulation());
                txtLicence.setText(info.getLicence());

            }


        }
        return view;
    }
}
