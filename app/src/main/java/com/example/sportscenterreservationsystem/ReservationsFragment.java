package com.example.sportscenterreservationsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservationsFragment extends Fragment {

    //private ReservationsViewModel mViewModel;
    View view;
    Spinner sportsSpinner;
    FileWriterObject fileWriterObject = new FileWriterObject();
    Spinner roomsSpinner;
    Context context = null;
    EditText ddText;
    EditText mmText;
    EditText yyyyText;
    EditText timeText;
    EditText endTimeText;
    EditText reasonText;
    Button addReservationButton;
    Button reservationsButton;
    TextView outputText;
    String day;
    String month;
    String year;
    String startHour;
    String startMinute;
    String endHour;
    String endMinute;
    int selectedSport = 0;
    Singleton singleton = Singleton.getInstance();
    ArrayList<Reservation> reservations = new ArrayList<>();
    LinearLayout reservationsLayout;
    ScrollView reservationsScrollView;
    ArrayList<String> roomNamesArrayList = new ArrayList<>();
    int selectedRoom = 0;
    int overlap = 0;
    Reservation selectedReservation;
    ArrayList<String> sportsArrayList = new ArrayList<>();
    ArrayAdapter<String> sportsArrayAdapter;
    String sport = "";

    public static ReservationsFragment newInstance() {
        return new ReservationsFragment();
    }

    @Override
    public void onAttach(Context givenContext) {
        super.onAttach(givenContext);
        context = givenContext;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservations_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(ReservationsViewModel.class);
        // TODO: Use the ViewModel
        roomsSpinner = view.findViewById(R.id.roomsspinner);
        ddText = view.findViewById(R.id.ddEditText);
        mmText = view.findViewById(R.id.mmEditText);
        yyyyText = view.findViewById(R.id.yyyyEditText);
        timeText = view.findViewById(R.id.timeEditText);
        endTimeText = view.findViewById(R.id.endTimeEditText);
        reasonText = view.findViewById(R.id.reasonEditText);
        reservationsLayout = view.findViewById(R.id.reservationsLinearLayout);
        reservationsScrollView = view.findViewById(R.id.reservationsscrollview);
        reservationsButton = view.findViewById(R.id.showreservationsbutton);
        addReservationButton = view.findViewById(R.id.makeReservationButton);
        outputText = (TextView) view.findViewById(R.id.outputTextView);
        sportsSpinner = view.findViewById(R.id.sportsspinner2);
        listSports();
        readRoomNamesToList();
        ArrayAdapter<String> roomNamesArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, roomNamesArrayList);
        roomNamesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomsSpinner.setAdapter(roomNamesArrayAdapter);
        roomsSpinner();
        setSportsSpinner();
        reservationsButton.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                showReservations();
            }
        });
        addReservationButton.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                addReservation();
            }
        });
    }

    void setSportsSpinner() {
        sportsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSport = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSport = 0;
            }
        });
    }

    //Gets sports from singleton and lists them in an ArrayList. Uses this list in a spinner.
    void listSports() {
        sportsArrayList = singleton.getSports();
        sportsArrayList.add(0, "No sport selected");
        sportsArrayAdapter =  new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sportsArrayList);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(sportsArrayAdapter);
        sportsArrayList.remove(0);
    }

    //Gets rooms names from singleton and lists them in an ArrayList. Uses this list in a spinner.
    void readRoomNamesToList() {
        ArrayList<Room> rooms = Singleton.getInstance().getRooms();
        if ((!rooms.isEmpty()) && (roomNamesArrayList.isEmpty())) {
            for (int i = 0; i < rooms.size(); i++) {
                roomNamesArrayList.add(rooms.get(i).name);
            }
            roomNamesArrayList.add("SELECT ALL ROOMS");
        }
    }

    void roomsSpinner() {
        roomsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoom = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedRoom = 0;
            }
        });
    }

    //Shows reservations that match with the filter information selected by the user
    //Can filter by date combined with room, sport or both
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showReservations() {
        try {
            reservationsLayout.removeAllViews();
            //gets reservations from singleton
            reservations = singleton.getReservations();
            if ((!reservations.isEmpty()) && (!ddText.getText().toString().isEmpty()) && (!mmText.getText().toString().isEmpty()) && (!yyyyText.getText().toString().isEmpty())) {
                //All reservations that match filter criteria are added to a separate list
                ArrayList<Reservation> selectedReservations = new ArrayList<>();
                for (int i = 0; i < reservations.size(); i++) {
                    if ((Integer.parseInt(String.valueOf(reservations.get(i).begins.getDayOfMonth())) == Integer.parseInt(String.valueOf(ddText.getText()))) && (reservations.get(i).begins.getMonthValue() == Integer.parseInt(String.valueOf(mmText.getText())))
                            && (reservations.get(i).begins.getYear() == Integer.parseInt(String.valueOf(yyyyText.getText())))) {
                        if ((selectedRoom == roomNamesArrayList.size() - 1) || (selectedRoom == reservations.get(i).roomId)) {
                            if ((selectedSport == 0) || (singleton.getSports().get(selectedSport).equals(reservations.get(i).sport))) {
                                selectedReservations.add(reservations.get(i));
                            }
                        }
                    }
                }
                if (!selectedReservations.isEmpty()) {
                    if (selectedReservations.size() > 1) {
                        /* INEFFICIENT SORTING HAPPENS HERE would make better if had more time*/
                        //Sorts selected reservations for viewing in order later
                        for (int i = 1; i < selectedReservations.size(); i++) {
                            for (int j = 0; j < i; j++) {
                                if (selectedReservations.get(i).begins.isBefore(selectedReservations.get(j).begins)) {
                                    selectedReservation = selectedReservations.get(i);
                                    selectedReservations.remove(i);
                                    selectedReservations.add( j, selectedReservation);
                                }
                            }
                        }
                        String previousEndHour = "00";
                        String previousEndMinute = "00";
                        for (int i = 0; i < selectedReservations.size(); i++) {
                            //Makes sure strings containing date info are always formatted in the same way
                            day = (selectedReservations.get(i).begins.getDayOfMonth() + "");
                            month = (selectedReservations.get(i).begins.getMonthValue() + "");
                            year = (selectedReservations.get(i).begins.getYear() + "");
                            startHour = (selectedReservations.get(i).begins.getHour() + "");
                            startMinute = (selectedReservations.get(i).begins.getMinute() + "");
                            endHour = (selectedReservations.get(i).ends.getHour() + "");
                            endMinute = (selectedReservations.get(i).ends.getMinute() + "");
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
                            sport = "";
                            if (!selectedReservations.get(i).sport.isEmpty()) {
                                sport = "\n\tSport: " + selectedReservations.get(i).sport;
                            }
                            //Prints free times whenever there is no reservation and prints reservations where they belong
                            if ((Integer.parseInt(previousEndHour) < Integer.parseInt(startHour)) || ((Integer.parseInt(previousEndHour) == Integer.parseInt(startHour)) &&
                                    (Integer.parseInt(previousEndMinute) < Integer.parseInt(startMinute)))) {
                                TextView reservationView = new TextView(context);
                                reservationView.setText(roomNamesArrayList.get(selectedReservations.get(i).roomId) + " " + day + "." + month +
                                        "." + year + " " + previousEndHour + ":" + previousEndMinute + " - " +
                                        startHour + ":" + startMinute + " - FREE");
                                reservationsLayout.addView(reservationView);
                            }
                            TextView reservationView = new TextView(context);
                            reservationView.setText(roomNamesArrayList.get(selectedReservations.get(i).roomId) + " " + day + "." + month +
                                    "." + year + " " + startHour + ":" + startMinute + " - " +
                                    endHour + ":" + endMinute + " Reason: " + selectedReservations.get(i).reason + sport);
                            reservationsLayout.addView(reservationView);
                            previousEndHour = endHour;
                            previousEndMinute = endMinute;
                        }
                        if (selectedReservations.get(selectedReservations.size() - 1).ends.getHour() < 24) {
                            TextView reservationView = new TextView(context);
                            reservationView.setText(roomNamesArrayList.get(selectedReservations.get(selectedReservations.size() - 1).roomId) + " " + day + "." + month +
                                    "." + year + " " + previousEndHour + ":" + previousEndMinute + " - " +
                                    "24" + ":" + "00" + " - FREE");
                            reservationsLayout.addView(reservationView);
                        }
                    } else {
                        //If only one reservation made it through the filters
                        //No sorting must be done
                        // and no iteration through the list is done for printing reservations
                        //other steps are the same
                        TextView reservationView = new TextView(context);
                        day = (selectedReservations.get(0).begins.getDayOfMonth() + "");
                        month = (selectedReservations.get(0).begins.getMonthValue() + "");
                        year = (selectedReservations.get(0).begins.getYear() + "");
                        startHour = (selectedReservations.get(0).begins.getHour() + "");
                        startMinute = (selectedReservations.get(0).begins.getMinute() + "");
                        endHour = (selectedReservations.get(0).ends.getHour() + "");
                        endMinute = (selectedReservations.get(0).ends.getMinute() + "");
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
                            endHour = "0" +endHour;
                        }
                        if (endMinute.length() == 1) {
                            endMinute = "0" + endMinute;
                        }
                        sport = "";
                        if (!selectedReservations.get(0).sport.isEmpty()) {
                            sport = "\n\tSport: " + selectedReservations.get(0).sport;
                        }
                        String previousEndHour = "00";
                        String previousEndMinute = "00";
                        if ((Integer.parseInt(previousEndHour) < Integer.parseInt(startHour)) || ((Integer.parseInt(previousEndHour) == Integer.parseInt(startHour)) &&
                                (Integer.parseInt(previousEndMinute) < Integer.parseInt(startMinute)))) {
                            reservationView = new TextView(context);
                            reservationView.setText(roomNamesArrayList.get(selectedReservations.get(0).roomId) + " " + day + "." + month +
                                    "." + year + " " + previousEndHour + ":" + previousEndMinute + " - " +
                                    startHour + ":" + startMinute + " - FREE");
                            reservationsLayout.addView(reservationView);
                        }
                        reservationView = new TextView(context);
                        reservationView.setText(roomNamesArrayList.get(selectedReservations.get(0).roomId) + " " + day + "." + month +
                                "." + year + " " + startHour + ":" + startMinute + " - " +
                                endHour + ":" + endMinute + " Reason: " + selectedReservations.get(0).reason + sport);
                        reservationsLayout.addView(reservationView);
                        if (selectedReservations.get(selectedReservations.size() - 1).ends.getHour() < 24) {
                            reservationView = new TextView(context);
                            reservationView.setText(roomNamesArrayList.get(selectedReservations.get(selectedReservations.size() - 1).roomId) + " " + day + "." + month +
                                    "." + year + " " + previousEndHour + ":" + previousEndMinute + " - " +
                                    "24" + ":" + "00" + " - FREE");
                            reservationsLayout.addView(reservationView);
                        }
                    }
                }
            }
        } catch (Exception e) {
            outputText.setText("Format your input according to the hints shown in empty fields");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addReservation() {
        reservations = singleton.getReservations();
        try {
            //overlap used to check if there already is an overlapping reservation
            overlap = 0;
            if ((!ddText.getText().toString().isEmpty()) && (!mmText.getText().toString().isEmpty()) && (!yyyyText.getText().toString().isEmpty()) &&
                    (!timeText.getText().toString().isEmpty()) && (!endTimeText.getText().toString().isEmpty()) && (selectedRoom != roomNamesArrayList.size() - 1)) {
                //given info is formatted to be similar every time
                day = ddText.getText().toString();
                month = mmText.getText().toString();
                year = yyyyText.getText().toString();
                startHour = timeText.getText().toString().split(":")[0];
                startMinute = timeText.getText().toString().split(":")[1];
                endHour = endTimeText.getText().toString().split(":")[0];
                endMinute = endTimeText.getText().toString().split(":")[1];
                if ((Integer.parseInt(startHour) < (Integer.parseInt(endHour))) || ((Integer.parseInt(startHour) == Integer.parseInt(endHour)) && (Integer.parseInt(startMinute) < Integer.parseInt(endMinute)))) {
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
                    sport = "";
                    if (selectedSport != 0) {
                        sport = singleton.getSports().get(selectedSport);
                    }
                    int reservationsSize = 0;
                    if (singleton.getReservations().size() > 0) {
                        reservationsSize = singleton.getReservations().size();
                    }
                    //new instance of Reservation is made with given info. It is only added permanently if it fills all the later inspected criteria for a reservation
                    Reservation selectedReservation = new Reservation(LocalDateTime.parse(year + "-" + month + "-" +
                            day + "T" + startHour + ":" + startMinute + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME), (LocalDateTime.parse(year + "-" + month + "-" +
                            day + "T" + endHour + ":" + endMinute + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)), singleton.getActiveUser().id, selectedRoom, reasonText.getText().toString(), sport,
                            reservationsSize);
                    //only reservation in the future allowed
                    LocalDateTime currentDate = LocalDateTime.now();
                    if (selectedReservation.begins.isAfter(currentDate)) {
                        //reservations can be made only 180 days in advance
                        int daysIntoTheFuture = (selectedReservation.begins.getYear() - currentDate.getYear())*365 + (selectedReservation.begins.getDayOfYear() - currentDate.getDayOfYear());
                        if (daysIntoTheFuture < 180) {
                            //Checks for overlapping reservations
                            for (int i = 0; i < reservations.size(); i++) {
                                if ((selectedReservation.roomId == reservations.get(i).roomId) && (selectedReservation.begins.isBefore(reservations.get(i).ends)) && (selectedReservation.ends.isAfter(reservations.get(i).begins))) {
                                    TextView reservationView = new TextView(context);
                                    day = (reservations.get(i).begins.getDayOfMonth() + "");
                                    month = (reservations.get(i).begins.getMonthValue() + "");
                                    year = (reservations.get(i).begins.getYear() + "");
                                    startHour = (reservations.get(i).begins.getHour() + "");
                                    startMinute = (reservations.get(i).begins.getMinute() + "");
                                    endHour = (reservations.get(i).ends.getHour() + "");
                                    endMinute = (reservations.get(i).ends.getMinute() + "");
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
                                    reservationView.setText(roomNamesArrayList.get(reservations.get(i).roomId) + " " + day + "." + month +
                                            "." + year + " " + startHour + ":" + startMinute + " - " +
                                            endHour + ":" + endMinute + " - " + reservations.get(i).reason + reservations.get(i).sport);
                                    reservationsLayout.removeAllViews();
                                    reservationsLayout.addView(reservationView);
                                    outputText.setText("Overlapping reservation, reservation not made");
                                    overlap = 1;
                                    break;
                                }
                            }
                            if (overlap == 0) {
                                //Reservation filled all criteria. Adds reservation to singletons ArrayList and to the reservations file
                                reservations.add(reservations.size(), selectedReservation);
                                User activeUser = singleton.getActiveUser();
                                activeUser.reservations.add(selectedReservation.id);
                                ArrayList<User> users = singleton.getUsers();
                                users.add(activeUser.id, activeUser);
                                users.remove(activeUser.id + 1);
                                singleton.setUsers(users);
                                singleton.setReservations(reservations);
                                fileWriterObject.writeUsersToFile("users.csv", singleton.getUsers());
                                fileWriterObject.writeReservationsToFile("reservations.csv", singleton.getReservations());
                                outputText.setText("Reservation added");
                            }
                        } else {
                            outputText.setText("Reservations can be made a maximum of 180 days ahead of time");
                        }
                    } else {
                        outputText.setText("Selected date has already passed");
                    }
                } else {
                    outputText.setText("Reservation must start before it ends");
                }
            } else if ((selectedRoom == roomNamesArrayList.size() - 1)) {
                outputText.setText("A single room must be selected to add reservation");
            } else {
                outputText.setText("All fields must be filled to add reservation");
            }
        } catch (Exception e) {
            outputText.setText("Format your input according to the hints shown in empty fields");
        }
    }
}
