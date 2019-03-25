package edu.usf.cse.labrador.save_a_bull.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.fragment.gallery.GalleryFragment;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.DatabaseHelper;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Address;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;

public class CameraFragment extends Fragment implements SensorEventListener {

    // View & Context Setup
    private Context mContext;
    View v;

    // Sensor
    private long mLastShakeTime;
    Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 5.25f;
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;

    // Image conversion
    static byte[] imgStream;
    ImageView imageView;
    private static final int CAMERA_REQUEST_CODE = 2019;
    private static final int GALLERY_REQUEST_CODE = 1917;

    // Database
    //DatabaseHelper db;
    private DatabaseReference mDatabase;

    // UI Elements
    Button takePhotoBtn;
    Button uploadBtn;
    Button findPhotoBtn;
    Button selectExpiryBtn;
    String selectedDate;
    TextView companyName;
    TextView companyAddress;
    TextView companyPhone ;
    TextView couponDescription;
    TextView couponExpiry;
    DatePickerDialog datePickerDialog;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_camera,
                container, false);
        mContext = v.getContext();

        // Setting up dropdown to allow a user to select a category for a coupon
        Spinner spinner = v.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoriesArray = new ArrayAdapter<>(getContext(),
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

        findPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        GALLERY_REQUEST_CODE);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToGallery();
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

        mDatabase = FirebaseDatabase.getInstance().getReference();


        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
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
                        bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).
                                getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        // Gathers all of the text in each of the text views, spinners, and the image bitmap
        // to create a new Coupon to add it to the database.
        companyName = v.findViewById(R.id.companyName_txt);
        companyAddress = v.findViewById(R.id.companyAddr_txt);
        companyPhone = v.findViewById(R.id.companyPhone_txt);
        couponDescription = v.findViewById(R.id.couponDesc_txt);
        couponExpiry = v.findViewById(R.id.expiry_date);
        Spinner categoryType = v.findViewById(R.id.categorySpinner);

        String cName = companyName.getText().toString();
        String cAdd = companyAddress.getText().toString();
        String cPhone = companyPhone.getText().toString();
        String cDesc = couponDescription.getText().toString();
        String cCat = categoryType.getSelectedItem().toString();
        String cExp = couponExpiry.getText().toString();

        if(cName.trim().length() == 0 || cDesc.trim().length() == 0 || cCat.trim().length() == 0 || imgStream == null){// || cExp.trim().length() == 0){
            Log.d("CAM_FRAG", "Not all fields are filled out");
            Toast.makeText(getContext(), "Fill out all fields and take a photo before uploading", Toast.LENGTH_LONG).show();
        } else {

            Random rand = new Random();
            int random = rand.nextInt(100000);
            String id = Integer.toString(random);
            Address a = new Address(cAdd);
            Coupon c = new Coupon ( id, cAdd, cCat, cName, cDesc, cExp, imgStream, cPhone );

            mDatabase.child(id).setValue(c);

            /*long id = db.insertMinCoupon(cName, cDesc, imgStream, cCat, cExp);
            Coupon c = db.getCoupon(id);

            if (c != null) {
                GalleryFragment.addCoupon(c);
                Toast.makeText(getContext(), "Coupon uploaded to the gallery", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Uses the accelerometer sensor and measures the current time every millisecond
        // If the acceleration between the x,y, and z planes are greater than the shake threshold
        // during a given time interval, the clear entries method is called
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                Log.d("SENSOR", "Acc: " + acceleration);

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    clearEntries();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    private void clearEntries(){
        // Shows an alert dialog that asks the users if they want to clear all of the fields
        // They entered for a coupon. If they say yes, all the entries are reset. If they say no,
        // the dialog disappears and their entries remain the same.
        AlertDialog.Builder clearEntries = new AlertDialog.Builder(mContext);
        clearEntries.setMessage("Clear all entries?");
        clearEntries.setCancelable(true);

        clearEntries.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, "Clearing Entries", Toast.LENGTH_SHORT).show();
                        companyName = v.findViewById(R.id.companyName_txt);
                        companyAddress = v.findViewById(R.id.companyAddr_txt);
                        companyPhone = v.findViewById(R.id.companyPhone_txt);
                        couponDescription = v.findViewById(R.id.couponDesc_txt);
                        couponExpiry = v.findViewById(R.id.expiry_date);
                        imageView = v.findViewById(R.id.coupon_img);
                        companyName.setText(null);
                        companyAddress.setText(null);
                        companyPhone.setText(null);
                        couponDescription.setText(null);
                        couponExpiry.setText(null);
                        imageView.setImageBitmap(null);
                    }
                });

        clearEntries.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = clearEntries.create();
        alertDialog.show();
    }
}
