package com.example.sportscenterreservationsystem;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//The purpose of this class is to provide methods for reading files
public class FileReaderObject {
    Singleton singleton = Singleton.getInstance();
    Context context;

    //Reads users from file to ArrayList if file contains them. Returns found users in ArrayList or empty ArrayList if users not found
    public ArrayList<User> readUsersFile(String fileName) {
        ArrayList<User> users = new ArrayList<>();
        try {
            context  = singleton.getMainContext();
            InputStream inputStream = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            s = br.readLine();
            ArrayList<Integer> reservations = new ArrayList<>();
            String[] rReservations;
            while ((s != null) && (!s.isEmpty())) {
                s = s.replace("\n", "");
                String[] uStr = s.split(";");
                //Adds users reservations to user if it has them
                if (uStr.length == 8) {
                    reservations.clear();
                    rReservations = uStr[7].split(",");
                    for (int i = 0; i < rReservations.length - 1; i++) {
                        reservations.add(Integer.parseInt(Array.get(rReservations, i).toString()));
                    }
                }
                users.add(new User(uStr[0], uStr[1], uStr[2], uStr[3], uStr[4], uStr[5], Integer.parseInt(uStr[6]), reservations));
                s = br.readLine();
            }
            br.close();
            inputStream.close();
            return users;
        } catch (IOException e) {
            Log.e("IOExeption", "Bad users filename");
        }
        return users;
    }

    //Reads rooms from file to ArrayList if file contains them. Returns found rooms in ArrayList or empty ArrayList if rooms not found
    //Program doesn't write rooms into file itself. I can however be provided with predetermined rooms from file.
    public ArrayList<Room> readRoomsFile(String fileName) {
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            context  = singleton.getMainContext();
            InputStream inputStream = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            s = br.readLine();
            while ((s != null) && (!s.isEmpty())) {
                s = s.replace("\n", "");
                String[] rStr = s.split(";");
                ArrayList<Integer> reservations = new ArrayList<>();
                String[] rReservations = rStr[4].split(",");
                for (int i = 0; i < rReservations.length ; i++) {
                    reservations.add(Integer.parseInt(rReservations[i]));
                }
                rooms.add(new Room(rStr[0], rStr[1], Integer.parseInt(rStr[2]), reservations));
                s = br.readLine();
            }
            br.close();
            inputStream.close();
            return rooms;
        } catch (IOException e) {
            Log.e("IOExeption", "Bad rooms filename");
        }
        return rooms;
    }

    //Reads reservations from file to ArrayList if file contains them. Returns found reservations in ArrayList or empty ArrayList if reservations not found
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Reservation> readReservationsFile(String fileName) {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        try {
            context  = singleton.getMainContext();
            InputStream inputStream = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            s = br.readLine();
            while ((s != null) && (!s.isEmpty())) {
                s = s.replace("\n", "");
                String[] rStr = s.split(";", -1);
                reservations.add(new Reservation(LocalDateTime.parse(rStr[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(rStr[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME), Integer.parseInt(rStr[2]), Integer.parseInt(rStr[3]), rStr[4], rStr[5], Integer.parseInt(rStr[6])));
                s = br.readLine();
            }
            br.close();
            inputStream.close();
            return reservations;
        } catch (IOException e) {
            Log.e("IOExeption", "Bad reservations filename");
        }
        return reservations;
    }

    //Reads sports from file to ArrayList if file contains them. Returns found sports in ArrayList or empty ArrayList if sports not found
    public ArrayList<String> readSportsFile(String fileName) {
        ArrayList<String> sports = new ArrayList<>();
        try {
            context  = singleton.getMainContext();
            InputStream inputStream = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            s = br.readLine();
            while ((s != null) && (!s.isEmpty())) {
                s = s.replace("\n", "");
                sports.add(s);
                s = br.readLine();
            }
            br.close();
            inputStream.close();
            return sports;
        } catch (IOException e) {
            Log.e("IOExeption", "Bad reservations filename");
        }
        return sports;
    }
}
