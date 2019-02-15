package edu.usf.cse.labrador.save_a_bull;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import java.util.Arrays;

public class WelcomeScreen extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        callbackManager = CallbackManager.Factory.create();

        loginClicked();
        signUpClicked();
        loginWithFacebookClicked();

    }

    private void loginClicked() {
        Button signUpBtn = this.findViewById(R.id.login_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });
    }

    private void signUpClicked() {

        Button signUpBtn = this.findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeScreen.this, SignUpScreen.class);
                startActivity(intent);
            }
        });
    }

    private void loginWithFacebookClicked() {

        //callbackManager = CallbackManager.Factory.create();
        LoginButton facebookBtn = findViewById(R.id.facebook_login_button);

        // Accessing the email of the user and placing it in an ArrayList
        facebookBtn.setReadPermissions(Arrays.asList(EMAIL));

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            // If the login is correct, the app will continue to the next activity
            // for the home page
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken(); // unique token that gives access to user's data
                GraphRequestAsyncTask graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        LoginManager.getInstance().logOut();
                        String firstName = user.optString("first_name");
                    }
                }).executeAsync();

                Toast.makeText(getApplicationContext(), "You are now logged in with Facebook", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WelcomeScreen.this, MainScreen.class));
                /*Bundle bundle = new Bundle();
                bundle.putString("fields", "name, email, id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();*/
            }

            // When they log out
            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_SHORT).show();
            }

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
