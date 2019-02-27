package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        Button signUpBtn = this.findViewById(R.id.createAcctBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkEntries();

            }
        });
    }

    private void checkEntries(){
        /*
        * Check that the user's entries are valid i.e. no numbers in first/last name
        * if everything is good, save the info to the database and send the back to the welcome
        * screen to log in
        * if an entry is not correct, display an error until it is fixed
        * */
        Intent intent = new Intent(SignUpScreen.this, WelcomeScreen.class);
        startActivity(intent);
    }
}
