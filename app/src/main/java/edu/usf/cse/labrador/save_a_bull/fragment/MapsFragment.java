//package edu.usf.cse.labrador.save_a_bull.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import edu.usf.cse.labrador.save_a_bull.R;
//
//public class MapsFragment extends Fragment implements OnMapReadyCallback {
//
//    View v;
//    GoogleMap mGoogleMap;
//    MapView mMapView;
//
//    static final LatLng USF = new LatLng(28.061816, -82.411282);
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.fragment_maps, container, false);
//        return v;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        mMapView = v.findViewById(R.id.google_map_id);
////        if(mMapView != null){
////            mMapView.onCreate(null);
////            mMapView.onResume();
////            mMapView.getMapAsync(this);
////        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
////        MapsInitializer.initialize(getContext());
////        mGoogleMap = googleMap;
////        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        //googleMap.addMarker(new MarkerOptions().position(USF).title("USF").snippet("Happy couponing!"));
//        //CameraPosition cameraPosition = CameraPosition.builder().target(USF).zoom(15).build();
//        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//    }
//}
//

package edu.usf.cse.labrador.save_a_bull.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.usf.cse.labrador.save_a_bull.R;
import edu.usf.cse.labrador.save_a_bull.directionHelpers.TaskLoadedCallback;
import edu.usf.cse.labrador.save_a_bull.directionHelpers.FetchURL;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

//import android.app.Fragment;

public class MapsFragment extends Fragment implements TaskLoadedCallback, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private float zoom = 12;
    LocationManager locationManager;
    LatLng myLocation;
    private Polyline currentPolyline;
    GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    private MarkerOptions place1, place2;


    // View & Context Setup
    private Context mContext;


    // Database
    private DatabaseReference mDatabase;
    private static List<Coupon> couponList = new ArrayList<>();
    private Coupon mCoupon;


    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maps, null, false);
        mContext = v.getContext();
        return v;
    }

    @Nullable
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    System.out.println("Hellow Wold 1");
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String add = addressList.get(0).getAddressLine(0);
                        //String lat = "Latitude: " + addressList.get(0).getLatitude();
                        //String lng = "Longitude: " + addressList.get(0).getLongitude();
                        String date = "Time: " + mLastUpdateTime + "\n";

                        System.out.println("Hellow Wold 2");
                        mMap.addMarker(new MarkerOptions().position(latLng).title(add).snippet(date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).flat(true));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    System.out.println("Hellow Wold 3");

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getAddressLine(0);
                        str += addressList.get(0).getLocality();
                        str += addressList.get(0).getPostalCode();
                        System.out.println("Hellow Wold 4");
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).flat(true));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        couponList.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference("");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieving coupons from Firebase to display location markers in map
                for (DataSnapshot couponSnapshot : dataSnapshot.getChildren()) {
                    Coupon coupon = couponSnapshot.getValue(Coupon.class);
                    couponList.add(coupon);
                    addMarker(coupon);
                }

                if (couponList.size() == 0) {
                    Toast.makeText(getContext(), "No coupons in the gallery. Go to the camera to add some coupons to share!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }


    private boolean addMarker(Coupon myCoupon) {

        Geocoder gc = new Geocoder(mContext);
        Double latitude;
        Double longitude;

        List<Address> addressList = null;
        try {
            addressList = gc.getFromLocationName(myCoupon.getAddr(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList.size() != 0 && addressList != null) {
            Address add = addressList.get(0);
            latitude = add.getLatitude();
            longitude = add.getLongitude();
            LatLng couponAdd = new LatLng(latitude, longitude);
            LatLng laLng = new LatLng(28.058665, -82.413704);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laLng, zoom));
            if (myCoupon.getCategory().equals("Food")) {
                //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_food);
                mMap.addMarker(new MarkerOptions().position(couponAdd).title(myCoupon.getCompanyName()).snippet(myCoupon.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).flat(true));
            }
            if (myCoupon.getCategory().equals("Grocery")) {
                //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_grocery);
                mMap.addMarker(new MarkerOptions().position(couponAdd).title(myCoupon.getCompanyName()).snippet(myCoupon.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).flat(true));
            }
            if (myCoupon.getCategory().equals("Clothing")) {
                //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_clothing);
                mMap.addMarker(new MarkerOptions().position(couponAdd).title(myCoupon.getCompanyName()).snippet(myCoupon.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).flat(true));
            }
            if (myCoupon.getCategory().equals("Automotive")) {
                // Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_automotive);
                mMap.addMarker(new MarkerOptions().position(couponAdd).title(myCoupon.getCompanyName()).snippet(myCoupon.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).flat(true));
            }
            if (myCoupon.getCategory().equals("Entertainment")) {
                //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_entertainment);
                mMap.addMarker(new MarkerOptions().position(couponAdd).title(myCoupon.getCompanyName()).snippet(myCoupon.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).flat(true));
            }
            return true;
        } else return false;

    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng latLng = new LatLng(28.058826,  -82.413940);
        MarkerOptions USF = new MarkerOptions().position(latLng).title("I am here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).flat(true);
        mMap.addMarker(USF);
        new FetchURL(mContext).execute(getUrl(USF.getPosition(), marker.getPosition(), "driving"), "driving");

        return false;
    }
}
