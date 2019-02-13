package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class WelcomeScreen extends AppCompatActivity {

    private LoginButton facebookBtn;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

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

        callbackManager = CallbackManager.Factory.create();
        facebookBtn = findViewById(R.id.facebook_login_button);

        // Accessing the email of the user and placing it in an ArrayList
        facebookBtn.setReadPermissions(Arrays.asList(EMAIL));

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            // If the login is correct, the app will continue to the next activity
            // for the home page
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken(); // unique token that gives access to user's data
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        goHome(object);
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "name, email, id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            // When they log out,
            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void goHome(JSONObject jObj){
        TextView textView = findViewById(R.id.textView);
        textView.setText(jObj.toString());

        Intent intent = new Intent(this, HomeScreen.class);

        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
