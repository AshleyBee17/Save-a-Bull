// Retrieves coupons from database to be displayed in the recyclerview
package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class GalleryFragment extends Fragment implements SearchView.OnQueryTextListener {

    View v;
    private static List<Coupon> couponList = new ArrayList<>();
    private Context mContext;
    RecycleViewAdapter recycleViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Creating a view of the fragment_gallery .xml layout.
        // Generates the icons and information taken from the coupon list and displays each item
        // in 2 columns and then returns the view

        v = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
        recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
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

        couponList.add(new Coupon("Moe's Southwest Grill", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("Panera Bread", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "Free popcorn!","Entertainment", R.drawable.welcome_background));
        couponList.add(new Coupon("Yellow Place", "Free bananas","Food"));
        couponList.add(new Coupon("Moe's Southwest Grill", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("Panera Bread", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "Free large drink w/ hot dog purchase","Entertainment", R.drawable.welcome_background));
        couponList.add(new Coupon("Yellow Place", "Free bananas","Food"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_gallery);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Try 'Food' or 'Entertainment'...");
    }

    public static void addCoupon(Coupon newCoupon){
        couponList.add(newCoupon);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String input = s.toLowerCase();
        List<Coupon> results = new ArrayList<>();

        for(Coupon c : couponList){
            String category = c.getCategory().toLowerCase();
            if(category.contains(input)){
                results.add(c);
            }
        }
        recycleViewAdapter.updateList(results);
        return true;
    }
}
