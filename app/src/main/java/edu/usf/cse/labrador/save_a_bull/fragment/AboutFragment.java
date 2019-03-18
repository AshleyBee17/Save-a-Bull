package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.usf.cse.labrador.save_a_bull.R;

public class AboutFragment extends Fragment {

    private Dialog settingsDialog;
    private Dialog instructionDialog;
    private Context mContext;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);
        mContext = view.getContext();


        TextView textView = view.findViewById(R.id.about_text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Opening instructions", Toast.LENGTH_SHORT).show();
                instructionDialog = new Dialog(mContext);
                instructionDialog.setContentView(R.layout.dialog_app_instructions);
                instructionDialog.show();
            }
        });

        Button submitFeedbackBtn = view.findViewById(R.id.submit_feedback);
        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Sending Feedback", Toast.LENGTH_SHORT).show();
                sendFeedback();
            }
        });

        return view;
    }

    private void sendFeedback() {
        EditText feedbackText = view.findViewById(R.id.user_feedback);
        String message = feedbackText.getText().toString();

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = 1 + Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String feedbackDate = day + "/" + month + "/" + year;

        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "saveabull_contact@gmail.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, "User Feedback - " + feedbackDate);
        Email.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(Email, "Send Feedback:"));


    }

    private void displaySettingsDialog(){
        settingsDialog = new Dialog(mContext);
        settingsDialog.setContentView(R.layout.dialog_account_settings);

        EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);
        EditText loggedInEmail = settingsDialog.findViewById(R.id.email_dialog_text);

        settingsDialog.show();

        Button saveChangeBtn = settingsDialog.findViewById(R.id.save_acct_changes_btn);
        saveChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Save Changes", Toast.LENGTH_SHORT).show();
                saveChangesClicked();
            }
        });

        final Button deleteAcctBtn = settingsDialog.findViewById(R.id.delete_acct_btn);
        deleteAcctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Delete Account", Toast.LENGTH_SHORT).show();
                deleteAccountClicked();
            }
        });

        /* With the Account class... set the hint for each edit text to be the user's current username
        current password, and current email address

        loggedInUsername.setHint(LoggedInUser.getUsername());
        loggedInPassword.setHint();
        loggedInEmail.setHint();
        */
    }


    private void saveChangesClicked(){
        EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);
        EditText loggedInEmail = settingsDialog.findViewById(R.id.email_dialog_text);

        /*
        * Find the account that is logged in
        * Change the credentials to whatever is typed in the boxes
        * save the account
        * */

    }

    private void deleteAccountClicked(){
        /*
        * Search for the account that is currently logged in
        * and remove it from the database
        *
        * */
    }
}
