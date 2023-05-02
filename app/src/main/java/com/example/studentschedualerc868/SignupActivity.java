package com.example.studentschedualerc868;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentschedualerc868.db.Entities.User;
import com.example.studentschedualerc868.db.DAOs.UserDAO;
import com.example.studentschedualerc868.db.DatabaseConn;

import org.junit.Test;

public class SignupActivity extends AppCompatActivity {
    private EditText fullName, userName, passWord, confirmPassword;
    private Button registerButton;

    public static UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        // Initialize userDAO
        userDAO = DatabaseConn.getDBInstance(getApplicationContext()).getUserDAO();

        // Get views
        fullName= findViewById(R.id.fullNameEditText);
        userName = findViewById(R.id.signupUsernameEditText);
        passWord = findViewById(R.id.signupPasswordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEdittext);
        registerButton = findViewById(R.id.registerButton);

        // Set up sign up button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fullName.getText().toString();
                String username = userName.getText().toString();
                String password =passWord.getText().toString();
                String confirmpassword = confirmPassword.getText().toString();

                //Validations

//                // Check if username already exists
//                if (userDAO.getUserByUsername(userName.getText().toString()) != null) {
//                    Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
//                    return; // Exit the method without inserting the user
//                }


                if (validateUsername(username)) {
                    return;
                }

                if (validatePassword(password, confirmpassword)) {
                    return;
                }

                if (validatePasswordRequirements(password, confirmpassword)) {
                    return;
                }

//                // Check if password and confirm password match
//                if (!password.equals(confirmpassword)) {
//                    Toast.makeText(SignupActivity.this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
//                    return; // Exit the method without inserting the user
//                }
//
//                // Check if password and confirm password meet requirements
//                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$") ||
//                        !confirmpassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$")) {
//                    Toast.makeText(SignupActivity.this, "Password and confirm password must be between 6 and 16 characters and contain at least one uppercase and lowercase letter and one digit", Toast.LENGTH_SHORT).show();
//                    return; // Exit the method without inserting the user
//                }

                // Create new user
                User newUser = new User(fullname, username, password);

                // Insert user into database
                userDAO.insertUser(newUser);
                if (newUser != null) {
                    Toast.makeText(SignupActivity.this, newUser.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

                // Go back to login activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean validateUsername(String username) {
        if (userDAO.getUserByUsername(username) != null) {
            Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    public boolean validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignupActivity.this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean validatePasswordRequirements(String password, String confirmPassword) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$") ||
                !confirmPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$")) {
            Toast.makeText(SignupActivity.this, "Password and confirm password must be between 6 and 16 characters and contain at least one uppercase and lowercase letter and one digit", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


}


//// Check if password and confirm password meet requirements
//                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z]).{6,16}$") ||
//                        !confirmpassword.matches("^(?=.*[a-z])(?=.*[A-Z]).{6,16}$")) {
//                        Toast.makeText(SignupActivity.this, "Password and confirm password must be between 6 and 16 characters and contain at least one uppercase and lowercase letter", Toast.LENGTH_SHORT).show();
//                        return; // Exit the method without inserting the user
//                        }