// Displays each coupon on a card from the information found in each coupon in the list of coupons
package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder> {

    private Context mContext;
    private List<Coupon> mData;
    Dialog couponDialog;

    RecycleViewAdapter(Context mContext, List<Coupon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Takes the cards from the cardview.xml
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_coupon,viewGroup,false);
        final myViewHolder viewHolder = new myViewHolder(v);



        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the dialog
                couponDialog = new Dialog(mContext);
                couponDialog.setContentView(R.layout.dialog_coupon);
                TextView dialogCompanyName = couponDialog.findViewById(R.id.company_name_dialog);
                TextView dialogCouponDesc = couponDialog.findViewById(R.id.coupon_desc_dialog);
                ImageView dialogImage = couponDialog.findViewById(R.id.img_dialog);

                dialogCompanyName.setText(mData.get(viewHolder.getAdapterPosition()).getCompanyName());
                dialogCouponDesc.setText(mData.get(viewHolder.getAdapterPosition()).getDescription());
                dialogImage.setImageResource(mData.get(viewHolder.getAdapterPosition()).getImg());
                couponDialog.show();
                //Toast.makeText(mContext, "Test" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_LONG).show();

                Button callButton = couponDialog.findViewById(R.id.call_dialog_btn);
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Make a call", Toast.LENGTH_LONG).show();
                        openCall(mData.get(viewHolder.getAdapterPosition()).getPhone());
                    }
                });

                Button locationButton = couponDialog.findViewById(R.id.location_dialog_btn);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Open map and redirect to this location", Toast.LENGTH_LONG).show();
                        openMaps(mData.get(viewHolder.getAdapterPosition()).getAddress());
                    }
                });
            }
        });

        return viewHolder; //new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        // Displays the text and images accordingly for each item in the coupon list
        myViewHolder.tv_companyName.setText(mData.get(i).getCompanyName());
        myViewHolder.tv_couponDesc.setText(mData.get(i).getDescription());
        myViewHolder.img_coupon.setImageResource(mData.get(i).getImg());


        // Manipulating the favorites
        final ImageButton favoriteButton = myViewHolder.img_fav.findViewById(R.id.coupon_favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isActivated()){
                    //v.setActivated(!v.isActivated());
                    Toast.makeText(mContext, "Item removed from favorites", Toast.LENGTH_SHORT).show();
                    /*
                     * Then remove the item from the user's favorites
                     *
                     */
                } else if(!v.isActivated()){
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
        String fakephone = "123456789";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", fakephone,null));
        mContext.startActivity(intent);
    }
    private void openMaps(final String addy){
        final double latitude = 28.055774;
        final double longitude = -82.426970;

        // Should open to MapsFragment and NOT the maps app

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mContext.startActivity(intent);

    }
}
