package edu.usf.cse.labrador.save_a_bull;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "account_information";
    private static final String COL1 = "first_name";
    private static final String COL2 = "last_name";
    private static final String COL3 = "username";
    private static final String COL4 = "password";
    private static final String COL5 = "account_type";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( AUTOINCREMENT " + COL1 +" "+ COL2
                + " " + COL3 + " " + COL4 + " " + COL5 +")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}

// Search this in the morning: 
