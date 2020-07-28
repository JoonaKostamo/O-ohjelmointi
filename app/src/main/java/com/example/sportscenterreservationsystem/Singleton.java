package com.example.sportscenterreservationsystem;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

//This singleton type class stores information for all the fragments to use and modify
public class Singleton {
    private static Singleton single = new Singleton();
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private User activeUser = null;
    private Context mainContext = null;
    private ArrayList<Integer> reservationsStringsIds = new ArrayList<>();
    private ArrayList<String> sports = new ArrayList<>();

    public static Singleton getInstance() {return single;}

    public void setRooms(ArrayList<Room> givenRooms) {
        rooms = givenRooms;
    }
    public void setReservations(ArrayList<Reservation> givenReservations) {
        reservations = givenReservations;
    }
    public void setUsers(ArrayList<User> givenUsers) {
        users = givenUsers;
    }
    public void setActiveUser(User givenUser) {
        activeUser = givenUser;
    }
    public void setMainContext(Context context) {
        mainContext = context;
    }
    public void setSports(ArrayList<String> givenSports) {
        sports = givenSports;
    }

    public Context getMainContext() {
        return mainContext;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }
    public  ArrayList<User> getUsers() {
        return users;
    }
    public User getActiveUser() {
        return activeUser;
    }
    public ArrayList<Integer> getReservationsStringsIds() {
        return reservationsStringsIds;
    }
    public ArrayList<String> getSports() {
        return sports;
    }

    // Provides an ArrayList of strings containing important parts of information contained in reservations ArrayList for use in spinners
    // only includes reservations that are in the future, not the past
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> getReservationsStrings() {
        reservationsStringsIds.clear();
        ArrayList<String> reservationsStrings = new ArrayList<>();
        Reservation reservation;
        String day;
        String month;
        String year;
        String startHour;
        String startMinute;
        String endHour;
        String endMinute;
        LocalDateTime now = LocalDateTime.now();
        if ((reservations != null) && (reservations.size() > 0)) {
            for (int i = 0; i < reservations.size(); i++) {
                reservation = reservations.get(i);
                if ((reservation.userId == activeUser.id) && (reservation.begins.isAfter(now))) {
                    day = (reservation.begins.getDayOfMonth() + "");
                    month = (reservation.begins.getMonthValue() + "");
                    year = (reservation.begins.getYear() + "");
                    startHour = (reservation.begins.getHour() + "");
                    startMinute = (reservation.begins.getMinute() + "");
                    endHour = (reservation.ends.getHour() + "");
                    endMinute = (reservation.ends.getMinute() + "");
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    if (startHour.length() == 1) {
                        startHour = "0" + startHour;
                    }
                    if (startMinute.length() == 1) {
                        startMinute = "0" + startMinute;
                    }
                    if (endHour.length() == 1) {
                        endHour = "0" + endHour;
                    }
                    if (endMinute.length() == 1) {
                        endMinute = "0" + endMinute;
                    }
                    reservationsStrings.add(rooms.get(reservation.roomId).name + " " + day + "." + month +
                            "." + year + " " + startHour + ":" + startMinute + " - " +
                            endHour + ":" + endMinute + " " + reservation.reason);
                    reservationsStringsIds.add(reservation.id);
                }
            }
        }
        return reservationsStrings;
    }

}
