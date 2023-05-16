package com.example.studentschedualerc868;

import static com.itextpdf.kernel.xmp.XMPDateTimeFactory.getCurrentDateTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentschedualerc868.db.DAOs.UserDAO;
import com.example.studentschedualerc868.db.Entities.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.property.HorizontalAlignment;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.example.studentschedualerc868.db.DAOs.AssessmentDAO;
import com.example.studentschedualerc868.db.DAOs.CourseDAO;
import com.example.studentschedualerc868.db.DAOs.TermDAO;
import com.example.studentschedualerc868.db.DatabaseConn;
import com.example.studentschedualerc868.db.Entities.Assessment;
import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.Term;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button termBtn, courseBtn, assessmentBtn;
    TextView tv;
    String username;
    int age;
    UserDAO userDAO;
    TermDAO termDAO;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;


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

    public void exportPdf(View view) {
        String fileName = username + "-report-" + getCurrentDateTime() +".pdf";

        // Define the content resolver and content values for inserting the PDF file into the downloads directory
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();
        userDAO = DatabaseConn.getDBInstance(getApplicationContext()).getUserDAO();

        List<Term> termList = termDAO.getTermsByUsername(username);
        List<Integer> termIds = new ArrayList<>();

        for (Term term : termList) {
            termIds.add(term.getTermId());
        }
        List<Course> courseList = courseDAO.getCoursesByTermList(termIds);
        List<Integer> courseIds = new ArrayList<>();
        for (Course course : courseList) {
            courseIds.add(course.getCourseID());
        }
        List<Assessment> assessmentList = assessmentDAO.getAssessmentByCourseList(courseIds);

        try {
            // Create a new PDF file using the path defined
            PdfWriter writer = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                writer = new PdfWriter(contentResolver.openOutputStream(contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)));
            }

            PdfDocument pdfDoc = new PdfDocument(writer);

            // Create a new page in the PDF document
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("Student Scheduler");
            title.setFontSize(20f);
            title.setBold();
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph());

            // Retrieve user information from UserDAO
            User user = userDAO.getUserByUsername(username);
            String fullName = user.getFullname();
            String username = user.getUsername();

            // Add user information as a field above the table
            Paragraph userInfo = new Paragraph("Full Name: " + fullName + "\nUsername: " + username);
            document.add(userInfo);

            document.add(new Paragraph());

            Table termTable = new Table(3);

            termTable.addCell("Term Title");
            termTable.addCell("Term Start Date");
            termTable.addCell("Term End Date");
            for (Term term : termList) {
                termTable.addCell(term.getTermTitle());
                termTable.addCell(term.getTermStartDate().toString());
                termTable.addCell(term.getTermEndDate().toString());
            }

            document.add(new Paragraph());

            Table courseTable = new Table(7);

            courseTable.addCell("Course Title");
            courseTable.addCell("Course Instructor Name");
            courseTable.addCell("Course Instructor Email");
            courseTable.addCell("Course Instructor Phone");
            courseTable.addCell("Course Status");
            courseTable.addCell("Course Start Date");
            courseTable.addCell("Course End Date");
            for (Course course : courseList) {
                courseTable.addCell(course.getCourseTitle());
                courseTable.addCell(course.getCourseInstructorName());
                courseTable.addCell(course.getCourseInstructorEmail());
                courseTable.addCell(course.getCourseInstructorPhone());
                courseTable.addCell(course.getCourseStatus());
                courseTable.addCell(course.getCourseStartDate());
                courseTable.addCell(course.getCourseEndDate());
            }

            document.add(new Paragraph());

            Table assessmentTable = new Table(4);

            assessmentTable.addCell("Assessment Title");
            assessmentTable.addCell("Assessment Type");
            assessmentTable.addCell("Assessment Status");
            assessmentTable.addCell("Assessment End Date");
            for (Assessment assessment : assessmentList) {
                assessmentTable.addCell(assessment.getAssessmentTitle());
                assessmentTable.addCell(assessment.getAssessmentType());
                assessmentTable.addCell(assessment.getAssessmentStatus());
                assessmentTable.addCell(assessment.getAssessmentEndDate());
            }

            document.add(termTable);
            document.add(new Paragraph("\n"));
            document.add(courseTable);
            document.add(new Paragraph("\n"));
            document.add(assessmentTable);
            document.add(new Paragraph("\n"));

            // Closing the document
            document.close();

            // Display a toast message to indicate that the PDF file has been created and saved
            Toast.makeText(this, "PDF file saved to " + Environment.DIRECTORY_DOWNLOADS + "/" + fileName, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Display a toast message to indicate that an error occurred while creating the PDF file
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}