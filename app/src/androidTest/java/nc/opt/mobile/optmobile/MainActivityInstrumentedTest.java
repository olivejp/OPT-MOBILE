package nc.opt.mobile.optmobile;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by orlanth23 on 01/10/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setupOrientation(){
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void changeOrientation() throws Exception {
        // We check that the frame for the recipe detail is present
        onView(withId(R.id.frame_main)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // We check that the frame for the recipe detail is present
        onView(withId(R.id.frame_main)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}

