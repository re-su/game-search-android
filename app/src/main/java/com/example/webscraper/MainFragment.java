package com.example.webscraper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private int selectedOption;

    public MainFragment() { }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.button_frag1);

        // Ustawianie Listy wyboru
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Ustawianie switchy sklepow
        Switch steamSwitch = (Switch) view.findViewById(R.id.steamSwitch);
        Switch gogSwitch = (Switch) view.findViewById(R.id.gogSwitch);
        Switch wsSwitch = (Switch) view.findViewById(R.id.wsSwitch);

        // Zrob to po nacisnieciu przycisku
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search = (EditText) view.findViewById(R.id.searchTxt);
                String toFind = search.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("find", toFind);
                bundle.putInt("option", selectedOption);
                bundle.putBoolean("steam", steamSwitch.isChecked());
                bundle.putBoolean("gog", gogSwitch.isChecked());
                bundle.putBoolean("ws", wsSwitch.isChecked());

                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_searchResultFragment2, bundle);

            }
        });

    }

    // Do obsługi listy (SPINNER)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedOption = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

}
