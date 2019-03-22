package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.SignUpScreen;
import edu.usf.cse.labrador.save_a_bull.WelcomeScreen;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;

import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_USERNAME;

public class AboutFragment extends Fragment {

    private Dialog settingsDialog;
    private Context mContext;
    private FirebaseAuth auth;
    private ProgressDialog PD;
    private UsersDBManager myUsersDataB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mContext = view.getContext();

        PD = new ProgressDialog(mContext);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Creates and Open Database
        myUsersDataB = new UsersDBManager(mContext);
        myUsersDataB.open();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final String email = user.getEmail();


        Button editAccountButton = view.findViewById(R.id.home_settings_btn);
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Opening Settings", Toast.LENGTH_SHORT).show();
                settingsDialog = new Dialog(mContext);
                settingsDialog.setContentView(R.layout.dialog_account_settings);
                settingsDialog.show();

                final Button saveChangeBtn = settingsDialog.findViewById(R.id.save_acct_changes_btn);
                saveChangeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Save Changes", Toast.LENGTH_SHORT).show();

                        EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
                        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);

                        final String userUsername = loggedInUsername.getText().toString();
                        final String userPassword = loggedInPassword.getText().toString();

                        if(userUsername.length() > 0){
                            updateEmail(email, userUsername);
                        }
                        if(userPassword.length()>0){
                            updatePassword(userPassword);
                        }

                        //saveChangesClicked();
                    }
                });

                final Button deleteAcctBtn = settingsDialog.findViewById(R.id.delete_acct_btn);
                deleteAcctBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Delete Account", Toast.LENGTH_SHORT).show();
                        PD.show();
                        myUsersDataB.deleteUser(email);
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override                            public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContext, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                             startActivity(new Intent(mContext, WelcomeScreen.class));
                                        } else {
                                            Toast.makeText(mContext, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        return view;
    }


    private void updateEmail(String username, String oldUsername){
        FirebaseUser userCurrent = auth.getCurrentUser();
        final String oldUsr = userCurrent.getEmail();
        try {
            if ( checkUsernameDB(username) && username.length() > 0){
                PD.show();
                userCurrent.updateEmail(username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                            mContext,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                    Log.v("error", task.getResult().toString());
                                } else {
                                    //Saving user to Database
                                    String newUsr = auth.getCurrentUser().getEmail();
                                    myUsersDataB.updateUsername(oldUsr, newUsr);
                                    Toast.makeText(
                                            mContext,
                                            "Email has been updated",
                                            Toast.LENGTH_LONG).show();
                                    Log.v("error", task.getResult().toString());
                                }
                                PD.dismiss();
                            }
                        });
            }
            else {

                if (!checkUsernameDB(username)) {
                    Toast.makeText(
                            mContext,
                            "Account already exists!",
                            Toast.LENGTH_LONG).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword(String password){

        FirebaseUser userCurrent = auth.getCurrentUser();
        try {
            if (validatePassword(password) && password.length() > 0){
                PD.show();
                userCurrent.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("error", "User password updated.");
                        }
                        else {
                            Toast.makeText(
                                    mContext,
                                    "Authentication Failed",
                                    Toast.LENGTH_LONG).show();
                            Log.v("error", task.getResult().toString());
                        }
                        PD.dismiss();
                    }

                });
        }else {

            if (!validatePassword(password)) {
                Toast.makeText(
                        mContext,
                        "Invalid Password. Password needs to have at least 8 characters, one upper case, one, lower case and one digit. ",
                        Toast.LENGTH_LONG).show();
            }

        }

    }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUsernameDB(String usernameDB) {
        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersDataB.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            if (usernames.contains(usernameDB)) return false;
            else return true;
        }
        else return true;
    }

    private boolean validatePassword(String password) {

        char c;
        int b;
        int l = 0;
        int u = 0;
        int d = 0;
        int t = 0;
        int len = password.length();

        if (len >= 8) {
            for (b = 0; b < len; b++) {
                c = password.charAt(b);
                if (Character.isLowerCase(c)) l++;
                if (Character.isUpperCase(c)) u++;
                if (Character.isDigit(c)) d++;
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c)) && c != ' ') {
                    t++;
                } else;
            }

            if(t == len && (l+u+d)==len && l>=1 && u>=1 && d>=1) return true;
            else return false;
        }
        else return false;
    }

    /*
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
                PD.show();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mContext, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                    myUsersDataB.deleteUser(userRowID);
                                    startActivity(new Intent(mContext, WelcomeScreen.class));
                                } else {
                                    Toast.makeText(mContext, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }
                        });
            }
        });

    }


    private void saveChangesClicked(){
        EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);
        EditText loggedInEmail = settingsDialog.findViewById(R.id.email_dialog_text);

    }


     private void deleteAccountClicked(){
       PD.show();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override                            public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                            myUsersDataB.deleteUser(userRowID);
                            startActivity(new Intent(mContext, WelcomeScreen.class));
                        } else {
                            Toast.makeText(mContext, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                        }
                        PD.dismiss();
                    }
                });

    }
*/

}
