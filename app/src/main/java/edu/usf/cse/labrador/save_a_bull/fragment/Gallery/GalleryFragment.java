// Retrieves coupons from database to be displayed in the recyclerview
package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.DatabaseHelper;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class GalleryFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static List<Coupon> couponList = new ArrayList<>();
    private static RecycleViewAdapter recycleViewAdapter;
    private DatabaseHelper db;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              Bundle savedInstanceState) {

        couponList.clear();

        if(v == null) {
            v = inflater.inflate(R.layout.fragment_gallery, container, false);

            setHasOptionsMenu(true);

            db = new DatabaseHelper(getContext());
            couponList.addAll(db.getAllCoupons());

            // Creating a view of the fragment_gallery .xml layout.
            // Generates the icons and information taken from the coupon list and displays each item
            // in 2 columns and then returns the view
            RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
            recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
            myRecyclerView.setAdapter(recycleViewAdapter);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            myRecyclerView.setLayoutManager(mLayoutManager);
        }
        return v;
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
