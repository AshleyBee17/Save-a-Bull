package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDataB extends SQLiteOpenHelper {

    public static final String DB_NAME = "users_db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_USER_TABLE =  "create table users(" +
            "_id integer primary key autoincrement, " +
            "fName text not null, " +
            "lName text not null, " +
            "username text not null)";

    public UsersDataB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS users");
        onCreate(database);
    }
}

