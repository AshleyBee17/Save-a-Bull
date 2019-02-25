package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

//@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment {

    View v;
    private static List<Coupon> couponList = new ArrayList<>();
    private Context mContext;


//    @SuppressLint("ValidFragment")
//    GalleryFragment(Context mContext) {
//        this.mContext = mContext;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Creating a view of the fragment_gallery .xml layout.
        // Generates the icons and information taken from the coupon list and displays each item
        // in 2 columns and then returns the view

        v = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
        myRecyclerView.setAdapter(recycleViewAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        myRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
          When the database is completed, make iterate through each "coupon" table to add it to
          the coupon list to be shown in the recyclerView
         */
        //couponList = new ArrayList<>();
        couponList.add(new Coupon("Moe's Southwest Grill", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("Panera Bread", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "10% off with USFID","Food", R.drawable.welcome_background));
        couponList.add(new Coupon("Yellow Place", "Free bananas","Food"));
        couponList.add(new Coupon("Moe's Southwest Grill", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("Panera Bread", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "10% off with USFID","Food", R.drawable.welcome_background));
        couponList.add(new Coupon("Yellow Place", "Free bananas","Food"));
    }

    public static void addCoupon(Coupon newCoupon){
        couponList.add(newCoupon);
    }

    //@Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // called when search is clicked
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // called when each new letter is typed in the search
//                return false;
//            }
//        });
//        return true;//super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.action_settings){
//            Toast.makeText(mContext, "Settings", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
