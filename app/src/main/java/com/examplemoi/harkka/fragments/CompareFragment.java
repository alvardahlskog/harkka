package com.examplemoi.harkka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.examplemoi.harkka.Info;
import com.examplemoi.harkka.R;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompareFragment#} factory method to
 * create an instance of this fragment.
 */
public class CompareFragment extends Fragment {
    private EditText etxtName;
    private Button btnSearchComp;

    private TextView txtComName2, txtComWeather2, txtPopulation2, txtLicence2;

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

        txtComName2 = view.findViewById(R.id.txtComName2);
        txtComWeather2 = view.findViewById(R.id.txtComWeather2);
        txtPopulation2 = view.findViewById(R.id.txtComPop2);
        txtLicence2 = view.findViewById(R.id.txtComLis2);

        etxtName = view.findViewById(R.id.etxtName);
        btnSearchComp = view.findViewById(R.id.btnSearchComp);

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
        btnSearchComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(etxtName.getText().toString());
            }
        });
        return view;
    }

    private void compare(String name) {
        txtComName2.setText(name);
        txtComWeather2.setText("hyvä sää :D");
        txtPopulation2.setText("paljon ihmisiä :P");
        txtLicence2.setText("sikana kuskeja :3");

        txtComName2.setVisibility(View.VISIBLE);
        txtComWeather2.setVisibility(View.VISIBLE);
        txtPopulation2.setVisibility(View.VISIBLE);
        txtLicence2.setVisibility(View.VISIBLE);
    }
}

