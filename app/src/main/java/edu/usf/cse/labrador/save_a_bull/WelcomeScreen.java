package edu.usf.cse.labrador.save_a_bull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        loginClicked();
        signUpClicked();
        loginWithFacebookClicked();


    }

    private void loginClicked() {
    }

    private void signUpClicked() {
    }

    private void loginWithFacebookClicked() {

        Button facebookBtn = findViewById(R.id.facebook_btn);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
