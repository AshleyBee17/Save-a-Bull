package edu.usf.cse.labrador.save_a_bull;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_DB_TABLE;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_ROWID;
import static edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager.USER_KEY_USERNAME;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ForgetAndChangePasswordActivity extends AppCompatActivity {

    private EditText edtMode;
    private TextView txtMode;
    private Button submit;
    private FirebaseAuth auth;
    private ProgressDialog PD;
    private TextInputLayout labelMode;
    private UsersDBManager myUsersDataB;


    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_and_change_password);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        //Creates and Open Database
        myUsersDataB = new UsersDBManager(this);
        myUsersDataB.open();

        edtMode = (EditText) findViewById(R.id.mode);
        txtMode = (TextView) findViewById(R.id.title);
        submit = (Button) findViewById(R.id.submit_button);
        labelMode = (TextInputLayout) findViewById(R.id.label);


        final int mode = getIntent().getIntExtra("Mode", 0);

        if (mode == 0) {
            txtMode.setText("Forgot Password");
            edtMode.setHint("Enter Registered Email");
            labelMode.setHint("Enter Registered Email");
        } else if (mode == 1) {
            txtMode.setText("Change Password");
            edtMode.setHint("Enter New Password");
            labelMode.setHint("Enter New Password");
        } else if (mode == 2) {
            txtMode.setText("Change Email");
            edtMode.setHint("Enter New Email");
            labelMode.setHint("Enter New Email");
        } else {
            txtMode.setText("Delete User");
            edtMode.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                callFunction(mode);
            }
        });

    }

    private void callFunction(int mode) {
        FirebaseUser user = auth.getCurrentUser();
        String modeStr = edtMode.getText().toString();

        if (mode == 0) {
            if (TextUtils.isEmpty(modeStr)) {
                edtMode.setError("Value Required");
            } else {
                PD.show();
                auth.sendPasswordResetEmail(modeStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetAndChangePasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetAndChangePasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        PD.dismiss();
                    }
                });
            }


        } else if (mode == 1) {
            if (TextUtils.isEmpty(modeStr) || !validatePassword(modeStr)) {
                edtMode.setError("Invalid Password. Password must be at least 8 characters long, must contain at least one lowercase, one upper case and one digit");
            } else {
                PD.show();
                user.updatePassword(modeStr)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = auth.getCurrentUser();
                                    String currUsername = user.getEmail();
                                    Cursor cur = myUsersDataB.getUser(currUsername);
                                    String username = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                                    String password = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));
                                    String firstname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_FIRST_NAME));
                                    String lastname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_LAST_NAME));
                                    Long id = cur.getLong(cur.getColumnIndex(UsersDBManager.USER_KEY_ROWID));
                                    User updatedUser = new User(id, firstname, lastname, username, password);
                                    myUsersDataB.updateUser(updatedUser);
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }

                        });
            }
        } else if (mode == 2) {
            if (TextUtils.isEmpty(modeStr)|| !(checkUsernameDB(modeStr))) {
                edtMode.setError("Empty Value or Account Already taken");
            } else {
                PD.show();
                user.updateEmail(modeStr)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Email address is updated.", Toast.LENGTH_LONG).show();
                                    String modeStr = edtMode.getText().toString();
                                    FirebaseUser user = auth.getCurrentUser();
                                    String currUsername = user.getEmail();
                                    Cursor cur = myUsersDataB.getUser(currUsername);
                                    String username = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_USERNAME));
                                    String password = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_PASSWORD));
                                    String firstname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_FIRST_NAME));
                                    String lastname = cur.getString(cur.getColumnIndex(UsersDBManager.USER_KEY_LAST_NAME));
                                    Long id = cur.getLong(cur.getColumnIndex(UsersDBManager.USER_KEY_ROWID));
                                    User updatedUser = new User(id, firstname, lastname, username, password);
                                    myUsersDataB.updateUser(updatedUser);
                                    } else {
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                }
                                PD.dismiss();
                            }
                        });
            }
        } else {
            if (user != null) {
                PD.show();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = auth.getCurrentUser();
                                    String currUsername = user.getEmail();
                                    myUsersDataB.deleteUser(currUsername);
                                } else {
                                    Toast.makeText(ForgetAndChangePasswordActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }
                        });
            }
        }

    }

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
