package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UsersDBManager {

    //DB fields for user table
    private static final String USER_DB_TABLE = "users";
    public static final String USER_KEY_ROWID = "_id";
    public static final String USER_KEY_FIRST_NAME = "fName";
    public static final String USER_KEY_LAST_NAME = "lName";
    public static final String USER_KEY_USERNAME = "username";
    public static final String USER_KEY_PASSWORD = "password";

    private Context context;
    private SQLiteDatabase database;
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

    private ContentValues createUsersValues(String fName, String lName, String username, String password){
        ContentValues values = new ContentValues();
        values.put(USER_KEY_FIRST_NAME, fName);
        values.put(USER_KEY_LAST_NAME, lName);
        values.put(USER_KEY_USERNAME, username);
        values.put(USER_KEY_PASSWORD, password);
        return values;
    }

    //Insert Method for database

    public long createUser(String fName, String lName, String username, String password){
        ContentValues initialValues = createUsersValues(fName, lName, username, password);
        return database.insert(USER_DB_TABLE, null, initialValues);
    }

    //Update Method for database

    public boolean updateUser(long rowID, String fName, String lName, String username, String password){
        ContentValues updateValues = createUsersValues(fName, lName, username, password);
        return database.update(USER_DB_TABLE, updateValues, USER_KEY_ROWID + "=" + rowID, null) >0;
    }

    //Delete User Method for Database

    public boolean deleteUser(long userId){
        return database.delete(USER_DB_TABLE, USER_KEY_ROWID + "=" + userId, null) > 0;
    }

    //Query Methods for Database

    public Cursor getAllUsers(){
        return database.query(USER_DB_TABLE, new String [] {USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD}, null, null, null, null, null);

    }

    public Cursor getUser(long rowId) throws SQLException{
        Cursor mCursor = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD}, USER_KEY_ROWID + "=" + rowId, null, null, null, null, null);

        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}

