package com.example.studentschedualerc868;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studentschedualerc868.db.DAOs.AssessmentDAO;
import com.example.studentschedualerc868.db.DAOs.CourseDAO;
import com.example.studentschedualerc868.db.DAOs.TermDAO;
import com.example.studentschedualerc868.db.DatabaseConn;
import com.example.studentschedualerc868.db.Entities.Assessment;
import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.Term;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String username;
    private static final int SEARCH_CATEGORY_TERMS = 1;
    private static final int SEARCH_CATEGORY_COURSES = 2;
    private static final int SEARCH_CATEGORY_ASSESSMENTS = 3;
    EditText searchEditText;
    RadioGroup searchRadioGroup;
    Button searchButton;

    String searchQuery;
    int selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            username = extras.getString("username");
        }

        setTitle("Search");

        searchEditText = findViewById(R.id.search_edittext);
        searchRadioGroup = findViewById(R.id.search_radiogroup);
        searchButton = findViewById(R.id.search_button);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

    }

    private void performSearch() {
        // Get the search query
        searchQuery = searchEditText.getText().toString().trim();

        // Check if the search query is empty
        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the search query is empty
        }

        // Get the selected search category
        int selectedRadioButtonId = searchRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.terms_radio) {
            selectedCategory = SEARCH_CATEGORY_TERMS;
        } else if (selectedRadioButtonId == R.id.courses_radio) {
            selectedCategory = SEARCH_CATEGORY_COURSES;
        } else if (selectedRadioButtonId == R.id.assessments_radio) {
            selectedCategory = SEARCH_CATEGORY_ASSESSMENTS;
        }

        // Perform the search based on the selected category
        switch (selectedCategory) {
            case SEARCH_CATEGORY_TERMS:
                searchTerms(searchQuery);
                break;
            case SEARCH_CATEGORY_COURSES:
                searchCourses(searchQuery);
                break;
            case SEARCH_CATEGORY_ASSESSMENTS:
                searchAssessments(searchQuery);
                break;
        }
    }

    private void searchTerms(String query) {
        TermDAO termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        List<Term> searchResults = termDAO.searchTermsByTitleAndUserName(query, username);

        // Handle the search results

        // Initialize the RecyclerView
        RecyclerView searchRecyclerView = findViewById(R.id.search_recyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter and set it on the RecyclerView
        TermListAdapter termListAdapter = new TermListAdapter(this, searchResults, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DisplayTerm.class);

                intent.putExtra("termID", searchResults.get(position).getTermId());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        searchRecyclerView.setAdapter(termListAdapter);
    }



    private void searchCourses(String query) {
        TermDAO termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        CourseDAO courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        List<Course> searchResults = courseDAO.searchCoursesByTitleAmongTerms(termDAO.getTermIdsByUsername(username), query);

        // Handle the search results

        // Initialize the RecyclerView
        RecyclerView searchRecyclerView = findViewById(R.id.search_recyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter and set it on the RecyclerView
        CourseListAdapter courseListAdapter = new CourseListAdapter(this, searchResults, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DisplayCourse.class);

                intent.putExtra("courseID", searchResults.get(position).getCourseID());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        searchRecyclerView.setAdapter(courseListAdapter);
    }


    private void searchAssessments(String query) {
        TermDAO termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        CourseDAO courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        AssessmentDAO assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        List<Assessment> searchResults = assessmentDAO.searchAssessmentsByTitleAmongCourses(courseDAO.searchCourseIDsAmongTerms(termDAO.getTermIdsByUsername(username)), query);

                // Initialize the RecyclerView
        RecyclerView searchRecyclerView = findViewById(R.id.search_recyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter and set it on the RecyclerView
        AssessmentListAdapter assessmentListAdapter = new AssessmentListAdapter(this, searchResults, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DisplayAssessment.class);

                intent.putExtra("assessmentID", searchResults.get(position).getAssessmentID());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        searchRecyclerView.setAdapter(assessmentListAdapter);

    }

    private void launchHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("type", "Assessments");
        i.putExtra("username", username);
        startActivity(i);
        super.finish();

    }

    @Override
    public void onBackPressed() {
        launchHomeActivity();
//        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}