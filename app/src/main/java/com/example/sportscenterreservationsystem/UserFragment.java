package com.example.sportscenterreservationsystem;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Build;
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

public class UserFragment extends Fragment {


    EditText fnameText;
    EditText lnameText;
    EditText emailText;
    EditText phoneText;
    EditText newReason;
    TextView outputText;
    Button updateInfoButton;
    Button showInfoButton;
    Button updateReservationButton;
    Button deleteReservationButton;
    Button showReservationsButton;
    Spinner reservationsSpinner;
    User activeUser;
    View view;
    Context context;
    ArrayList<String> reservationStrings = new ArrayList<>();
    Singleton singleton = Singleton.getInstance();
    FileWriterObject fileWriterObject = new FileWriterObject();
    int selectedReservation;


    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fnameText = view.findViewById(R.id.fnameEditText);
        lnameText = view.findViewById(R.id.lnameEditText);
        emailText = view.findViewById(R.id.emailEditText);
        phoneText = view.findViewById(R.id.phoneEditText);
        newReason = view.findViewById(R.id.newReasonEditText);
        outputText = (TextView) view.findViewById(R.id.outputTextView);
        updateInfoButton = view.findViewById(R.id.updateuserinfobutton);
        showInfoButton = view.findViewById(R.id.showprofileinfobutton);
        updateReservationButton = view.findViewById(R.id.updatereservationbutton);
        deleteReservationButton = view.findViewById(R.id.deletereservationbutton);
        showReservationsButton = view.findViewById(R.id.showreservationsbutton);
        reservationsSpinner = view.findViewById(R.id.reservationsspinner);
        context = singleton.getMainContext();
        showInfo();
        reservationsSpinner();
        showInfoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
            }
        });
        updateInfoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
        showReservationsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReservations();
            }
        });
        updateReservationButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReservation();
                showReservations();
            }
        });
        deleteReservationButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReservation();
                showReservations();
            }
        });
    }

    void reservationsSpinner() {
        reservationsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedReservation = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedReservation = 0;
            }
        });
    }

    public void showInfo() {
        activeUser = singleton.getActiveUser();
        fnameText.setText(activeUser.fName);
        lnameText.setText(activeUser.lName);
        emailText.setText(activeUser.email);
        phoneText.setText(activeUser.phone);
    }

    //Fills spinner with reservations the activeUser has made
    //Only reservations that are in the future are displayed
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showReservations() {
        reservationStrings = singleton.getReservationsStrings();
        ArrayAdapter<String> reservationStringsArrayAdapter =  new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, reservationStrings);
        reservationStringsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reservationsSpinner.setAdapter(reservationStringsArrayAdapter);
        reservationsSpinner();
    }

    //Updates the activeUsers info according to the editText field
    //The changes are made to the ArrayList<User> in the singleton and to the users file
    public void updateInfo() {
        if ((!fnameText.getText().toString().isEmpty()) && (!lnameText.getText().toString().isEmpty()) && (!emailText.getText().toString().isEmpty()) && (!phoneText.getText().toString().isEmpty())) {
            User activeUser = singleton.getActiveUser();
            activeUser.fName = fnameText.getText().toString();
            activeUser.lName = lnameText.getText().toString();
            activeUser.email = emailText.getText().toString();
            activeUser.phone = phoneText.getText().toString();
            singleton.setActiveUser(activeUser);
            ArrayList<User> users = new ArrayList<>();
            users = singleton.getUsers();
            users.add(activeUser.id, activeUser);
            users.remove(activeUser.id + 1);
            singleton.setUsers(users);
            fileWriterObject.writeUsersToFile("users.csv", singleton.getUsers());
        } else {
            outputText.setText("All fields must contain info");
        }
    }

    //Updates the selected reservation with the new reason given
    //Update is made to the ArrayList<Reservation> in singleton and to the reservations file
    public void updateReservation() {
        if (!newReason.getText().toString().isEmpty()) {
            if (!singleton.getReservations().isEmpty()) {
                Reservation reservation = singleton.getReservations().get(singleton.getActiveUser().reservations.get(selectedReservation));
                reservation.reason = newReason.getText().toString();
                ArrayList<Reservation> reservations = singleton.getReservations();
                reservations.add(reservation.id, reservation);
                reservations.remove(reservation.id + 1);
                singleton.setReservations(reservations);
                fileWriterObject.writeReservationsToFile("reservations.csv", singleton.getReservations());
            }
        } else {
            outputText.setText("Fill new reason field first");
        }
    }

    //deletes selected reservation from the singletons ArrayList and from the reservations file
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteReservation() {
        ArrayList<Reservation> reservations = singleton.getReservations();
        if (!reservations.isEmpty()) {
            Reservation reservation = reservations.get(singleton.getReservationsStringsIds().get(selectedReservation));
            reservations.remove(reservation);
            if (!reservations.isEmpty()) {
                int previousId = -1;
                for (int i = 0; i < reservations.size() ; i++) {
                    if (previousId + 1 < reservations.get(i).id) {
                        reservations.get(i).id = previousId + 1;
                    }
                    previousId = reservations.get(i).id;
                }
            }
            singleton.setReservations(reservations);
            fileWriterObject.writeReservationsToFile("reservations.csv", singleton.getReservations());
        }
        selectedReservation = 0;
        showReservations();
        reservationsSpinner();
    }
}