package nc.opt.mobile.optmobile.activity;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.ProviderTestUtilities;
import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.provider.OptProvider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityInsertDeleteTest {

    private Context mContext;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void precondition() {
        mContext = InstrumentationRegistry.getTargetContext();
        deleteAllRecordsFromProvider();
    }

    public void deleteAllRecordsFromProvider() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListAgency.LIST_AGENCY);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListActualite.LIST_ACTUALITE);
    }

    @Test
    public void mainActivityInsertDeleteTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        5),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_parcel),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("YYHHJJ"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_id_parcel), withText("YYHHJJ"),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("YYHHJJJJ"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_id_parcel), withText("YYHHJJJJ"),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_id_parcel), withText("YYHHJJJJ"),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("YYHHJJJJJJ"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_id_parcel), withText("YYHHJJJJJJ"),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edit_description_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("ju"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab_search_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab_add_parcel),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText7.perform(longClick());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("UA649306764US"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.edit_description_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("j"), closeSoftKeyboard());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.fab_search_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction floatingActionButton5 = onView(
                allOf(withId(R.id.fab_add_parcel),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton5.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText10.perform(longClick());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("RS908182710NL"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.edit_description_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("jj"), closeSoftKeyboard());

        ViewInteraction floatingActionButton6 = onView(
                allOf(withId(R.id.fab_search_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        floatingActionButton6.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_parcel_list_management),
                        childAtPosition(
                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, longClick()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.fab_delete_colis), withText("Supprimer"), withContentDescription("Delete Button"),
                        childAtPosition(
                                allOf(withId(R.id.relative_delete_layout),
                                        childAtPosition(
                                                withId(R.id.constraint_detail_colis_layout),
                                                8)),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction floatingActionButton7 = onView(
                allOf(withId(R.id.fab_add_parcel),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton7.perform(click());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(longClick());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.edit_id_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("EZ036524985US"), closeSoftKeyboard());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.edit_description_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("m"), closeSoftKeyboard());

        ViewInteraction floatingActionButton8 = onView(
                allOf(withId(R.id.fab_search_parcel),
                        childAtPosition(
                                allOf(withId(R.id.layout_search),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        floatingActionButton8.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recycler_parcel_list_management),
                        childAtPosition(
                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(2, longClick()));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.fab_delete_colis), withText("Supprimer"), withContentDescription("Delete Button"),
                        childAtPosition(
                                allOf(withId(R.id.relative_delete_layout),
                                        childAtPosition(
                                                withId(R.id.constraint_detail_colis_layout),
                                                8)),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());

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
