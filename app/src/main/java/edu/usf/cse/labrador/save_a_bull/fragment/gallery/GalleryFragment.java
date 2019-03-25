// Retrieves coupons from database to be displayed in the recyclerview
package edu.usf.cse.labrador.save_a_bull.fragment.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.DatabaseHelper;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;

public class GalleryFragment extends Fragment implements SearchView.OnQueryTextListener {


    @SuppressLint("StaticFieldLeak")
    private static List<Coupon> couponList = new ArrayList<>();
    private static RecycleViewAdapter recycleViewAdapter;
    View v;

    // Database retrieval
    private DatabaseReference mDatabase;

    public GalleryFragment(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              Bundle savedInstanceState) {

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        String expiryDate = currentDay + "/" + currentMonth + "/" + currentYear;

        couponList.clear();

        if(v == null) {
            v = inflater.inflate(R.layout.fragment_gallery, container, false);

            setHasOptionsMenu(true);


            mDatabase = FirebaseDatabase.getInstance().getReference("");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot couponSnapshot : dataSnapshot.getChildren()){
                        Coupon coupon = couponSnapshot.getValue(Coupon.class);

                        //Log.d("TEST_COUPON_LIST", "id: " + coupon.getId());
                        //Log.d("TEST_COUPON_LIST", "name: " + coupon.getCompanyName());
                        //Log.d("TEST_COUPON_LIST", "desc: " + coupon.getDescription());
                        //Log.d("TEST_COUPON_LIST", "key: " + couponSnapshot.getKey());
                        Log.d("TEST_COUPON_LIST", "name: " + coupon.getCompanyName());
                        couponList.add(coupon);
                    }
                    getRecycleView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            for(Coupon c : couponList){
//                if(c.getExpire() != null){
//                    String exp = c.getExpire();
//                    if(exp.equals(expiryDate)){
//                        // delete from database;
//                    }
//                }
//            }


            if(couponList.size() == 0){
                Toast.makeText(getContext(), "No coupons in the gallery. Go to the camera to add some coupons to share!", Toast.LENGTH_LONG).show();
            }

            // Creating a view of the fragment_gallery .xml layout.
            // Generates the icons and information taken from the coupon list and displays each item
            // in 2 columns and then returns the view
            //getRecycleView();
//            RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
//            recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
//            myRecyclerView.setAdapter(recycleViewAdapter);
//            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//            myRecyclerView.setLayoutManager(mLayoutManager);
        }
        return v;
    }

    public void getRecycleView(){
        RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
        recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
        myRecyclerView.setAdapter(recycleViewAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        myRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Setting up search options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){

        menuInflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_gallery);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Try 'Food' or 'Entertainment'...");
    }

    public static void addCoupon(Coupon newCoupon){
        couponList.add(0, newCoupon);
        recycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    // Reads the text the user enters in the search field and updates it based on
    // the category
    @Override
    public boolean onQueryTextChange(String s) {
        String input = s.toLowerCase();
        List<Coupon> results = new ArrayList<>();

        for(Coupon c : couponList){
            if(c.getCategory() != null){
                String category = c.getCategory().toLowerCase();
                if(category.contains(input)){
                    results.add(c);
                }
            }
        }
        recycleViewAdapter.updateList(results);
        return true;
    }
}

