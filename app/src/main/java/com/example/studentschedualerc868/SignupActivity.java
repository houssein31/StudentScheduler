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

        userDAO = DatabaseConn.getDBInstance(getApplicationContext()).getUserDAO();

        fullName= findViewById(R.id.fullNameEditText);
        userName = findViewById(R.id.signupUsernameEditText);
        passWord = findViewById(R.id.signupPasswordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEdittext);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fullName.getText().toString();
                String username = userName.getText().toString();
                String password =passWord.getText().toString();
                String confirmpassword = confirmPassword.getText().toString();

                //Validations

                if (validateUsername(registerButton, username)) {
                    return;
                }

                if (validatePassword(registerButton, password, confirmpassword)) {
                    return;
                }

                if (validatePasswordRequirements(registerButton, password, confirmpassword)) {
                    return;
                }


                User newUser = new User(fullname, username, password);

                userDAO.insertUser(newUser);
                if (newUser != null) {
//                    Toast.makeText(SignupActivity.this, newUser.toString(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(SignupActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static boolean validateUsername(View view, String username) {
        if (userDAO.getUserByUsername(username) != null) {
            Context context = view.getContext();
            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    public static boolean validatePassword(View view, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Context context = view.getContext();
            Toast.makeText(context, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean validatePasswordRequirements(View view, String password, String confirmPassword) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$") ||
                !confirmPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$")) {
            Context context = view.getContext();
            Toast.makeText(context, "Password and confirm password must be between 6 and 16 characters and contain at least one uppercase and lowercase letter and one digit", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
