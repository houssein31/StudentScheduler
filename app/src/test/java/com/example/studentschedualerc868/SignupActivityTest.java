package com.example.studentschedualerc868;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import junit.framework.TestCase;

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

    public void testOnCreate() {
    }

    @Mock
    UserDAO mockUserDAO;

    @Test
    public void testValidateUsername() {
//        // Mock the userDAO.getUserByUsername(username) method
//        String existingUsername = "existingUser";
//        when(mockUserDAO.getUserByUsername(existingUsername)).thenReturn(new User(existingUsername));
//
//        Context context = ApplicationProvider.getApplicationContext();
//        boolean result = SignupActivity.validateUsername(context, mockUserDAO, existingUsername);
//
//        // Verify that the toast message is shown and the result is true
//        Mockito.verify(mockUserDAO).getUserByUsername(existingUsername);
//        assertEquals(true, result);
    }



    @Test
    public void testValidatePassword() {
    }

    @Test
    public void testValidatePasswordRequirements() {
    }
}