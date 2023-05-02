package com.example.studentschedualerc868;

import com.example.studentschedualerc868.SignupActivity;
import static com.example.studentschedualerc868.SignupActivity.validatePassword;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import junit.framework.TestCase;

//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

//import com.google.android.material.textfield.TextInputLayout;
//
//import junit.framework.TestCase;
//
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.studentschedualerc868.db.DAOs.UserDAO;
import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.User;
import com.google.android.material.textfield.TextInputLayout;

@RunWith(MockitoJUnitRunner.class)
public class AddNewCourseTest {

//    @Test
//    public void testIsStatusRadioButtonSelected() {
//        RadioGroup mockRadioGroup = mock(RadioGroup.class);
//
//        // Set up the mockRadioGroup behavior
//        when(mockRadioGroup.getCheckedRadioButtonId()).thenReturn(123);
//
//        assertFalse(SignupActivity.isStatusRadioButtonSelected(mockRadioGroup));
//    }
}
