package com.example.studentschedualerc868;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    Button termBtn, courseBtn, assessmentBtn;
    TextView tv;
    String username;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        // Set a custom action bar layout
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // Enable the back button in the action bar if needed
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termBtn = findViewById(R.id.term_btn);
        courseBtn = findViewById(R.id.course_btn);
        assessmentBtn = findViewById(R.id.assessment_btn);
        tv = findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();

        if(extras!= null){

           username = extras.getString("username");
        }

        tv.setText("Hello " + username);



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        return true;
//    }

    public void launchTerm(View v) {
        //Launch Term


        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", termBtn.getText());
        i.putExtra("username", username);
        startActivity(i);
    }

    public void launchCourse(View v) {
        //Launch Course

        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", courseBtn.getText());
        i.putExtra("username", username);
        startActivity(i);
    }

    public void launchAssessment(View v) {
        //Launch Assessments

        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", assessmentBtn.getText());
        i.putExtra("username", username);
        startActivity(i);
    }

    private void launchMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
//        i.putExtra("type", "Terms");
//        i.putExtra("username", username);

        startActivity(i);
        super.finish();
    }

    public void launchSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        launchMainActivity();
//        super.finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.action_search) {
//            // Perform search action
//            performSearch();
//            return true;
//        } else if (itemId == android.R.id.home) {
//            // Handle back button click if needed
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void performSearch() {
//        // Perform the search action
//        // For example, open a search activity or display a search dialog
//        // ...
//        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
//    }

}