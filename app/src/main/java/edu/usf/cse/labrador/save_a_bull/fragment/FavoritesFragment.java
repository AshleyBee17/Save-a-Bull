package edu.usf.cse.labrador.save_a_bull.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.Gallery.RecycleViewAdapter;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class FavoritesFragment extends Fragment implements SearchView.OnQueryTextListener {

    User user;
    RecycleViewAdapter recycleViewAdapter;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(user == null){
            Toast.makeText(getContext(), "No account found", Toast.LENGTH_LONG).show();
        } else if(user.Faves.size() == 0){
            Toast.makeText(getContext(), "No favorites yet! Head on to the gallery to add some favorites", Toast.LENGTH_LONG).show();
        } else {

            user.Faves.clear();

            if (v == null) {

                v = inflater.inflate(R.layout.fragment_gallery, container, false);

                RecyclerView myRecyclerView = v.findViewById(R.id.gallery_recyclerview);
                recycleViewAdapter = new RecycleViewAdapter(getContext(), user.Faves);
                myRecyclerView.setAdapter(recycleViewAdapter);
                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                myRecyclerView.setLayoutManager(mLayoutManager);
            }
        }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    public boolean onQueryTextChange(String s) {
        // Reads the text the user enters in the search field
        // and updates it based on the category
        String input = s.toLowerCase();
        List<Coupon> results = new ArrayList<>();

        for(Coupon c : user.Faves){
            String category = c.getCategory().toLowerCase();
            if(category.contains(input)){
                results.add(c);
            }
        }
        recycleViewAdapter.updateList(results);
        return true;
    }
}
