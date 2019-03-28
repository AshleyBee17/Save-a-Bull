//package edu.usf.cse.labrador.save_a_bull.fragment.favorites;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import edu.usf.cse.labrador.save_a_bull.R;
//
//public class FavoritesFragment extends Fragment {
//
//    Context mContext;
//    View v;
//
//
//    public FavoritesFragment() { }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        if (v == null) {
//
//            v = inflater.inflate(R.layout.fragment_favorites, container, false);
//            mContext = this.getContext();
//
//        }
//        return v;
//    }
//}
//
//
//
//

package edu.usf.cse.labrador.save_a_bull.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.gallery.RecycleViewAdapter;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class FavoritesFragment extends Fragment {
    private UsersDBManager myUsersData;
    private FirebaseUser loggedInUserFB;
    private Context mContext;
    private User loggedInUser;
    RecycleViewAdapter recycleViewAdapter;
    View v;
    private List<Coupon> Fav = new ArrayList<>();
    private DatabaseReference mDatabase;

    private String favoritesLine;
    private List<String> favoritesList;

    public FavoritesFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        loggedInUserFB = FirebaseAuth.getInstance().getCurrentUser();
        assert loggedInUserFB != null;
        mContext = getContext();

        try {
            favoritesLine = readFileOnInternalStorage(mContext,loggedInUserFB.getUid()+"_file" );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(v == null) {
            v = inflater.inflate(R.layout.fragment_favorites, container, false);

            mDatabase = FirebaseDatabase.getInstance().getReference("");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieving coupons from Firebase to display in RecycleView
                    for (DataSnapshot couponSnapshot : dataSnapshot.getChildren()) {
                        Coupon coupon = couponSnapshot.getValue(Coupon.class);

                        if (favoritesLine != null) {
                            favoritesList = User.convertStringToArray(favoritesLine);
                            for (String s : favoritesList) {
                                if (coupon.getId().equals(s)) {
                                    Fav.add(coupon);
                                }
                            }
                        }
                    }


                    if (Fav.size() == 0) {
                        Toast.makeText(getContext(), "No favorites yet! Head on to the " +
                                "gallery to add some favorites", Toast.LENGTH_LONG).show();
                    } else {
                        getRecycleView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }

            });
        }
        return v;
    }

    public void getRecycleView()
    {
        RecyclerView myRecycleView = v.findViewById(R.id.favorites_recyclerview);
        recycleViewAdapter = new RecycleViewAdapter(getContext(), Fav);
        myRecycleView.setAdapter(recycleViewAdapter);
        GridLayoutManager mLayoutMan = new GridLayoutManager(getActivity(), 2);
        myRecycleView.setLayoutManager(mLayoutMan);
    }

    private void writeFileOnInternalStorage(Context mcoContext,String sFileName, String sBody) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                File(mContext.getFilesDir() + File.separator + sFileName + ".txt")));
        bufferedWriter.write(sBody);
        bufferedWriter.close();
    }

    private String readFileOnInternalStorage(Context mcoContext,String sFileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new
                File(mContext.getFilesDir() + File.separator + sFileName + ".txt")));
        String read;
        StringBuilder builder = new StringBuilder("");

        while((read = bufferedReader.readLine()) != null){
            builder.append(read);
        }
        Log.d("Output", builder.toString());
        bufferedReader.close();

        return  builder.toString();
    }
}




