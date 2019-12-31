package com.example.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Grade extends Fragment {
    View view;

    TextView tv1;

    String param1 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.grade, container, false);

        param1 = getArguments().getString("param1");

        tv1 = (TextView) view.findViewById(R.id.gradetextView);
        tv1.setText(param1);

        return view;
    }
}
