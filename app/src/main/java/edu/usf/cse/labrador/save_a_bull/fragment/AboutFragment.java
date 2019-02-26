package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.usf.cse.labrador.save_a_bull.R;

public class AboutFragment extends Fragment {

    private Dialog settingsDialog;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mContext = view.getContext();

        Button editAccountButton = view.findViewById(R.id.home_settings_btn);
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Opening Settings", Toast.LENGTH_SHORT).show();
                displaySettingsDialog();
            }
        });

        return view;
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
