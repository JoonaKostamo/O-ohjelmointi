package com.example.sportscenterreservationsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context = null;
    LoginFragment loginFragment = new LoginFragment();
    RoomsFragment roomsFragment = new RoomsFragment();
    ReservationsFragment reservationsFragment = new ReservationsFragment();
    UserFragment userFragment = new UserFragment();
    TextView outputText;
    Singleton singleton = Singleton.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        singleton.setMainContext(context);
        outputText = (TextView) findViewById(R.id.outputTextView);
        readFilesToLists();
        //Login screen is displayed first. Other fragments and their views can be accessed only when valid login has been made
        displayLogin();
        ArrayList<User> users = new ArrayList<>();
        users = Singleton.getInstance().getUsers();
        singleton.setUsers(users);
    }

// Used to switch fragment that is visible to user
    void displayLogin () {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, loginFragment);
        fragmentTransaction.commit();
    }

    void displayRooms () {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, roomsFragment);
        fragmentTransaction.commit();
    }


    void displayReservations() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, reservationsFragment);
        fragmentTransaction.commit();
    }

    void displayUser() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, userFragment);
        fragmentTransaction.commit();
    }

// Activated by buttons. Checks if login is valid by checking if activeUser has been set in singleton. If valid switches
// to view of the fragment selected by button
    public void rooms(View V) {
        if (Singleton.getInstance().getActiveUser() != null) {
            outputText.setText("Active user: " + singleton.getActiveUser().username);
            displayRooms();
        } else {
            outputText.setText("Log in first!");
        }
    }

    public void reservations(View V) {
        if (Singleton.getInstance().getActiveUser() != null) {
            outputText.setText("Active user: " + singleton.getActiveUser().username);
            displayReservations();
        } else {
            outputText.setText("Log in first!");
        }
    }

    public void user(View V) {
        if (Singleton.getInstance().getActiveUser() != null) {
            outputText.setText("Active user: " + singleton.getActiveUser().username);
            displayUser();
        } else {
            outputText.setText("Log in first!");
        }
    }

//Gets called on startup. Gets previous state from files. Information found in files gets stored in the singleton to enable access from all fragments.
    @RequiresApi(api = Build.VERSION_CODES.O)
    void readFilesToLists() {
        FileReaderObject fileReaderObject = new FileReaderObject();
        singleton.setUsers(fileReaderObject.readUsersFile("users.csv"));
        singleton.setRooms(fileReaderObject.readRoomsFile("rooms.csv"));
        singleton.setReservations(fileReaderObject.readReservationsFile("reservations.csv"));
        singleton.setSports(fileReaderObject.readSportsFile("sports.csv"));
        if ((singleton.getSports().isEmpty()) || (!singleton.getSports().get(0).equals("No sport selected"))) {
            ArrayList<String> sports = singleton.getSports();
            sports.add(0, "No sport selected");
        }
        //If no rooms file is provided, these default rooms exist in the Sports hall. For different rooms alter the code here, or provide the program
        // a "rooms.csv" file in the application folder that contains the rooms you would like to have used.
        if (singleton.getRooms().isEmpty()) {
            ArrayList<Room> rooms = singleton.getRooms();
            rooms.add(new Room("Gym", "Gym room for lifting weights", 0, new ArrayList<Integer>()));
            rooms.add(new Room("Multipurpose room", "Big room with basketball hoops and storage with sports equipment", 1, new ArrayList<Integer>()));
            rooms.add(new Room("Gym 2", "Gym room for lifting weights", 3, new ArrayList<Integer>()));
            rooms.add(new Room("Gym 3", "Gym room for lifting weights", 4, new ArrayList<Integer>()));
            singleton.setRooms(rooms);
        }
    }


}