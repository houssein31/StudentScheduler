package com.example.studentschedualerc868;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentschedualerc868.db.DAOs.UserDAO;
import com.example.studentschedualerc868.db.DatabaseConn;
import com.example.studentschedualerc868.db.Entities.User;

//public class LoginActivity extends AppCompatActivity {
//
//    private EditText fullName, userName, passWord, confirmPassword;
//    private Button enter;
//
//    private UserDAO userDAO;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        setTitle("Log in");
//
//        // Initialize userDAO
//        userDAO = DatabaseConn.getDBInstance(getApplicationContext()).getUserDAO();
//
//        // Get views
//        userName = findViewById(R.id.loginUsernameEditText);
//        passWord =  findViewById(R.id.loginPasswordEditText);
//        enter = findViewById(R.id.enterButton);
//
//        //Validation
//
//    }
//}
public class LoginActivity extends AppCompatActivity {

    private EditText userName, passWord;
    private Button enter;

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in");

        userDAO = DatabaseConn.getDBInstance(getApplicationContext()).getUserDAO();

        userName = findViewById(R.id.loginUsernameEditText);
        passWord = findViewById(R.id.loginPasswordEditText);
        enter = findViewById(R.id.enterButton);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String password = passWord.getText().toString();

                User user = userDAO.getUserByUsername(username);

                if (user == null) {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without logging in
                }

                if (!password.equals(user.getPassword())) {
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without logging in
                }

                String fullName = userDAO.getFullName(username);
                Toast.makeText(LoginActivity.this, "Logged in as " + fullName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });
    }
}

