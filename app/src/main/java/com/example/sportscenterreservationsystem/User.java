package com.example.sportscenterreservationsystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User {
    String username;
    String password;
    String fName;
    String lName;
    String email;
    String phone;
    int id;
    public ArrayList<Integer> reservations;

    public User(String givenUsername, String givenPassword, String givenfName, String givenlName, String givenEmail, String givenPhone, int givenId, ArrayList<Integer> givenReservations) {
        username = givenUsername;
        password = givenPassword;
        fName = givenfName;
        lName = givenlName;
        email = givenEmail;
        phone = givenPhone;
        id = givenId;
        reservations = givenReservations;
    }


    /*public class Admin extends User {

    }*/
}
