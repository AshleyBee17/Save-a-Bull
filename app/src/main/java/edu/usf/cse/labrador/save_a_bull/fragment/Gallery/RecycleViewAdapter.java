package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder> {

    private Context mContext;
    private List<Coupon> mData;

    RecycleViewAdapter(Context mContext, List<Coupon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Takes the cards from the cardview.xml
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_coupon,viewGroup,false);
        return new myViewHolder(v);
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

        // Click to view more information
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open window to show coupon desc.
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
        CardView cardView;

        myViewHolder(View v){
            super(v);

            tv_companyName = itemView.findViewById(R.id.company_name);
            tv_couponDesc = itemView.findViewById(R.id.coupon_desc);
            img_coupon =  itemView.findViewById(R.id.coupon_img);
            cardView = itemView.findViewById(R.id.gallery_cardview);
            img_fav = itemView.findViewById(R.id.coupon_favorite);

        }
    }
}
