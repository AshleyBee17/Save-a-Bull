package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class UsersDBManager {

    //DB fields for user table
    public static final String USER_DB_TABLE = "users";
    public static final String USER_KEY_ROWID = "_id";
    public static final String USER_KEY_FIRST_NAME = "fName";
    public static final String USER_KEY_LAST_NAME = "lName";
    public static final String USER_KEY_USERNAME = "username";
    public static final String USER_KEY_PASSWORD = "password";

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
    public void updateUser(User userUpdated){
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_KEY_ROWID, userUpdated.getUserID());
        contentValues.put(USER_KEY_FIRST_NAME, userUpdated.getfName());
        contentValues.put(USER_KEY_LAST_NAME, userUpdated.getlName());
        contentValues.put(USER_KEY_USERNAME, userUpdated.getUsername());
        contentValues.put(USER_KEY_PASSWORD, userUpdated.getPassword());
        //String whereClause = USER_KEY_ROWID + "=" + userUpdated.getUserID();
        //String whereArgs[] = {Long.toString(userUpdated.getUserID())};
        database.update(USER_DB_TABLE, contentValues, USER_KEY_ROWID + "='" + userUpdated.getUserID() + "'", null);
    }

    //Delete User Method for Database
    public void deleteUser(String email){
        database.delete(USER_DB_TABLE, USER_KEY_USERNAME + "='" + email + "'", null);
    }

    //Query Methods for Database
    /*public List<User> getAllUsers(){
        List<User> usersList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + USER_DB_TABLE
                + " ORDER BY " + USER_KEY_ROWID + " DESC";
           Cursor cursor = database.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                User newUser = new User();
                Integer userId = cursor.getInt(cursor.getColumnIndex(USER_KEY_ROWID));
                Long myLong = new Long(userId);
                newUser.setUserID(myLong);
                newUser.setfName(cursor.getString(cursor.getColumnIndex(USER_KEY_FIRST_NAME)));
                newUser.setlName(cursor.getString(cursor.getColumnIndex(USER_KEY_LAST_NAME)));
                newUser.setUsername(cursor.getString(cursor.getColumnIndex(USER_KEY_USERNAME)));
                newUser.setPassword(cursor.getString(cursor.getColumnIndex(USER_KEY_PASSWORD)));

                //Add movie details to list
                usersList.add(newUser);
            } while (cursor.moveToNext());
        }
        return usersList;

    }

*/

    public Cursor getAllUsers() {

        String selectQuery = "SELECT * FROM " + USER_DB_TABLE
                + " ORDER BY " + USER_KEY_ROWID + " DESC";
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getUser(String username) throws SQLException{

        Cursor mCursor = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD}, USER_KEY_USERNAME + "='" + username + "'", null, null, null, null, null);
        if (null != mCursor) {
            mCursor.moveToFirst();
        }
        return mCursor;

        /*User user = new User();
        Cursor mCursor = database.query(true, USER_DB_TABLE, new String []{USER_KEY_ROWID, USER_KEY_FIRST_NAME, USER_KEY_LAST_NAME, USER_KEY_USERNAME, USER_KEY_PASSWORD}, USER_KEY_USERNAME + "=" + username, null, null, null, null, null);

        if (null != mCursor) {
            mCursor.moveToFirst();
            user.setUserID(mCursor.getLong(mCursor.getColumnIndex(USER_KEY_ROWID)));
            user.setfName(mCursor.getString(mCursor.getColumnIndex(USER_KEY_FIRST_NAME)));
            user.setlName(mCursor.getString(mCursor.getColumnIndex(USER_KEY_LAST_NAME)));
            user.setUsername(username);
            user.setPassword(mCursor.getString(mCursor.getColumnIndex(USER_KEY_PASSWORD)));
        }
        return user;*/

    }
}
