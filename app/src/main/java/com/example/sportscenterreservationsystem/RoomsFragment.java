package com.example.sportscenterreservationsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomsFragment extends Fragment {


    View view;
    Spinner roomsSpinner;
    Spinner sportsSpinner;
    Context context;
    TextView roomInfo;
    TextView sportInfo;
    ArrayList<String> roomNamesArrayList = new ArrayList<>();
    ArrayList<String> sportsArrayList = new ArrayList<>();
    TextView outputText;
    Button addSportButton;
    EditText newSportText;
    int selectedRoom = 0;
    int selectedSport = 0;
    Singleton singleton;
    FileWriterObject fileWriterObject = new FileWriterObject();
    ArrayAdapter<String> sportsArrayAdapter;

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

    @Override
    public void onAttach(Context givenContext) {
        super.onAttach(givenContext);
        context = givenContext;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rooms_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        roomsSpinner = view.findViewById(R.id.roomsspinner);
        sportsSpinner = view.findViewById(R.id.sportsspinner);
        addSportButton = view.findViewById(R.id.addsportbutton);
        outputText = view.findViewById(R.id.outputTextView);
        newSportText = view.findViewById(R.id.newSportEditText);
        roomInfo = view.findViewById(R.id.roomInfoTextView);
        sportInfo = view.findViewById(R.id.sportInfoTextView);
        singleton = Singleton.getInstance();
        readRoomNamesToList();
        listSports();
        setSportsSpinner();
        ArrayAdapter<String> roomNamesArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, roomNamesArrayList);
        roomNamesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomsSpinner.setAdapter(roomNamesArrayAdapter);
        roomsSpinner();
        addSportButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSport();
            }
        });
    }

    //Gets ArrayList of sports from singleton for use in spinner
    void listSports() {
        sportsArrayList = singleton.getSports();
        sportsArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sportsArrayList);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(sportsArrayAdapter);
    }

    //adds given sport to ArrayList of sports in singleton if the sport doesn't already exist
    void addSport() {
        if (!newSportText.getText().toString().isEmpty()) {
            ArrayList<String> sports = singleton.getSports();
            if (!sports.isEmpty()) {
                int sportExists = 0;
                for (int i = 0; i < sports.size(); i++) {
                    if (sports.get(i).equals(newSportText.getText().toString())) {
                        sportExists = 1;
                    }
                }
                if (sportExists == 1) {
                    outputText.setText("Sport is already on the list");
                } else {
                    sports.add(sports.size(), newSportText.getText().toString());
                    outputText.setText("Sport added to list");
                    singleton.setSports(sports);
                    fileWriterObject.writeSportsToFile("sports.csv", singleton.getSports());
                    listSports();
                }
            } else {
                sports.add(newSportText.getText().toString());
                outputText.setText("Sport added to list");
                singleton.setSports(sports);
                fileWriterObject.writeSportsToFile("sports.csv", singleton.getSports());
                listSports();
            }
        } else {
            outputText.setText("Type in the sport you want to add before pressing add button");
        }
    }

    //Gets rooms names to ArrayList from singleton for use in spinner
    void readRoomNamesToList() {
        ArrayList<Room> rooms = Singleton.getInstance().getRooms();
        if ((!rooms.isEmpty()) && (roomNamesArrayList.isEmpty())) {
            for (int i = 0; i < rooms.size(); i++) {
                roomNamesArrayList.add(rooms.get(i).name);
            }
        }
    }

    void roomsSpinner() {
        roomsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoom = i;
                roomInfo.setText(singleton.getRooms().get(selectedRoom).description);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedRoom = 0;
            }
        });
    }

    void setSportsSpinner() {
        sportsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSport = i;
                showSportInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSport = 0;
            }
        });
    }

    //displays the amount of reservations made with the selected sport
    @SuppressLint("SetTextI18n")
    void showSportInfo() {
        int reservedAmount = 0;
        for (int i = 0; i < singleton.getReservations().size(); i++) {
            if (singleton.getReservations().get(i).sport.equals(singleton.getSports().get(selectedSport))) {
                reservedAmount = reservedAmount + 1;
            }
        }
        sportInfo.setText(reservedAmount + " reservations have been made for sport: " + singleton.getSports().get(selectedSport));
    }
}