package com.example.studentschedualerc868;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.studentschedualerc868.db.DAOs.AssessmentDAO;
import com.example.studentschedualerc868.db.DAOs.CourseDAO;
import com.example.studentschedualerc868.db.DAOs.TermDAO;
import com.example.studentschedualerc868.db.DatabaseConn;
import com.example.studentschedualerc868.db.Entities.Assessment;
import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.Term;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MultiPageActivity extends AppCompatActivity implements RecyclerViewInterface{

    String type;
    String username;

    RecyclerView recyclerView;
    TermDAO termDAO;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;
    Button addNewItemButton;
    private TermListAdapter termListAdapter;
    private  CourseListAdapter courseListAdapter;
    private AssessmentListAdapter assessmentListAdapter;

    List<Term> termList;
    List<Course> courseList;
    List<Assessment> assessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_page);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            type = extras.getString("type");
            username = extras.getString("username");
        }

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        setTitle(type);

        addNewItemButton = findViewById(R.id.add_new_item_button);
        addNewItemButton.setText("Add new " + type);

        recyclerView = findViewById(R.id.recyclerView);

        initRecyclerView(type);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (type){
                case "Terms":

                    int termID = termList.get(position).getTermId();

                    if (termDAO.hasCourse(termID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiPageActivity.this);
                        builder.setMessage("This term cannot be deleted because it has courses associated with it. Delete all courses associated with this term and then delete it.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        initRecyclerView(type);

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        termList.remove(position);
                        termListAdapter.notifyItemRemoved(position);
                        termDAO.delete(termID);
                    }

                    break;
                case "Courses":

                    int courseID = courseList.get(position).getCourseID();

                    if (courseDAO.hasAssessment(courseID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiPageActivity.this);
                        builder.setMessage("This course cannot be deleted because it has assessments associated with it. Delete all assessment associated with this course and then delete it.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        initRecyclerView(type);

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        courseList.remove(position);
                        courseListAdapter.notifyItemRemoved(position);
                        courseDAO.delete(courseID);
                    }
                    break;
                case "Assessments":
                    int assessmentID = assessmentList.get(position).getAssessmentID();
                    assessmentList.remove(position);
                    assessmentListAdapter.notifyItemRemoved(position);
                    assessmentDAO.delete(assessmentID);
                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MultiPageActivity.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)

                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void addItem(View view){

        switch (type){
            case "Terms":
                launchAddTerm();
                break;
            case "Courses":
                launchAddCourses();
                break;
            case "Assessments":
                launchAddAssessments();
                break;
        }


    }

    private void launchAddAssessments() {
        Intent i = new Intent(this, AddNewAssessment.class);
        i.putExtra("username", username);
        i.putExtra("type", "Assessments");

        startActivity(i);
        super.finish();
    }

    private void launchAddCourses() {
        Intent i = new Intent(this, AddNewCourse.class);
        i.putExtra("username", username);
        i.putExtra("type", "Courses");

        startActivity(i);
        super.finish();
    }

    private void launchAddTerm() {
        Intent i = new Intent(this, AddNewTerm.class);
        i.putExtra("username", username);
        i.putExtra("type", "Terms");

        startActivity(i);
        super.finish();
    }


    private void initRecyclerView(String type) {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        termList = termDAO.getTermsByUsername(username);
                List<Integer> termIds = new ArrayList<>();
                for (Term term : termList) {
                    termIds.add(term.getTermId());
                }
                courseList = courseDAO.getCoursesByTermIdList(termIds);

        switch (type){
            case "Terms":
                termListAdapter = new TermListAdapter(this, termList, this);
                recyclerView.setAdapter(termListAdapter);
                break;
            case "Courses":
                courseListAdapter = new CourseListAdapter(this.getApplicationContext(), courseList, this);
                recyclerView.setAdapter(courseListAdapter);
                break;
            case "Assessments":
                List<Integer> courseIds = new ArrayList<>();
                for (Course course : courseList) {
                    courseIds.add(course.getCourseID());
                }
                assessmentList = assessmentDAO.getAssessmentsByCourseIdList(courseIds);
                assessmentListAdapter = new AssessmentListAdapter(this.getApplicationContext(), assessmentList, this);
                recyclerView.setAdapter(assessmentListAdapter);
                break;
        }

    }

    private void loadTermList() { termList = termDAO.getTermsByUsername(username); }
    private void loadCourseList() {
        courseList = courseDAO.getAllCourses();
    }

    private void loadAssessmentList() { assessmentList = assessmentDAO.getAllAssessments(); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            loadTermList();
            loadCourseList();
            loadAssessmentList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int position) {

        switch (type){
            case "Terms":
                Intent intentTerm = new Intent(MultiPageActivity.this, DisplayTerm.class);
                intentTerm.putExtra("username", username);
                intentTerm.putExtra("termID", termList.get(position).getTermId());
                startActivity(intentTerm);
                super.finish();
                break;
            case "Courses":
                Intent intentCourse = new Intent(MultiPageActivity.this, DisplayCourse.class);
                intentCourse.putExtra("username", username);
                intentCourse.putExtra("courseID", courseList.get(position).getCourseID());
                startActivity(intentCourse);
                super.finish();
                break;
            case "Assessments":
                Intent intentAssessment = new Intent(MultiPageActivity.this, DisplayAssessment.class);
                intentAssessment.putExtra("username", username);
//                Toast.makeText(this, String.valueOf(username), Toast.LENGTH_SHORT).show();
                intentAssessment.putExtra("assessmentID", assessmentList.get(position).getAssessmentID());
                startActivity(intentAssessment);
                break;
        }


    }
    private void launchHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("type", "Terms");
        i.putExtra("username", username);

        startActivity(i);
        super.finish();
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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