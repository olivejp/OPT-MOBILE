package nc.opt.mobile.optmobile.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.awaitility.Duration;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

import nc.opt.mobile.optmobile.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivitySuiviColisTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private Callable<Boolean> openNavigationDrawer() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ViewInteraction appCompatImageButton = onView(
                        allOf(
                                withContentDescription(R.string.navigation_drawer_open),
                                childAtPosition(allOf(
                                        withId(R.id.toolbar),
                                        childAtPosition(withClassName(
                                                is("android.support.design.widget.AppBarLayout")), 0))
                                        , 1),
                                isDisplayed()));
                appCompatImageButton.perform(click());
                return true;
            }
        };
    }

    private Callable<Boolean> clickNavigationView() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ViewInteraction navigationMenuItemView = onView(
                        allOf(childAtPosition(
                                allOf(withId(R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                5),
                                isDisplayed()));
                navigationMenuItemView.perform(click());
                return true;
            }
        };
    }

    private Callable<Boolean> clickFloatingButton() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ViewInteraction floatingActionButton = onView(
                        allOf(withId(R.id.fab_add_parcel),
                                childAtPosition(
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0),
                                        1),
                                isDisplayed()));
                floatingActionButton.perform(click());
                return true;
            }
        };
    }

    private Callable<Boolean> checkEditIdAndDescriptionAreDisplayed() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ViewInteraction editText = onView(
                        allOf(withId(R.id.edit_id_parcel),
                                isDisplayed()));
                editText.check(matches(isDisplayed()));

                ViewInteraction editText2 = onView(
                        allOf(withId(R.id.edit_description_parcel),
                                isDisplayed()));
                editText2.check(matches(isDisplayed()));
                return true;
            }
        };
    }

    @Test
    public void mainActivitySuiviColisTest() {

        await().atMost(Duration.FIVE_SECONDS).until(openNavigationDrawer());

        await().atMost(Duration.FIVE_SECONDS).until(clickNavigationView());

        await().atMost(Duration.FIVE_SECONDS).until(clickFloatingButton());

        await().atMost(Duration.FIVE_SECONDS).until(checkEditIdAndDescriptionAreDisplayed());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
