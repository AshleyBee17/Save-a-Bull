package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button signUpBtn = this.findViewById(R.id.checkEntriesLoginBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkEntries();

            }
        });
    }

    private void checkEntries(){
        /*
        * if the username and password are correct
        * take the user to the login page w/ their information
        * if one or both of the entries are incorrect
        */
        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
        startActivity(intent);
    }

}
