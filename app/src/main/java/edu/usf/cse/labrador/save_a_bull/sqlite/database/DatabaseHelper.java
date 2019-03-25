package edu.usf.cse.labrador.save_a_bull.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;


public class DatabaseHelper
{
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef;
    private DatabaseReference couponRef;
    private List<Coupon> Gallery = new LinkedList<>();



    protected void onCreate()
    {
        //Prefetch all existing items for the node using a SingleValueEventListener
        FirebaseDatabase.getInstance().getReference("coupon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {

                    aSnapshotIterable.getValue(Coupon.class);
                    Gallery.add(aSnapshotIterable.getValue(Coupon.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Once you fetch all existing items, add a listener on the last item for realtime updates
        FirebaseDatabase.getInstance().getReference("coupon").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> snapshotIterator = snapshotIterable.iterator();
                //fetch last added item
                if(snapshotIterator.hasNext())
                {
                    Coupon value = snapshotIterator.next().getValue(Coupon.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}


//package edu.usf.cse.labrador.save_a_bull.sqlite.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
//
//import static edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon.COLUMN_EXPIRY;
//import static edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon.TABLE_NAME;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "hab_db";
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Creating the tables
//        //db.execSQL(Coupon.CREATE_TABLE);
//        db.execSQL("DROP TABLE " + TABLE_NAME);
//    }
//
//    private static final String DATABASE_ALTER_COUPON_1 = "ALTER TABLE "
//            + TABLE_NAME + " ADD COLUMN " + COLUMN_EXPIRY + " TEXT;";
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop the table if it already exists and create them again
//        if (oldVersion < newVersion) {
//            db.execSQL(DATABASE_ALTER_COUPON_1);
//        }
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//    }
//
//    public long insertCoupon(String coupName, String coupDesc, String coupCategory, int coupImg,
//                             String coupPhone, String coupLong, String coupLat, String coupExpiry) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        ContentValues values = new ContentValues();
//
//        // Inserting tuple values
//        values.put(Coupon.COLUMN_COMPANY_NAME, coupName);
//        values.put(Coupon.COLUMN_DESCRIPTION, coupDesc);
//        values.put(Coupon.COLUMN_CATEGORY, coupCategory);
//        values.put(Coupon.COLUMN_IMAGE, coupImg);
//        values.put(Coupon.COLUMN_PHONE, coupPhone);
//        values.put(Coupon.COLUMN_LONGITUDE, coupLong);
//        values.put(Coupon.COLUMN_LATITUDE, coupLat);
//        values.put(Coupon.COLUMN_EXPIRY, coupExpiry);
//
//        long id = db.insert(TABLE_NAME, null, values);
//        db.close();
//
//        return id;
//    }
//
//    public long insertMinCoupon(String coupName, String coupDesc, byte[] coupImg, String coupCat, String coupExpiry) {
//
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(Coupon.COLUMN_COMPANY_NAME, coupName);
//        values.put(Coupon.COLUMN_DESCRIPTION, coupDesc);
//        values.put(Coupon.COLUMN_IMAGE, coupImg);
//        values.put(Coupon.COLUMN_CATEGORY, coupCat);
//        values.put(Coupon.COLUMN_EXPIRY, coupExpiry);
//
//        long id = db.insert(TABLE_NAME, null, values);
//        db.close();
//
//        return id;
//    }
//
//
//    public Coupon getCoupon(long id) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor c = db.query(TABLE_NAME,
//                new String[]{Coupon.COLUMN_ID, Coupon.COLUMN_COMPANY_NAME, Coupon.COLUMN_DESCRIPTION,
//                        Coupon.COLUMN_CATEGORY, Coupon.COLUMN_IMAGE, Coupon.COLUMN_PHONE, Coupon.COLUMN_LONGITUDE,
//                        Coupon.COLUMN_LATITUDE /*,Coupon.COLUMN_EXPIRY*/}, Coupon.COLUMN_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if (c != null) c.moveToFirst();
//
//        assert c != null;
//        Coupon coupon = new Coupon(
//                c.getString(c.getColumnIndex(Coupon.COLUMN_COMPANY_NAME)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_DESCRIPTION)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_CATEGORY)),
//                c.getType(c.getColumnIndex(Coupon.COLUMN_IMAGE)),
//                c.getString(c.getColumnIndex(Coupon.COLUMN_EXPIRY))
//        );
//
//        c.close();
//
//        return coupon;
//    }
//
//    public List<Coupon> getAllCoupons() {
//
//        List<Coupon> coupons = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
//                Coupon.COLUMN_COMPANY_NAME + " DESC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c.moveToFirst()) {
//            do {
//                Coupon coup = new Coupon();
//                coup.setCompanyName(c.getString(c.getColumnIndex(Coupon.COLUMN_COMPANY_NAME)));
//                coup.setDescription(c.getString(c.getColumnIndex(Coupon.COLUMN_DESCRIPTION)));
//                coup.setCategory(c.getString(c.getColumnIndex(Coupon.COLUMN_CATEGORY)));
//                //coup.setExpiry(c.getString(c.getColumnIndex(Coupon.COLUMN_EXPIRY)));
//                byte[] imgByte = c.getBlob(c.getColumnIndex(Coupon.COLUMN_IMAGE));
//                coup.setImg(imgByte);
//                coupons.add(coup);
//
//            } while (c.moveToNext());
//        }
//
//        return coupons;
//    }
//
//    public int getCouponCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(countQuery, null);
//
//        int count = c.getCount();
//        c.close();
//
//        return count;
//    }
//
//    public void deleteAllCoupons() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + TABLE_NAME);
//        db.close();
//    }
//
//    public void deleteExpiredCoupon(Coupon expiredCoupon){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Coupon.TABLE_NAME, Coupon.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(expiredCoupon.getId())});
//        db.close();
//    }
//}