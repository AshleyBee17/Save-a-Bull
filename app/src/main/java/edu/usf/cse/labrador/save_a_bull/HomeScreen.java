package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;



public class HomeScreen extends AppCompatActivity {

    String receivedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Bundle b = getIntent().getExtras();

        // getting the string from the bundle via the key we gave it
        assert b != null;
        receivedMessage = b.getString("EXTRA_MESSAGE");

        // displaying the message
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(receivedMessage);

        // setting the activity layout to the text view
        setContentView(textView);

        // ** does something
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
