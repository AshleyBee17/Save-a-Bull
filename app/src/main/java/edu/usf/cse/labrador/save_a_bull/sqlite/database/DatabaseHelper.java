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


public class DatabaseHelper  {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef;
    private DatabaseReference couponRef;
    private List<Coupon> Gallery = new LinkedList<>();

    protected void onCreate() {
        //Prefetch all existing items for the node using a SingleValueEventListener
        FirebaseDatabase.getInstance().getReference("coupon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for(DataSnapshot aSnapshotIterable : snapshotIterable) {
                    aSnapshotIterable.getValue(Coupon.class);
                    Gallery.add(aSnapshotIterable.getValue(Coupon.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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