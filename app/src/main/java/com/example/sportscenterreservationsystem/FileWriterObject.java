package com.example.sportscenterreservationsystem;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

//The purpose of this class is to provide methods for writing files
public class FileWriterObject {

    Singleton singleton = Singleton.getInstance();
    Context context;

    //Receives sports in an ArrayList and writes them into file for later use
    public void writeSportsToFile(String filename, ArrayList<String> sports) {
        if (!sports.isEmpty()) {
            try {
                context  = singleton.getMainContext();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
                BufferedWriter br = new BufferedWriter(outputStreamWriter);
                for (int i = 0; i < sports.size(); i++) {
                    br.write(sports.get(i) + "\n");
                }
                br.close();
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("IOException", "Virhe syötteessä");
            }
        }
    }

    //Receives users in an ArrayList and writes them into file for later use
    public void writeUsersToFile(String filename, ArrayList<User> users) {
        if (!users.isEmpty()) {
            try {
                context  = singleton.getMainContext();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
                BufferedWriter br = new BufferedWriter(outputStreamWriter);
                for (int i = 0; i < users.size(); i++) {
                    br.write(users.get(i).username + ";" + users.get(i).password + ";" + users.get(i).fName + ";" + users.get(i).lName + ";" + users.get(i).email +
                            ";" + users.get(i).phone + ";" + users.get(i).id + ";");
                    if (!users.get(i).reservations.isEmpty()) {
                        for (int j = 0; j < users.get(i).reservations.size(); j++) {
                            br.write(users.get(i).reservations.get(j) + ",");
                        }
                    }
                    br.write("\n");
                }
                br.close();
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("IOException", "Virhe syötteessä");
            }
        }
    }

    //Receives reservations in an ArrayList and writes them into file for later use
    public void writeReservationsToFile(String filename, ArrayList<Reservation> reservations) {
        if (!reservations.isEmpty()) {
            try {
                context  = singleton.getMainContext();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
                BufferedWriter br = new BufferedWriter(outputStreamWriter);
                for (int i = 0; i < reservations.size(); i++) {
                    br.write(reservations.get(i).begins.toString() + ";" + reservations.get(i).ends.toString() + ";" + reservations.get(i).userId + ";" + reservations.get(i).roomId + ";" + reservations.get(i).reason +
                            ";" + reservations.get(i).sport + ";" + reservations.get(i).id + ";\n");
                }
                br.close();
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("IOException", "Virhe syötteessä");
            }
        }
    }
}
