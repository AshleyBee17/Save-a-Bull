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
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;

public class LoginScreen extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin, forgetPassword;
    private ProgressDialog PD;


    private UsersDBManager myUsersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Creates and Open Database
        myUsersData = new UsersDBManager(this);
        myUsersData.open();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();


        btnLogin = (Button) findViewById(R.id.checkEntriesLoginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                inputEmail = (TextInputLayout) findViewById(R.id.validEmail);
                inputPassword = (TextInputLayout) findViewById(R.id.validPassword);
                final String email = inputEmail.getEditText().getText().toString();
                final String password = inputPassword.getEditText().getText().toString();

                try {

                    if (validateEmail(email) && password.length()>0 && email.length()>0) {
                        PD.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            inputEmail.setError("Invalid Emal/Password Combination");
                                            Toast.makeText(
                                                    LoginScreen.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {

                                            inputEmail.setErrorEnabled(false);
                                            inputPassword.setErrorEnabled(false);

                                            inputEmail.setError(null);
                                            inputPassword.setError(null);

                                            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {

                        if (!validateEmail(email)) {
                            Toast.makeText(
                                    LoginScreen.this,
                                    "Account does not exist!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else inputEmail.setError(null);

                        if (password.length()==0) {
                            inputPassword.setError("Empty Field");
                        }
                        else inputPassword.setError(null);

                        if (email.length()==0) {
                            inputEmail.setError("Empty Field");
                        }
                        else inputPassword.setError(null);

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


    private boolean validateEmail(String email){

        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersData.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            return usernames.contains(email);
        }
        return false;
    }



}