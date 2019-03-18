package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.Gallery.GalleryFragment;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.DatabaseHelper;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;


public class CameraFragment extends Fragment {


    private static final int CAMERA_REQUEST_CODE = 2019;
    private static final int GALLERY_REQUEST_CODE = 1917;
    Button takePhotoBtn;
    Button uploadBtn;
    Button findPhotoBtn;
    Button selectExpiryBtn;
    String selectedDate;

    static byte[] imgStream;
    ImageView imageView;
    TextView couponExpiry;
    View v;

    Calendar c;
    DatePickerDialog datePickerDialog;
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
        findPhotoBtn = v.findViewById(R.id.findPhoto_btn);
        uploadBtn = v.findViewById(R.id.upload_btn);
        imageView = v.findViewById(R.id.coupon_img);
        selectExpiryBtn = v.findViewById(R.id.select_expiry_btn);
        couponExpiry = v.findViewById(R.id.expiry_date);

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

        findPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
            }
        });

        selectExpiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int dpYear, int dpMonth, int dpDay) {
                        selectedDate = dpDay + "/" + dpMonth + 1 + "/" + dpYear;
                        couponExpiry.setText(selectedDate);
                        Log.d("CAM_FRAG", " "+ selectedDate);
                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });

        db = new DatabaseHelper(getContext());

        return v;
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
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bitmap bmp = null;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //imageView.setImageBitmap(bitmap);
                    //Bitmap bmp = (Bitmap) data.getExtras().get("data");
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
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadToGallery(){

        TextView companyName = v.findViewById(R.id.companyName_txt);//.getText().toString();
        TextView couponDescription = v.findViewById(R.id.couponDesc_txt);//.toString();
        //TextView couponExpiry = v.findViewById(R.id.expiry_date);
        Spinner categoryType = v.findViewById(R.id.categorySpinner);

        //couponExpiry.setText(selectedDate);

        String cName = companyName.getText().toString();
        String cDesc = couponDescription.getText().toString();
        String cCat = categoryType.getSelectedItem().toString();
        String cExp = couponExpiry.getText().toString();

        if(cName.trim().length() == 0 || cDesc.trim().length() == 0 || cCat.trim().length() == 0 || imgStream == null || cExp.trim().length() == 0){
            Log.d("CAM_FRAG", "Not all fields are filled out");
            Toast.makeText(getContext(), "Fill out all fields and take a photo before uploading", Toast.LENGTH_LONG).show();
        } else {

            //long id = db.insertMinCoupon(cName, cDesc, imgStream, cCat,exp);
            //Coupon c = db.getCoupon(id);
            long id = db.insertMinCoupon(cName, cDesc, imgStream, cCat, cExp);
            Coupon c = db.getCoupon(id);

            if (c != null) {
                GalleryFragment.addCoupon(c);
                Toast.makeText(getContext(), "Coupon uploaded to the gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
