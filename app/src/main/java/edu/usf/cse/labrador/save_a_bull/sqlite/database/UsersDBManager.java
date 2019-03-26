package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class UsersDBManager {

    //DB fields for user table
    public static final String USER_DB_TABLE = "users";
    public static final String USER_KEY_ROWID = "_id";
    public static final String USER_KEY_FIRST_NAME = "fName";
    public static final String USER_KEY_LAST_NAME = "lName";
    public static final String USER_KEY_USERNAME = "username";
    public static final String USER_KEY_PASSWORD = "password";
    public static final String USER_KEY_FAVORITES = "favorites";


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

    private ContentValues createUsersValues(String fName, String lName, String username, String password, String faves){
        ContentValues values = new ContentValues();
        values.put(USER_KEY_FIRST_NAME, fName);
        values.put(USER_KEY_LAST_NAME, lName);
        values.put(USER_KEY_USERNAME, username);
        values.put(USER_KEY_PASSWORD, password);
        values.put(USER_KEY_FAVORITES, "x");
        return values;
    }


    //Insert Method for database
    public long createUser(String fName, String lName, String username, String password, String faves){
        ContentValues initialValues = createUsersValues(fName, lName, username, password, faves);
        return database.insert(USER_DB_TABLE, null, initialValues);
    }

    //Update Method for database
    public void updateUser(User userUpdated){
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_KEY_ROWID, userUpdated.getUserID());
        contentValues.put(USER_KEY_FIRST_NAME, userUpdated.getfName());
        contentValues.put(USER_KEY_LAST_NAME, userUpdated.getlName());
        contentValues.put(USER_KEY_USERNAME, userUpdated.getUsername());
        contentValues.put(USER_KEY_PASSWORD, userUpdated.getPassword());
        contentValues.put(USER_KEY_FAVORITES, userUpdated.getLine());
        database.update(USER_DB_TABLE, contentValues, USER_KEY_ROWID + "='" + userUpdated.getUserID() + "'", null);
    }

    //Delete User Method for Database
    public void deleteUser(String email){
        database.delete(USER_DB_TABLE, USER_KEY_USERNAME + "='" + email + "'", null);
    }

    //Query Methods for Database

    public Cursor getAllUsers() {

        String selectQuery = "SELECT * FROM " + USER_DB_TABLE
                + " ORDER BY " + USER_KEY_ROWID + " DESC";
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getUser(String username) throws SQLException{

        Cursor mCursor = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD, USER_KEY_FAVORITES}, USER_KEY_USERNAME + "='" + username + "'", null, null, null, null, null);
        if (null != mCursor) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public User getUserReturnUserType(String username) throws SQLException{

        //Cursor c = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD, USER_KEY_FAVORITES}, USER_KEY_USERNAME + "='" + username + "'", null, null, null, null, null);

        String selectQuery = "SELECT * FROM " + USER_DB_TABLE
                + " WHERE " + USER_KEY_USERNAME;// + " DESC";
        Cursor c = database.rawQuery(selectQuery, null);


        if (c != null) c.moveToFirst();

        assert c != null;
        User user = new User();

        user.setUsername(username);
        user.setLine("00000,");

        return user;
    }



//    private static String strSeparator = "__,__";
//    public static String convertArrayToString(String[] array){
//        StringBuilder str = new StringBuilder();
//        for (int i = 0;i<array.length; i++) {
//            str.append(array[i]);
//            // Do not append comma at the end of last element
//            if(i<array.length-1){
//                str.append(strSeparator);
//            }
//        }
//        return str.toString();
//    }
//    public static String[] convertStringToArray(String str){
//        return str.split(strSeparator);
//    }
}