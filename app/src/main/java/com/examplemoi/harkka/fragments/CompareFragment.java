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
        TextView txtPopulation1 = view.findViewById(R.id.txtCompPop1);
        TextView txtWikiLink = view.findViewById(R.id.txtWikiLink);




        if (getArguments() != null) {
            Serializable infoSerializable = getArguments().getSerializable("dataID");

// Check if the serializable object is not null and is of the expected type
            if (infoSerializable instanceof Info) {
                // Cast the serializable object to the appropriate type
                Info info = (Info) infoSerializable;

                // Set the name to the TextView
                txtName1.setText(info.getName());
                txtPopulation1.setText(info.getPopulation());
                txtWikiLink.setText("fi.wikipedia.org/wiki/"+info.getName());


            }


        }

        return view;
    }


}

