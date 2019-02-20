package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class GalleryFragment extends Fragment {

    View v;
    private List<Coupon> couponList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
        couponList = new ArrayList<>();
        couponList.add(new Coupon("Moe's Southwest Grill", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("Panera Bread", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "10% off with USFID","Food", R.drawable.welcome_background));
        couponList.add(new Coupon("Yellow Place", "Free bananas","Food"));
    }
}
