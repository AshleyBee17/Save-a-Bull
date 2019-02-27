package edu.usf.cse.labrador.save_a_bull;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.usf.cse.labrador.save_a_bull.fragment.CameraFragment;
import edu.usf.cse.labrador.save_a_bull.fragment.FavoritesFragment;
import edu.usf.cse.labrador.save_a_bull.fragment.GalleryFragment;
import edu.usf.cse.labrador.save_a_bull.fragment.HomeFragment;
import edu.usf.cse.labrador.save_a_bull.fragment.MapsFragment;


public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //loading the default home fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    // Switch between fragment displays depending on the nav item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;

            case R.id.nav_favorites:
                fragment = new FavoritesFragment();
                break;

            case R.id.nav_camera:
                fragment = new CameraFragment();
                break;

            case R.id.nav_gallery:
                fragment = new GalleryFragment();
                break;

            case R.id.nav_maps:
                fragment = new MapsFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
