package com.example.studentschedualerc868.db;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studentschedualerc868.MultiPageActivity;
import com.example.studentschedualerc868.R;
import com.example.studentschedualerc868.db.DAOs.AssessmentDAO;
import com.example.studentschedualerc868.db.DAOs.CourseDAO;
import com.example.studentschedualerc868.db.DAOs.TermDAO;
import com.example.studentschedualerc868.db.Entities.Assessment;
import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.Term;
import com.example.studentschedualerc868.db.Entities.User;
import com.example.studentschedualerc868.db.DAOs.UserDAO;

import java.util.Calendar;
import java.util.List;

@Database(entities = {User.class, Term.class, Course.class, Assessment.class}, version = 1)
public abstract class DatabaseConn extends RoomDatabase {

    public abstract UserDAO getUserDAO();
    public abstract TermDAO getTermDao();
    public abstract CourseDAO getCourseDao();
    public abstract AssessmentDAO getAssessmentDao();


    private static DatabaseConn INSTANCE;


    public static DatabaseConn getDBInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseConn.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;

    }

    public static class AddNewAssessment extends AppCompatActivity {

        private int courseID;
        CourseDAO courseDAO;
        AssessmentDAO assessmentDAO;
        List<Course> allCourse;
        AutoCompleteTextView autoCompleteTextView;
        ArrayAdapter<String> adapterItems;
        private DatePickerDialog assessmentEndDatePickerDialog;
        private Button assessmentEndDateButton;
        DatePickerDialog.OnDateSetListener assessmentEndDateSetListener;

        RadioGroup radioGroup;
        RadioButton perfomanceButton, objectiveButton, upcomingButton, inProgressButton, completedButton;
        String selectedTypeButton, selectedStatusButton;
        TextView textView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_new_assessment);

            assessmentDAO = getDBInstance(getApplicationContext()).getAssessmentDao();
            courseDAO = getDBInstance(getApplicationContext()).getCourseDao();

            assessmentEndDateButton = findViewById(R.id.assessmentEndDatePickerButton);

            autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

            perfomanceButton = (RadioButton)findViewById(R.id.radio_performance);
            objectiveButton = (RadioButton)findViewById(R.id.radio_objective);

            upcomingButton = (RadioButton)findViewById(R.id.radio_asse_upcoming);
            inProgressButton = (RadioButton)findViewById(R.id.radio_asse_in_progress);
            completedButton = (RadioButton)findViewById(R.id.radio_asse_completed);

            courseDAO = getDBInstance(getApplicationContext()).getCourseDao();
            assessmentDAO = getDBInstance(getApplicationContext()).getAssessmentDao();

            setTitle("Add New Assessment");

            allCourse = courseDAO.getAllCourses();

            String[] courseTitles = new String[allCourse.size()];
            for (int i = 0; i<courseTitles.length; i++) {
                courseTitles[i] = allCourse.get(i).getCourseTitle();
            }

            adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_item, courseTitles);

            autoCompleteTextView.setAdapter(adapterItems);
            //

            assessmentEndDatePickerDialog = new DatePickerDialog(
                    AddNewAssessment.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            // Set the end date text when the user chooses a date
                            assessmentEndDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );

            assessmentEndDateButton.setText(getTodaysDate());

            assessmentEndDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assessmentEndDatePickerDialog.show();
                }
            });

            final EditText assessmentTitleInput = findViewById(R.id.assessmentTitleInput);
            final RadioGroup assessmentTypeInput = findViewById(R.id.asse_type_radiogroup);
            final RadioGroup assessmentStatusInput = findViewById(R.id.asse_status_radiogroup);
            final Button assessmentEndDateInput = findViewById(R.id.assessmentEndDatePickerButton);
            final EditText assessmentNoteInput = findViewById(R.id.assessmentNoteInput);

            Button saveButton = findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (assessmentTypeInput.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select an assessment type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (assessmentStatusInput.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select an assessment status", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (assessmentEndDateInput.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter an assessment end date", Toast.LENGTH_SHORT).show();
                        return;
                    }




                    if ((perfomanceButton.isChecked())) {
                        selectedTypeButton = perfomanceButton.getText().toString();
                    } else if(objectiveButton.isChecked()) {
                        selectedTypeButton = objectiveButton.getText().toString();
                    }

                    if (upcomingButton.isChecked()) {
                        selectedStatusButton = upcomingButton.getText().toString();
                    } else if(inProgressButton.isChecked()) {
                        selectedStatusButton = inProgressButton.getText().toString();
                    } else if(completedButton.isChecked()) {
                        selectedStatusButton = completedButton.getText().toString();
                    }


                    String endDate = assessmentEndDateInput.getText().toString().trim();
                    if (endDate.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    saveNewCourse(assessmentTitleInput.getText().toString(), selectedTypeButton, selectedStatusButton, assessmentEndDateInput.getText().toString(), assessmentNoteInput.getText().toString());

                }
            });



        }
        private void saveNewCourse(String assessmentTitle, String assessmentType, String assessmentStatus, String assessmentEndDate, String assessmentNote) {

            String selectedCourse = autoCompleteTextView.getText().toString();
            if (selectedCourse.trim().isEmpty() | selectedCourse.equals("Choose a Course") ) {
                Toast.makeText(this, "Please select a Course.", Toast.LENGTH_SHORT).show();
                return;
            }

            courseID = courseDAO.getCourseIDByTitle(selectedCourse);

            Assessment assessment = new Assessment(courseID, assessmentTitle, assessmentType, assessmentStatus, assessmentEndDate, assessmentNote);

            assessmentDAO.insertAssessment(assessment);
            launchMultiPageActivity();
            super.finish();
        }

        private String getTodaysDate() {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            month = month +1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return makeDateString(month, day, year);

        }

        private void initDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            assessmentEndDatePickerDialog = new DatePickerDialog(AddNewAssessment.this, assessmentEndDateSetListener, year, month, day);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_DeviceDefault_Dialog,
                    assessmentEndDateSetListener,
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }


        private String makeDateString(int year, int month, int day) {
            return year + "/" + month + "/" + day;
        }

        public void openAssessmentEndDatePicker(View view) {
            assessmentEndDatePickerDialog.show();
        }

        private void launchMultiPageActivity() {
            Intent i = new Intent(this, MultiPageActivity.class);
            i.putExtra("type", "Assessments");
            startActivity(i);
        }
        @Override
        public void onBackPressed() {
            launchMultiPageActivity();
            super.finish();
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
}
