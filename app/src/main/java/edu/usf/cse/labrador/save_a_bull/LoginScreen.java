package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;

public class LoginScreen extends AppCompatActivity {

    private UsersDBManager myUsersData;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        /*Creates and Open Database
        myUsersData = new UsersDBManager(this);
        myUsersData.open();*/

        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button signUpBtn = this.findViewById(R.id.checkEntriesLoginBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEntries();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void checkEntries(){
        /*
         * if the username and password are correct
         * take the user to the login page w/ their information
         * if one or both of the entries are incorrect
         */
        TextInputLayout usernameMessage = (TextInputLayout) findViewById(R.id.validUsername);
        TextInputLayout passwordMessage = (TextInputLayout) findViewById(R.id.validPassword);

        String username = usernameMessage.getEditText().getText().toString();
        String password = passwordMessage.getEditText().getText().toString();

        usernameMessage.setHint("Username");
        passwordMessage.setHint("Password");

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else{
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        /*if (!validateUsername(username)) {
            usernameMessage.setError("Invalid Username");
        }
        else usernameMessage.setError(null);

        if (!validatePassword(password)) {
            passwordMessage.setError("Invalid Password");
        }
        else passwordMessage.setError(null);

        if(validateUsername(username) && validatePassword(password)) {
            usernameMessage.setErrorEnabled(false);
            passwordMessage.setErrorEnabled(false);

            usernameMessage.setError(null);
            passwordMessage.setError(null);


        }*/

    }

    private boolean validateUsername(String username){

        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersData.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            return usernames.contains(username);
        }
        return false;
    }

    private boolean validatePassword(String password){

        List<String> passwordList = new LinkedList<String>();
        Cursor cur = myUsersData.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String passwordNext = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));
                passwordList.add(passwordNext);
            }
            return passwordList.contains(password);
        }
        return false;

    }
    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
            startActivity(intent);
        }

        else{


        }
    }
}
