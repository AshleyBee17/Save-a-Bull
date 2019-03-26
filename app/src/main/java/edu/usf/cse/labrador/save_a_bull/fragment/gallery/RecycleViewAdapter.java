// Displays each coupon on a card from the information found in each coupon in the list of coupons
package edu.usf.cse.labrador.save_a_bull.fragment.gallery;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.MapsFragment;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.UsersDBManager;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Address;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder> {

    // View & Context
    private View v;
    private  Context mContext;
    private Bitmap enlargedImg;
    private ImageButton favoriteButton;

    // Data
    private List<Coupon> mData;
    private FirebaseUser loggedInUserFB;

    // Dialogs
    private Dialog couponDialog;
    private Dialog fullImageDialog;

    // Accounts
    User loggedInUser;
    UsersDBManager myUsersData;
    private FirebaseAuth auth;
    List<String> userFavorites;

    public RecycleViewAdapter() { }

    public RecycleViewAdapter(Context mContext, List<Coupon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // For mini dialog that is displayed when a coupon card item is clicked
        myUsersData = new UsersDBManager(mContext);
        myUsersData.open();
        auth = FirebaseAuth.getInstance();
        loggedInUserFB = FirebaseAuth.getInstance().getCurrentUser();
        assert loggedInUserFB != null;
        loggedInUser = myUsersData.getUserReturnUserType(loggedInUserFB.getEmail());

        v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_coupon,viewGroup,false);
        final myViewHolder viewHolder = new myViewHolder(v);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //Initialize the dialog
                couponDialog = new Dialog(mContext);
                couponDialog.setContentView(R.layout.dialog_coupon);

                final TextView dialogCompanyName = couponDialog.findViewById(R.id.company_name_dialog);
                final TextView dialogCouponDesc = couponDialog.findViewById(R.id.coupon_desc_dialog);
                final TextView dialogCouponExp = couponDialog.findViewById(R.id.coupon_exp_dialog);
                final ImageView dialogImage = couponDialog.findViewById(R.id.img_dialog);

                try {
                    Bitmap imageBitmap = decodeFromFirebaseBase64(mData.get(viewHolder.getAdapterPosition()).getImg());
                    dialogImage.setImageBitmap(imageBitmap);
                    enlargedImg = imageBitmap;

                    dialogCompanyName.setText(mData.get(viewHolder.getAdapterPosition()).getCompanyName());
                    dialogCouponDesc.setText(mData.get(viewHolder.getAdapterPosition()).getDescription());
                    if(mData.get(viewHolder.getAdapterPosition()).getExpire().trim().equals("")){
                        dialogCouponExp.setText("No Expiry");
                    } else dialogCouponExp.setText("Expires: " + mData.get(viewHolder.getAdapterPosition()).getExpire());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                couponDialog.show();

                Button callButton = couponDialog.findViewById(R.id.call_dialog_btn);
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = mData.get(viewHolder.getAdapterPosition()).getPhone();

                        if(phone == null){
                            Toast.makeText(mContext, "Phone number not available", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Make a call", Toast.LENGTH_SHORT).show();
                            openCall(phone);
                        }
                    }
                });

                Button locationButton = couponDialog.findViewById(R.id.location_dialog_btn);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(mData.get(viewHolder.getAdapterPosition()).getLongitude() == null ||
                                mData.get(viewHolder.getAdapterPosition()).getLatitude() == null){
                            Toast.makeText(mContext, "Location not available", Toast.LENGTH_SHORT).show();
                        } else {
                            Double latitude = mData.get(viewHolder.getAdapterPosition()).getLatitude();
                            Double longitude = mData.get(viewHolder.getAdapterPosition()).getLongitude();
                            String address = mData.get(viewHolder.getAdapterPosition()).getAddress();;

                            Toast.makeText(mContext, "Open map and redirect to this location", Toast.LENGTH_SHORT).show();
                            openMaps(longitude, latitude, address);
                        }
                    }
                });

                dialogImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fullImageDialog = new Dialog(mContext);
                        fullImageDialog.setContentView(R.layout.dialog_full_image);

                        ImageView large_img = fullImageDialog.findViewById(R.id.enlarged_coupon_img);
                        large_img.setImageBitmap(enlargedImg);

                        fullImageDialog.show();
                    }
                });
            }
       });
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i) {

        try {
            Bitmap imageBitmap = decodeFromFirebaseBase64(mData.get(i).getImg());

            myViewHolder.img_coupon.setImageBitmap(imageBitmap);
            myViewHolder.tv_companyName.setText(mData.get(i).getCompanyName());
            myViewHolder.tv_couponDesc.setText(mData.get(i).getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }

        favoriteButton = myViewHolder.img_fav.findViewById(R.id.coupon_favorite);

//        for(Coupon c : mData){
//            if(loggedInUser.Faves == mData.get(i).getId()){
//                favoriteButton.isActivated();
//            }
//        }

        for(Coupon c: mData){
            if(c.getId() == loggedInUser.Faves.toString()){
                favoriteButton.isActivated();
            }
        }

        // Manipulating the favorites
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (v.isActivated()) {
                    /*Remove from favorites*/

                } else if (!v.isActivated()) {
                    /*Add to favorites*/

                    loggedInUser.Line = loggedInUser.Line.concat(mData.get(i).getId() + ",");
                    //userFavorites = User.convertStringToArray(loggedInUser.Line);
                    loggedInUser.Faves = User.convertStringToArray(loggedInUser.Line);;
                    //userFavorites.add(String.valueOf(User.convertStringToArray(loggedInUser.Line)));

                    Toast.makeText(mContext, "Item added to favorites",
                            Toast.LENGTH_SHORT).show();
                }
                v.setActivated(!v.isActivated());
            }
        });
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        // Gets the id names of all components to the cardView items
        private TextView tv_companyName;
        private TextView tv_couponDesc;
        private ImageView img_coupon;
        private ImageButton img_fav;
        private CardView cardView;

        myViewHolder(View v){
            super(v);

            tv_companyName = itemView.findViewById(R.id.company_name);
            tv_couponDesc = itemView.findViewById(R.id.coupon_desc);
            img_coupon =  itemView.findViewById(R.id.coupon_img);
            img_fav = itemView.findViewById(R.id.coupon_favorite);
            cardView = itemView.findViewById(R.id.cardview_item_coupon_id);
        }
    }

    private  void openCall(final String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        mContext.startActivity(intent);
    }

    private  void openMaps(final Double lon, final Double lat, String address){
        final double longitude = -82.426970;
        final double latitude = 28.055774;

        Address a = new Address(address);

        // Should open to MapsFragment and NOT the maps app
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        MapsFragment myFragment = new MapsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//        Uri gmmIntentUri = Uri.parse("geo:"+lon+","+lat);
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        mContext.startActivity(mapIntent);
    }

    // Updates the search when a user enters text
    void updateList(List<Coupon> searchList){
        mData = new ArrayList<>();
        mData.addAll(searchList);
        notifyDataSetChanged();
    }

    private static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private void getUserInformation(){

        //Cursor sdlkfjn = myUsersData.getUser(loggedInUserFB.getEmail());
        loggedInUser = myUsersData.getUserReturnUserType(loggedInUserFB.getEmail());

    }
}

