package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.ForgetAndChangePasswordActivity;
import edu.usf.cse.labrador.save_a_bull.MainScreen;
import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.SignUpScreen;
import edu.usf.cse.labrador.save_a_bull.WelcomeScreen;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_FAVORITES;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_FIRST_NAME;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_LAST_NAME;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_PASSWORD;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_ROWID;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_USERNAME;


public class AboutFragment extends Fragment {

    private Dialog settingsDialog;
    private Dialog instructionsDialog;
    private Context mContext;
    private FirebaseAuth auth;
    private ProgressDialog PD;
    private UsersDBManager myUsersDataB;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);
        mContext = view.getContext();

        final TextView instructionsText = view.findViewById(R.id.about_text_view);
        instructionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionsDialog = new Dialog(mContext);
                instructionsDialog.setContentView(R.layout.dialog_app_instructions);
                instructionsDialog.show();
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

        Button editAccountButton = view.findViewById(R.id.home_settings_btn);
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Opening Settings", Toast.LENGTH_SHORT).show();
                settingsDialog = new Dialog(mContext);
                settingsDialog.setContentView(R.layout.dialog_account_settings);
                settingsDialog.show();

                PD = new ProgressDialog(mContext);
                PD.setMessage("Loading...");
                PD.setCancelable(true);
                PD.setCanceledOnTouchOutside(false);

                auth = FirebaseAuth.getInstance();


                //Creates and Open Database
                myUsersDataB = new UsersDBManager(mContext);
                myUsersDataB.open();


                Button saveChangeBtn = settingsDialog.findViewById(R.id.save_acct_changes_btn);
                saveChangeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Save Changes", Toast.LENGTH_SHORT).show();

                        EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
                        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);

                        String userUsername = loggedInUsername.getText().toString();
                        String userPassword = loggedInPassword.getText().toString();


                        if(userUsername.length() > 0){
                            updateEmail(userUsername);
                        }
                        if(userPassword.length()>0){
                            updatePassword(userPassword);
                        }

                        if (userUsername.length() == 0 && userPassword.length()==0)
                            Toast.makeText(mContext, "No changes to be saved!", Toast.LENGTH_SHORT).show();


                    }
                });

                Button deleteAcctBtn = settingsDialog.findViewById(R.id.delete_acct_btn);
                deleteAcctBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = auth.getCurrentUser();
                        String email = user.getEmail();
                        myUsersDataB.deleteUser(email);

                        Toast.makeText(mContext, "Delete Account", Toast.LENGTH_SHORT).show();
                        PD.show();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
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

                Button logoutBtn = settingsDialog.findViewById(R.id.logout_btn);
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        auth.signOut();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(mContext, WelcomeScreen.class));
                            //finish();
                        }

                    }
                });
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
        Email.setType("application/octet-stream");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "saveabull.contact@gmail.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, "User Feedback - " + feedbackDate);
        Email.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(Email, "Send Feedback:"));
    }

    // Updates the current user's email to the email entered on internal and Firebase DBs/
    private void updateEmail(String username){
        FirebaseUser userCurrent = auth.getCurrentUser();
        final String email = userCurrent.getEmail();

        try {
            if ( checkUsernameDB(username)){
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
                                } else {
                                    //Saving user to Database
                                    System.out.println("1");
                                    Cursor curs = myUsersDataB.getUser(email);
                                    System.out.println("2");
                                    String password = curs.getString(curs.getColumnIndex(USER_KEY_PASSWORD));
                                    String firstname = curs.getString(curs.getColumnIndex(USER_KEY_FIRST_NAME));
                                    String lastname = curs.getString(curs.getColumnIndex(USER_KEY_LAST_NAME));
                                    Long id = curs.getLong(curs.getColumnIndex(USER_KEY_ROWID));
                                    String favorites = curs.getString(curs.getColumnIndex(USER_KEY_FAVORITES));

                                    EditText loggedInUsername = settingsDialog.findViewById(R.id.usernamename_dialog_txt);
                                    String userUsername = loggedInUsername.getText().toString();
                                    System.out.println("3");
                                    User updatedUsername = new User(id, firstname, lastname, userUsername, password, favorites);
                                    myUsersDataB.updateUser(updatedUsername);
                                    System.out.println("4");
                                    Toast.makeText(
                                            mContext,
                                            "Email has been updated",
                                            Toast.LENGTH_LONG).show();
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

    // Updates the current user's password to the password entered on internal and Firebase DBs/
    private void updatePassword(String password){
        EditText loggedInPassword = settingsDialog.findViewById(R.id.password_dialog_text);
        FirebaseUser user = auth.getCurrentUser();
        if (!validatePassword(password)) {
            loggedInPassword.setError("Invalid Password. Password must be at least 8 characters long, must contain at least one lowercase, one upper case and one digit");
        } else {
            PD.show();
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = auth.getCurrentUser();
                                String currUsername = user.getEmail();
                                Cursor cur = myUsersDataB.getUser(currUsername);
                                String username = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                                String password = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));
                                String firstname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_FIRST_NAME));
                                String lastname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_LAST_NAME));
                                Long id = cur.getLong(cur.getColumnIndex(UsersDBManager.USER_KEY_ROWID));
                                String favorites = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_FAVORITES));
                                User updatedUser = new User(id, firstname, lastname, username, password, favorites);
                                myUsersDataB.updateUser(updatedUser);
                                Toast.makeText(mContext, "Password is updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Failed to update password!", Toast.LENGTH_SHORT).show();
                            }
                            PD.dismiss();
                        }

                    });
        }
    }

    // Checks if the entered email is in the internal DB. If it is, it returns false. If it is not, it returns true.
    private boolean checkUsernameDB(String usernameDB) {

        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersDataB.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            if (usernames.contains(usernameDB)) return false;
            else return true;
        }
        else return true;
    }

    // Checks if the entered password meets the minimum requirements
    // Password must be at least 8 characters long. Must have at least one upper case, one lower case and one digit.
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


}