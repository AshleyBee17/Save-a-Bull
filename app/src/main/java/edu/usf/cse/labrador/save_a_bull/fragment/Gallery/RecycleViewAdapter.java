// Displays each coupon on a card from the information found in each coupon in the list of coupons
package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.User;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder> {

    private Context mContext;
    private List<Coupon> mData;
    private Dialog couponDialog;
    private Dialog fullImageDialog;
    private User user;
    View v;

    public RecycleViewAdapter(Context mContext, List<Coupon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_coupon,viewGroup,false);
       final myViewHolder viewHolder = new myViewHolder(v);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the dialog
                couponDialog = new Dialog(mContext);
                couponDialog.setContentView(R.layout.dialog_coupon);

                final TextView dialogCompanyName = couponDialog.findViewById(R.id.company_name_dialog);
                final TextView dialogCouponDesc = couponDialog.findViewById(R.id.coupon_desc_dialog);
                final ImageView dialogImage = couponDialog.findViewById(R.id.img_dialog);


                dialogCompanyName.setText(mData.get(viewHolder.getAdapterPosition()).getCompanyName());
                dialogCouponDesc.setText(mData.get(viewHolder.getAdapterPosition()).getDescription());

                final Bitmap bitmap = BitmapFactory.decodeByteArray(mData.get(viewHolder.getAdapterPosition()).getImg(), 0, mData.get(viewHolder.getAdapterPosition()).getImg().length);
                dialogImage.setImageBitmap(bitmap);

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

                            Toast.makeText(mContext, "Open map and redirect to this location", Toast.LENGTH_SHORT).show();
                            openMaps(longitude, latitude);
                        }
                    }
                });

                dialogImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fullImageDialog = new Dialog(mContext);
                        fullImageDialog.setContentView(R.layout.dialog_full_image);

                        ImageView large_img = fullImageDialog.findViewById(R.id.enlarged_coupon_img);
                        large_img.setImageBitmap(bitmap);

                        fullImageDialog.show();
                    }
                });
            }
       });
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {


            // Displays the text and images accordingly for each item in the coupon list
            myViewHolder.tv_companyName.setText(mData.get(i).getCompanyName());
            myViewHolder.tv_couponDesc.setText(mData.get(i).getDescription());

            Bitmap bitmap = BitmapFactory.decodeByteArray(mData.get(i).getImg(), 0, mData.get(i).getImg().length);
            myViewHolder.img_coupon.setImageBitmap(bitmap);

            // Manipulating the favorites
            final ImageButton favoriteButton = myViewHolder.img_fav.findViewById(R.id.coupon_favorite);
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isActivated()) {
                        //v.setActivated(!v.isActivated());
                        Toast.makeText(mContext, "Item removed from favorites", Toast.LENGTH_SHORT).show();
                        /*
                         * Then remove the item from the user's favorites
                         *
                         */
                    } else if (!v.isActivated()) {
                        //v.setActivated(v.isActivated());
                        Toast.makeText(mContext, "Item added to favorites", Toast.LENGTH_SHORT).show();

                        /*
                         * Add the item to the user's favorites*
                         *
                         */
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

    private void openCall(final String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        mContext.startActivity(intent);
    }

    private void openMaps(final Double lon, final Double lat){
        final double longitude = -82.426970;
        final double latitude = 28.055774;

        // Should open to MapsFragment and NOT the maps app

        Uri gmmIntentUri = Uri.parse("geo:"+lon+","+lat);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mContext.startActivity(mapIntent);
    }

    // Updates the search when a user enters text
    public void updateList(List<Coupon> searchList){
        mData = new ArrayList<>();
        mData.addAll(searchList);
        notifyDataSetChanged();
    }
}

