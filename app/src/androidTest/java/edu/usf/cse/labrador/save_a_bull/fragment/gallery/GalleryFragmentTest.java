package edu.usf.cse.labrador.save_a_bull.fragment.gallery;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.RelativeLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import edu.usf.cse.labrador.save_a_bull.fragment.gallery.GalleryFragment;
import edu.usf.cse.labrador.save_a_bull.MainScreen;
import edu.usf.cse.labrador.save_a_bull.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GalleryFragmentTest {

    @Rule
    public ActivityTestRule<MainScreen> mainScreenActivityTestRule = new
            ActivityTestRule<>(MainScreen.class, true, false);
    private GalleryFragment gFragment = new GalleryFragment();

    @Before
    public void setUp() throws Exception {
//        mScreen = mainScreenActivityTestRule.getActivity();
        gFragment=null;
        this.mainScreenActivityTestRule.launchActivity(new Intent().putExtra(MainScreen.DISPLAY_SERVICE, 2));

        // this.gFragment = ((GalleryFragment) this.mainScreenActivityTestRule.getActivity().getFragmentManager().findFragmentById(R.id.fragment_gallery_id));
    }

    @Test
    public void testOnCreateView(){
        assertEquals(3,this.gFragment.getId());
    }

    @After
    public void tearDown() throws Exception {
        gFragment = null;
    }
}