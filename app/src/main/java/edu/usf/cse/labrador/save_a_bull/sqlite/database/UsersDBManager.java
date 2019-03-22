package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UsersDBManager {

    //DB fields for user table
    public static final String USER_DB_TABLE = "users";
    public static final String USER_KEY_ROWID = "_id";
    public static final String USER_KEY_FIRST_NAME = "fName";
    public static final String USER_KEY_LAST_NAME = "lName";
    public static final String USER_KEY_USERNAME = "username";

    private Context context;
    public SQLiteDatabase database;
    private UsersDataB userDB;

    public UsersDBManager(Context context){
        this.context = context;
    }

    public void open() throws SQLException {
        userDB = new UsersDataB(context);
        database = userDB.getWritableDatabase();
    }

    public void close(){
        userDB.close();
    }

    private ContentValues createUsersValues(String fName, String lName, String username){
        ContentValues values = new ContentValues();
        values.put(USER_KEY_FIRST_NAME, fName);
        values.put(USER_KEY_LAST_NAME, lName);
        values.put(USER_KEY_USERNAME, username);
        return values;
    }

    private ContentValues createUsersValue(String username){
        ContentValues value = new ContentValues();
        value.put(USER_KEY_USERNAME, username);
        return value;
    }

    //Insert Method for database

    public long createUser(String fName, String lName, String username){
        ContentValues initialValues = createUsersValues(fName, lName, username);
        return database.insert(USER_DB_TABLE, null, initialValues);
    }

    //Update Method for database

    public boolean updateUser(long rowID, String fName, String lName, String username){
        ContentValues updateValues = createUsersValues(fName, lName, username);
        return database.update(USER_DB_TABLE, updateValues, USER_KEY_ROWID + "=" + rowID, null) >0;
    }
    public boolean updateUsername(String oldUsername, String newUsername){
        ContentValues updateValue =  createUsersValue(newUsername);
        return database.update(USER_DB_TABLE,  updateValue,USER_KEY_USERNAME + "=" + oldUsername, null)> 0;

    }
    //Delete User Method for Database

    public boolean deleteUser(String username){
        return database.delete(USER_DB_TABLE, USER_KEY_USERNAME + "=" + username, null) > 0;
    }

    //Query Methods for Database

    public Cursor getAllUsers(){
        return database.query(USER_DB_TABLE, new String [] {USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME}, null, null, null, null, null);

    }


    public Cursor getUser(String username) throws SQLException{
        Cursor mCursor = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME}, USER_KEY_USERNAME + "=" + username, null, null, null, null, null);

        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}