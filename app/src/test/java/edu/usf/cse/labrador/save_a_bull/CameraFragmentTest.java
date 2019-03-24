package edu.usf.cse.labrador.save_a_bull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import edu.usf.cse.labrador.save_a_bull.fragment.CameraFragment;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
public class CameraFragmentTest {

    private CameraFragment cameraFragment;

    @Before
    public void setUp(){
        cameraFragment = CameraFragment.newInstance();
    }

    @Test
    public void testNotNull(){
        startFragment(cameraFragment);
        assertNotNull(cameraFragment);
    }

    @After
    public void tearDown() throws Exception {
        cameraFragment = null;
    }

}
