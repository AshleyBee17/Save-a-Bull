package edu.usf.cse.labrador.save_a_bull.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.usf.cse.labrador.save_a_bull.R;


public class CameraFragment extends Fragment {

    private static final int cameraRequest = 2019;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        startCamera();
        return inflater.inflate(R.layout.fragment_camera, null);
    }

    private void startCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,cameraRequest);
    }

}
