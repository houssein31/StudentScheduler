package com.example.studentschedualerc868;

import static com.example.studentschedualerc868.SignupActivity.validatePassword;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

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
import com.example.studentschedualerc868.db.Entities.User;
import com.google.android.material.textfield.TextInputLayout;

@RunWith(MockitoJUnitRunner.class)
public class SignupActivityTest extends TestCase {

    @Mock
    UserDAO mockUserDAO;

//    @Test
//    public void testValidateUsername() {
//        String username = "hkharro";
//
//        View mockView = mock(View.class);
//
//        when(mockUserDAO.getUserByUsername(username)).thenReturn(new User());
//        assertTrue(SignupActivity.validateUsername(mockView, username));
//
//    }

    @Test
    public void testValidatePassword() {
        String password = "password123";
        String confirmPassword = "password123";

        View mockView = mock(View.class);

        assertFalse(SignupActivity.validatePassword(mockView, password, confirmPassword));
    }

    @Test
    public void testValidatePasswordRequirements() {
        String password = "Password123";
        String confirmPassword = "Password123";

        View mockView = mock(View.class);

        assertFalse(SignupActivity.validatePasswordRequirements(mockView, password, confirmPassword));
    }
}