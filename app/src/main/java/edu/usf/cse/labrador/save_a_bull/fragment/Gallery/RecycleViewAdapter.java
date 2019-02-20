package edu.usf.cse.labrador.save_a_bull.fragment.Gallery;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.usf.cse.labrador.save_a_bull.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder> {


    Context mContext;
    List<Coupon> mData;

    public RecycleViewAdapter(Context mContext, List<Coupon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_coupon,viewGroup,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        myViewHolder.tv_companyName.setText(mData.get(i).getCompanyName());
        myViewHolder.tv_couponDesc.setText(mData.get(i).getDescription());
        myViewHolder.img_coupon.setImageResource(mData.get(i).getImg());

        final ImageButton favoriteButton = myViewHolder.img_fav.findViewById(R.id.coupon_favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(!v.isActivated());

            }
        });

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

    public static class myViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_companyName;
        private TextView tv_couponDesc;
        private ImageView img_coupon;
        private ImageButton img_fav;
        CardView cardView;

        public myViewHolder(View v){
            super(v);

            tv_companyName = itemView.findViewById(R.id.company_name);
            tv_couponDesc = itemView.findViewById(R.id.coupon_desc);
            img_coupon =  itemView.findViewById(R.id.coupon_img);
            cardView = itemView.findViewById(R.id.gallery_cardview);
            img_fav = itemView.findViewById(R.id.coupon_favorite);

        }
    }
}
