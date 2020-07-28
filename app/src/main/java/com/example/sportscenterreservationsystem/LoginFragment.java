package com.example.sportscenterreservationsystem;

import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginFragment extends Fragment {


    View view;
    EditText usernameText;
    EditText passwordText;
    EditText fnameText;
    EditText lnameText;
    EditText emailText;
    EditText phoneText;
    TextView outputText;
    Button loginbutton;
    Button createuserbutton;
    FileWriterObject fileWriterObject = new FileWriterObject();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        return view;
}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usernameText = (EditText) view.findViewById(R.id.usernameEditText);
        passwordText = (EditText) view.findViewById(R.id.passwordEditText);
        fnameText = (EditText) view.findViewById(R.id.fNameEditText);
        lnameText = (EditText) view.findViewById(R.id.lNameEditText);
        emailText = (EditText) view.findViewById(R.id.emailEditText);
        phoneText = (EditText) view.findViewById(R.id.phoneEditText);
        outputText = (TextView) view.findViewById(R.id.outputTextView);
        loginbutton = (Button) view.findViewById(R.id.loginButton);
        createuserbutton = (Button) view.findViewById(R.id.createAccountButton);
        loginbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        createuserbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    //Checks if login is valid. If valid stores active user into singleton
    //loginSuccessful value is only a temporary value used to print the correct text
    public void login() {
        Singleton singleton = Singleton.getInstance();
        ArrayList<User> users = singleton.getUsers();
        int loginSuccessful = 0;
        if (!users.isEmpty()) {
            String givenUsername = usernameText.getText().toString();
            String givenPassword = passwordText.getText().toString();
            for (int i = 0; i < users.size(); i++) {
                if ((givenUsername.equals(users.get(i).username)) && (givenPassword.equals(users.get(i).password))) {
                    singleton.setActiveUser(users.get(i));
                    loginSuccessful = 1;
                    break;
                }
            }
            if (loginSuccessful == 1) {
                outputText.setText("LOGIN SUCCESSFUL");
            } else {
                outputText.setText("INVALID USERNAME OR PASSWORD");
            }
        } else {
            outputText.setText("NO EXISTING USERS");
        }
    }

    //Checks if account with the same username already exists
    // if not, creates new account with given details if enough details are provided
    // Created account is added to singletons list of users, and is also added to the file of users
    public void createAccount() {
        Singleton singleton = Singleton.getInstance();
        ArrayList<User> users = singleton.getUsers();
        if ((!usernameText.getText().toString().isEmpty()) && (!passwordText.getText().toString().isEmpty()) && (!fnameText.getText().toString().isEmpty()) && (!lnameText.getText().toString().isEmpty())
                && (!emailText.getText().toString().isEmpty()) && (!phoneText.getText().toString().isEmpty())) {
            if (passwordText.getText().toString().length() < 3) {
                outputText.setText("Password must be at least 12 characters long!");
            }
            //TODO: salasanan laadun varmistaminen else if rakenteella
            else if (users.isEmpty()) {
                users.add(new User(usernameText.getText().toString(), passwordText.getText().toString(), fnameText.getText().toString(), lnameText.getText().toString(), emailText.getText().toString(),
                        phoneText.getText().toString(), 0, new ArrayList<Integer>()));
                singleton.setUsers(users);
                fileWriterObject.writeUsersToFile("users.csv", singleton.getUsers());
                outputText.setText("ACCOUNT CREATED");
            } else {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).username.equals(usernameText.getText().toString())) {
                        outputText.setText("Username is already taken.");
                        return;
                    }
                }
                users.add(users.size(), new User(usernameText.getText().toString(), passwordText.getText().toString(), fnameText.getText().toString(), lnameText.getText().toString(), emailText.getText().toString(),
                        phoneText.getText().toString(), users.size(), new ArrayList<Integer>()));
                singleton.setUsers(users);
                fileWriterObject.writeUsersToFile("users.csv", singleton.getUsers());
                outputText.setText("ACCOUNT CREATED");
            }
        } else {
            outputText.setText("All fields must be filled to create an account!");
        }
    }

}