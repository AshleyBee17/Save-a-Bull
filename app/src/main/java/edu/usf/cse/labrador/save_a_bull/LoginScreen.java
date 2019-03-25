package edu.usf.cse.labrador.save_a_bull;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.support.design.widget.TextInputLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_FIRST_NAME;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_LAST_NAME;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_PASSWORD;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_ROWID;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_USERNAME;

public class LoginScreen extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin, forgetPassword;
    private UsersDBManager myUsersData;
    private ProgressDialog PD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        auth = FirebaseAuth.getInstance();


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        //Creates and Open Database
        myUsersData = new UsersDBManager(this);
        myUsersData.open();

        btnLogin = (Button) findViewById(R.id.checkEntriesLoginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                inputEmail = (TextInputLayout) findViewById(R.id.validEmail);
                inputPassword = (TextInputLayout) findViewById(R.id.validPassword);
                String email = inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();

                try {

                    if (password.length()>0 && email.length()>0) {
                        PD.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("error", task.getException());
                                            Toast.makeText(
                                                    LoginScreen.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();

                                        } else {

                                            inputEmail.setErrorEnabled(false);
                                            inputPassword.setErrorEnabled(false);

                                            inputEmail.setError(null);
                                            inputPassword.setError(null);

                                            FirebaseUser user = auth.getCurrentUser();
                                            String currUsername = user.getEmail();
                                            inputPassword = (TextInputLayout) findViewById(R.id.validPassword);
                                            String password = inputPassword.getEditText().getText().toString();
                                            validatePassword(password, currUsername);

                                            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {

                        if (password.length()==0) {
                            inputPassword.setError("Empty Field");
                        } else if (password.length()>0) inputPassword.setError(null);

                        if (email.length()==0) {
                            inputEmail.setError("Empty Field");
                        } else if (email.length()>0) inputEmail.setError(null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        forgetPassword = findViewById(R.id.forget_password_button);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0));
            }
        });
    }



  /*  @Override    protected void onResume() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginScreen.this, MainScreen.class));
            finish();
        }
        super.onResume();
    }

    */

    // Checks if password entered matches password stored for current user. Returns true if it matches and false otherwise.
    private void validatePassword(String password, String email) {

        Cursor cur = myUsersData.getUser(email);
        String newPassword = cur.getString(cur.getColumnIndex(USER_KEY_PASSWORD));
        String usrName = cur.getString(cur.getColumnIndex(USER_KEY_USERNAME));
        String fName = cur.getString(cur.getColumnIndex(USER_KEY_FIRST_NAME));
        String lName = cur.getString(cur.getColumnIndex(USER_KEY_LAST_NAME));
        Long id = cur.getLong(cur.getColumnIndex(USER_KEY_ROWID));

        if(newPassword.equals(password));
        else {
            User updatedUser = new User(id, fName, lName, usrName, password);
            myUsersData.updateUser(updatedUser);
        }

    }



}