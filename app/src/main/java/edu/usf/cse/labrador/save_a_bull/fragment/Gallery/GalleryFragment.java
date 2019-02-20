package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getContext(), couponList);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recycleViewAdapter);

        return v;
        //return inflater.inflate(R.layout.fragment_gallery, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        couponList = new ArrayList<>();
        // Take this information from the database when fully implemented
        couponList.add(new Coupon("Moe's", "10% off with USFID","Food", R.drawable.moes));
        couponList.add(new Coupon("Chicken Salad Chick", "15% off with USFID","Food", R.drawable.csc));
        couponList.add(new Coupon("panera", "Student Fridays - Free Drink","Food", R.drawable.panera));
        couponList.add(new Coupon("Book Holders", "Free Koozie with rental","Books", R.drawable.bh));
        couponList.add(new Coupon("Blank", "10% off with USFID","Food", R.drawable.welcome_background));
    }
}
