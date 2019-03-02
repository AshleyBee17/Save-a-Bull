package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.DatabaseHelper;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;
import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.Gallery.GalleryFragment;

import static java.sql.Types.NULL;


public class CameraFragment extends Fragment {


    private static final int CAMERA_REQUEST_CODE = 2019;
    Button takePhotoBtn;
    Button uploadBtn;
    static byte[] imgStream;
    ImageView imageView;
    View v;

    DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_camera,
                container, false);

        // Setting up dropdown to allow a user to select a category for a coupon
        Spinner spinner = v.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoriesArray = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category_types));
        categoriesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoriesArray);

        // Setting up buttons and views
        takePhotoBtn = v.findViewById(R.id.takePhoto_btn);
        uploadBtn = v.findViewById(R.id.upload_btn);
        imageView = v.findViewById(R.id.coupon_img);

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAMERA_REQUEST_CODE);

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToGallery();
            }
        });

        db = new DatabaseHelper(getContext());

        return v;//inflater.inflate(R.layout.fragment_camera, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                imgStream = stream.toByteArray();

                // convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                imageView.setImageBitmap(bitmap);

            }
        }
    }



    private void uploadToGallery(){
        TextView companyName = v.findViewById(R.id.companyName_txt);//.getText().toString();
        TextView couponDescription = v.findViewById(R.id.couponDesc_txt);//.toString();
        ImageView couponImage = v.findViewById(R.id.coupon_img); // save as an image??
        Spinner categoryType = v.findViewById(R.id.categorySpinner);


        String cName = companyName.getText().toString();
        String cDesc = couponDescription.getText().toString();
        String cCat = categoryType.toString();
        //Bitmap cImg = ;
        //int img = couponImage.getBaseline();

//        Coupon c = new Coupon();
//        c.setCompanyName(cName);
//        c.setDescription(cDesc);
//        //c.setImg(couponImage);
//        // ADD TO DATABASE
//        GalleryFragment.addCoupon(c);

        long id = db.insertMinCoupon(cName, cDesc, imgStream);
        Coupon c = db.getCoupon(id);

        if (c != null){
            GalleryFragment.addCoupon(c);
        }

    }
}
