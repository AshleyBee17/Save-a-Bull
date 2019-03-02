package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hab_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the tables
        db.execSQL(Coupon.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it already exists and create them again
        db.execSQL("DROP TABLE IF EXISTS " + Coupon.TABLE_NAME);
        onCreate(db);
    }

    public long insertCoupon(String coupName, String coupDesc, String coupCategory, int coupImg,
                            String coupPhone, String coupLong, String coupLat ){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        // Inserting tuple values
        values.put(Coupon.COLUMN_COMPANY_NAME, coupName);
        values.put(Coupon.COLUMN_DESCRIPTION, coupDesc);
        values.put(Coupon.COLUMN_CATEGORY, coupCategory);
        values.put(Coupon.COLUMN_IMAGE, coupImg);
        values.put(Coupon.COLUMN_PHONE, coupPhone);
        values.put(Coupon.COLUMN_LONGITUDE, coupLong);
        values.put(Coupon.COLUMN_LATITUDE, coupLat);

        long id = db.insert(Coupon.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public long insertMinCoupon(String coupName, String coupDesc, byte[] coupImg, String coupCat){


        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        // Inserting tuple values
        values.put(Coupon.COLUMN_COMPANY_NAME, coupName);
        values.put(Coupon.COLUMN_DESCRIPTION, coupDesc);
        values.put(Coupon.COLUMN_IMAGE, coupImg);
        values.put(Coupon.COLUMN_CATEGORY, coupCat);

        long id = db.insert(Coupon.TABLE_NAME, null, values);
        db.close();



        return id;
    }

    public Coupon getCoupon(long id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(Coupon.TABLE_NAME,
                new String[]{Coupon.COLUMN_ID, Coupon.COLUMN_COMPANY_NAME, Coupon.COLUMN_DESCRIPTION,
                Coupon.COLUMN_CATEGORY, Coupon.COLUMN_IMAGE, Coupon.COLUMN_PHONE, Coupon.COLUMN_LONGITUDE,
                Coupon.COLUMN_LATITUDE}, Coupon.COLUMN_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if(c != null) c.moveToFirst();

//        Coupon coupon = new Coupon(
//                c.getInt(c.getColumnIndex(Coupon.COLUMN_ID)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_COMPANY_NAME)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_DESCRIPTION)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_CATEGORY),
//                c.getInt(c.getColumnIndex(Coupon.COLUMN_IMAGE)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_PHONE)),
//                c.getDouble(c.getColumnIndex(Coupon.COLUMN_LONGITUDE)),
//                c.getDouble(c.getColumnIndex(Coupon.COLUMN_LATITUDE))
//        );

        assert c != null;
        Coupon coupon = new Coupon(
                c.getString(c.getColumnIndex(Coupon.COLUMN_COMPANY_NAME)),
                c.getString(c.getColumnIndex(Coupon.COLUMN_DESCRIPTION)),
                c.getString(c.getColumnIndex(Coupon.COLUMN_CATEGORY)),
                c.getType(c.getColumnIndex(Coupon.COLUMN_IMAGE))
        );

        c.close();

        return coupon;
    }

    public List<Coupon> getAllCoupons(){

        List<Coupon> coupons = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Coupon.TABLE_NAME + " ORDER BY " +
                Coupon.COLUMN_COMPANY_NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                Coupon coup = new Coupon();
                coup.setCompanyName(c.getString(c.getColumnIndex(Coupon.COLUMN_COMPANY_NAME)));
                coup.setDescription(c.getString(c.getColumnIndex(Coupon.COLUMN_DESCRIPTION)));
                coup.setCategory(c.getString(c.getColumnIndex(Coupon.COLUMN_CATEGORY)));
                //coup.setImg(c.getInt(c.getColumnIndex(Coupon.COLUMN_IMAGE)));
                //coup.setImg(c.getBlob(c.getColumnIndex(Coupon.COLUMN_IMAGE)));
                byte[] imgByte = c.getBlob(c.getColumnIndex(Coupon.COLUMN_IMAGE));
                coup.setImg(imgByte);
                coupons.add(coup);

            } while (c.moveToNext());
        }

        return coupons;
    }

    public int getCouponCount() {
        String countQuery = "SELECT  * FROM " + Coupon.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }

    // Possible additions to delete or update a coupon??

}
