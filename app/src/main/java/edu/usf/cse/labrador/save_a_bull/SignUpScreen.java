package edu.usf.cse.labrador.save_a_bull;

import android.content.Context;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;


public class SignUpScreen extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword, inputFirstName, inputLastName;
    private FirebaseAuth auth;
    private Button btnSignUp;
    private ProgressDialog PD;

    private UsersDBManager myUsersDataB;
    long current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        /* Allows an already created user to login without credentials
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpScreen.this, MainScreen.class));
            finish();
        }*/

        //Creates and Open Database
        myUsersDataB = new UsersDBManager(this);
        myUsersDataB.open();


        btnSignUp = (Button) findViewById(R.id.createAcctBtn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputEmail = (TextInputLayout) findViewById(R.id.validateEmail);
                inputPassword = (TextInputLayout) findViewById(R.id.validatePassword);
                inputFirstName = (TextInputLayout) findViewById(R.id.validateFirstName);
                inputLastName = (TextInputLayout) findViewById(R.id.validateLastName);

                final String email = inputEmail.getEditText().getText().toString();
                final String password = inputPassword.getEditText().getText().toString();
                final String firstName = inputFirstName.getEditText().getText().toString();
                final String lastName = inputLastName.getEditText().getText().toString();
                try {
                    if ( checkUsernameDB(email) && validateFirstName(firstName) && validateLastName(lastName) && validatePassword(password) && email.length()>0) {
                        PD.show();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            inputEmail.setError("Invalid Email");
                                            Toast.makeText(
                                                    SignUpScreen.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {

                                            inputFirstName.setErrorEnabled(false);
                                            inputLastName.setErrorEnabled(false);
                                            inputEmail.setErrorEnabled(false);
                                            inputPassword.setErrorEnabled(false);

                                            inputFirstName.setError(null);
                                            inputLastName.setError(null);
                                            inputEmail.setError(null);
                                            inputPassword.setError(null);

                                            //Saving user to Database
                                            current_user_id = myUsersDataB.createUser(firstName, lastName, email);

                                            Intent intent = new Intent(SignUpScreen.this, WelcomeScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    }
                    else {

                        if (!validateFirstName(firstName)) {
                            inputFirstName.setErrorEnabled(true);
                            inputFirstName.setError("Invalid First Name. Enter letters only.");
                        } else if (validateFirstName(firstName)) inputFirstName.setError(null);

                        if (!validateLastName(lastName)) {
                            inputLastName.setErrorEnabled(true);
                            inputLastName.setError("Invalid Last Name. Enter letters only.");
                        } else if (validateLastName(lastName)) inputLastName.setError(null);

                        if (email.length() == 0) {
                            inputEmail.setErrorEnabled(true);
                            inputEmail.setError("Empty Field");
                        } else if (email.length() > 0) inputEmail.setError(null);

                        if (!checkUsernameDB(email)) {
                            Toast.makeText(
                                    SignUpScreen.this,
                                    "Account already exists!",
                                    Toast.LENGTH_LONG).show();
                        } else if (email.length() > 0) inputEmail.setError(null);

                        if (!validatePassword(password)) {
                            inputPassword.setErrorEnabled(true);
                            inputPassword.setError("Invalid Password. Password must be 8 characters or more and has to include at least one capital letter, one lower case letter and one digit.");
                        } else if (validatePassword(password)) inputPassword.setError(null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        myUsersDataB.close();
    }


    // Checks if all user entries are valid and if so, the user is saved to the DB and redirected to the Welcome screen
    private void checkEntries(String fName, String lName, String email, String password) {
        /*
         * Check that the user's entries are valid i.e. no numbers in first/last name
         * if everything is good, save the info to the database and send the back to the welcome
         * screen to log in
         * if an entry is not correct, display an error until it is fixed
         * */

        if(validateFirstName(fName) && validateLastName(lName) && validateEmail(email) && validatePassword(password)){

            inputFirstName.setErrorEnabled(false);
            inputLastName.setErrorEnabled(false);
            inputEmail.setErrorEnabled(false);
            inputPassword.setErrorEnabled(false);

            inputFirstName.setError(null);
            inputLastName.setError(null);
            inputEmail.setError(null);
            inputPassword.setError(null);

            //Saving user to Database
            current_user_id = myUsersDataB.createUser(fName, lName, email);

            //Taking the user back to Welcome screen.
            Intent intent = new Intent(SignUpScreen.this, WelcomeScreen.class);
            startActivity(intent);
        }

        else {

            if (!validateFirstName(fName)) {
                inputFirstName.setErrorEnabled(true);
                inputFirstName.setError("Invalid First Name. Enter letters only.");
            } else if (validateFirstName(fName)) inputFirstName.setError(null);

            if (!validateLastName(lName)) {
                inputLastName.setErrorEnabled(true);
                inputLastName.setError("Invalid Last Name. Enter letters only.");
            } else if (validateLastName(lName)) inputLastName.setError(null);

            if (!validateEmail(email)) {
                inputEmail.setErrorEnabled(true);
                inputEmail.setError("Invalid or taken Username. Enter letters or digits only.");
            } else if (validateEmail(email)) inputEmail.setError(null);

            if (!validatePassword(password)) {
                inputPassword.setErrorEnabled(true);
                inputPassword.setError("Invalid Password. Password must be 8 characters or more and has to include at least one capital letter, one lower case letter and one digit.");
            } else if (validatePassword(password)) inputPassword.setError(null);

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

    private boolean validateEmail(String email) {
        char d;
        int length, x, y = 0;
        length = email.length();

        if (length >=6) {
            for (x = 0; x < length; x++) {
                d = email.charAt(x);
                if (((d >= 'a' && d <= 'z') || (d >= 'A' && d <= 'Z') || Character.isDigit(d)) && d != ' ') {
                    y++;
                } else;
            }
            if(y==length && checkUsernameDB(email) == true) return true;
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
}