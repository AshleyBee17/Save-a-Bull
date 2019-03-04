//package edu.usf.cse.labrador.save_a_bull;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//public class SignUpScreen extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up_screen);
//
//        Button signUpBtn = this.findViewById(R.id.createAcctBtn);
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                checkEntries();
//
//            }
//        });
//    }
//
//    private void checkEntries(){
//        /*
//        * Check that the user's entries are valid i.e. no numbers in first/last name
//        * if everything is good, save the info to the database and send the back to the welcome
//        * screen to log in
//        * if an entry is not correct, display an error until it is fixed
//        * */
//        Intent intent = new Intent(SignUpScreen.this, WelcomeScreen.class);
//        startActivity(intent);
//    }
//}

package edu.usf.cse.labrador.save_a_bull;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//import cse.usf.edu.android.db.UsersDBManager;
//import cse.usf.edu.android.db.UsersDataB;
//import cse.usf.edu.android.entities.User;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;

public class SignUpScreen extends AppCompatActivity {

    // UsersDBManager UsersDB = new UsersDBManager(this);
    private UsersDBManager myUsersDataB;
    //private User newUser;
    //private String fileName = "users.txt";
    long current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //Creates and Open Database
        myUsersDataB = new UsersDBManager(this);
        myUsersDataB.open();

        Button signUpBtn = this.findViewById(R.id.createAcctBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                checkEntries();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        myUsersDataB.close();
    }
    // Checks if all user entries are valid and if so, the user is saved to the DB and redirected to the Welcome screen
    private void checkEntries() {
        /*
         * Check that the user's entries are valid i.e. no numbers in first/last name
         * if everything is good, save the info to the database and send the back to the welcome
         * screen to log in
         * if an entry is not correct, display an error until it is fixed
         * */

        TextInputLayout fNameMessage = (TextInputLayout) findViewById(R.id.validateFirstName);
        TextInputLayout lNameMessage = (TextInputLayout) findViewById(R.id.validateLastName);
        TextInputLayout usernameMessage = (TextInputLayout) findViewById(R.id.validateUsername);
        TextInputLayout passwordMessage = (TextInputLayout) findViewById(R.id.validatePassword);

        String fName = fNameMessage.getEditText().getText().toString();
        String lName = lNameMessage.getEditText().getText().toString();
        String username = usernameMessage.getEditText().getText().toString();
        String password = passwordMessage.getEditText().getText().toString();

        fNameMessage.setHint("First Name");
        lNameMessage.setHint("Last Name");
        usernameMessage.setHint("Username");
        passwordMessage.setHint("Password");


        if(validateFirstName(fName) && validateLastName(lName) && validateUsername(username) && validatePassword(password)){

            fNameMessage.setErrorEnabled(false);
            lNameMessage.setErrorEnabled(false);
            usernameMessage.setErrorEnabled(false);
            passwordMessage.setErrorEnabled(false);

            fNameMessage.setError(null);
            lNameMessage.setError(null);
            usernameMessage.setError(null);
            passwordMessage.setError(null);

            //Saving user to Database
            current_user_id = myUsersDataB.createUser(fName, lName, username, password);

            //Taking the user back to Welcome screen.
            Intent intent = new Intent(SignUpScreen.this, WelcomeScreen.class);
            startActivity(intent);
        }

        else {

            if (!validateFirstName(fName)) {
                fNameMessage.setErrorEnabled(true);
                fNameMessage.setError("Invalid First Name. Enter letters only.");
            } else if (validateFirstName(fName)) fNameMessage.setError(null);

            if (!validateLastName(lName)) {
                lNameMessage.setErrorEnabled(true);
                lNameMessage.setError("Invalid Last Name. Enter letters only.");
            } else if (validateLastName(lName)) lNameMessage.setError(null);

            if (!validateUsername(username)) {
                usernameMessage.setErrorEnabled(true);
                usernameMessage.setError("Invalid or taken Username. Enter letters or digits only.");
            } else if (validateUsername(username)) usernameMessage.setError(null);

            if (!validatePassword(password)) {
                passwordMessage.setErrorEnabled(true);
                passwordMessage.setError("Invalid Password. Password must be 8 characters or more and has to include at least one capital letter, one lower case letter and one digit.");
            } else if (validatePassword(password)) passwordMessage.setError(null);

        }
    }
    // User Input validation functions
    private boolean validateFirstName(String fName) {
        char c;
        int len, i, x=0;
        len = fName.length();

        if (len >= 2) {
            for (i = 0; i < len; i++) {
                c = fName.charAt(i);
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && c != ' ') {
                    x++;
                }
                else;
            }
            if(x == len) return true;
            else return false;
        }
        else return false;
    }

    private boolean validateLastName(String lName) {
        char d;
        int length, x, y =0;
        length = lName.length();

        if (length >= 2) {
            for (x = 0; x < length; x++) {
                d = lName.charAt(x);
                if (((d >= 'a' && d <= 'z') || (d >= 'A' && d <= 'Z')) && d != ' ') {
                    y++;
                } else;
            }
            if(y==length) return true;
            else return false;
        }
        else return false;
    }

    private boolean validateUsername(String username) {
        char d;
        int length, x, y = 0;
        length = username.length();

        if (length >=6) {
            for (x = 0; x < length; x++) {
                d = username.charAt(x);
                if (((d >= 'a' && d <= 'z') || (d >= 'A' && d <= 'Z') || Character.isDigit(d)) && d != ' ') {
                    y++;
                } else;
            }
            if(y==length && checkUsernameDB(username) == true) return true;
            else return false;
        }
        else return false;
    }

    // Checks if username has already been taken and returns true otherwise
    private boolean checkUsernameDB(String usernameDB) {
        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersDataB.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            if (usernames.contains(usernameDB)) return false;
            else return true;
        }
        else return true;
    }

    private boolean validatePassword(String password) {

        char c;
        int b;
        int l = 0;
        int u = 0;
        int d = 0;
        int t = 0;
        int len = password.length();

        if (len >= 8) {
            for (b = 0; b < len; b++) {
                c = password.charAt(b);
                if (Character.isLowerCase(c)) l++;
                if (Character.isUpperCase(c)) u++;
                if (Character.isDigit(c)) d++;
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c)) && c != ' ') {
                    t++;
                } else;
            }

            if(t == len && (l+u+d)==len && l>=1 && u>=1 && d>=1) return true;
            else return false;
        }
        else return false;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


/*
    private void setListEvent() {

        List<User> theUserList = new LinkedList<User>();
        Cursor cur = myUsersDataB.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                long id = cur.getLong(cur.getColumnIndex(UsersDBManager.USER_KEY_ROWID));
                String firstName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_FIRST_NAME));
                String lastName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_LAST_NAME));
                String usrName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                String pass = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));
                User theNewUser = new User(id, firstName, lastName, usrName, pass);
                theUserList.add(theNewUser);
            }
        }
    }

    private void saveToInternalFile(String fileName) {

        Cursor curs = myUsersDataB.getAllUsers();
        if (curs.getCount() != 0) {
            try {
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_APPEND);
                String theWriteString = "ID   FirstName   Last Name   Username  Password";
                fos.write(theWriteString.getBytes());

                while (curs.moveToNext()) {
                    long id = curs.getLong(curs.getColumnIndex(UsersDBManager.USER_KEY_ROWID));
                    String firstName = curs.getString(curs.getColumnIndex(UsersDBManager.USER_KEY_FIRST_NAME));
                    String lastName = curs.getString(curs.getColumnIndex(UsersDBManager.USER_KEY_LAST_NAME));
                    String usrName = curs.getString(curs.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                    String pass = curs.getString(curs.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));

                    theWriteString = "" + id + "." + firstName + "." + lastName + "." + usrName + "." + pass + "\n";
                    fos.write(theWriteString.getBytes());
                }
                fos.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }
    }
*/

}
