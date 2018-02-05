package com.sai.weatherio;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sai.weatherio.main.MainActivity;
import com.sai.weatherio.recycler_view_helpers.RecyclerViewItemCountAssertion;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.sai.weatherio.recycler_view_helpers.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by sai on 2/4/18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_StartState() {
        // Location edit text visible, fetch button not visible
        onView(withId(R.id.location_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.fetch_button)).check(matches(not(isDisplayed())));

        // Empty layout image and text visible
        onView(withId(R.id.empty_icon_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_title_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void test_StartTyping_FetchAppears() {
        // Location edit text visible, fetch button not visible
        onView(withId(R.id.location_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.fetch_button)).check(matches(not(isDisplayed())));

        // Start typing on the edit text
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Sa"));

        // Verify fetch button appeard
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));
    }

    @Test
    public void test_EmptyLocation_ErrorSnackbarAppears() {
        // Empty location being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText(" "));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_enter_valid_city_state))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_OnlyCity_ErrorSnackbarAppears() {
        // Only city being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_enter_valid_city_state))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_OnlyState_ErrorSnackbarAppears() {
        // Only state being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("NC"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_enter_valid_city_state))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_IncompleteState_ErrorSnackbarAppears() {
        // Incomplete state being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh, N"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_enter_valid_city_state))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_WrongState_ErrorSnackbarAppears() {
        // Wrong state being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh, NCS"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_enter_valid_city_state))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_ValidLocation_ForecastAppears() {
        // Valid location being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Austin, TX"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Perform click
        onView(withId(R.id.fetch_button)).perform(click());

        // Safety to ensure weather forecast is fetched
        sleep(2000);

        // Empty state not visible any more
        onView(withId(R.id.empty_icon_image_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_title_text_view)).check(matches(not(isDisplayed())));

        // Swipe container with recycler view visible
        // Empty layout image and text visible
        onView(withId(R.id.swipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.forecast_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void test_ValidLocation_ForecastAppearsWith10Results() {
        // Valid location being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh, NC"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Perform click
        onView(withId(R.id.fetch_button)).perform(click());

        // Safety to ensure weather forecast is fetched
        sleep(2000);

        // Check data count in recycler view, we need forecast for 10 days
        onView(withId(R.id.forecast_recycler_view)).check(new RecyclerViewItemCountAssertion(10));
    }

    @Test
    public void test_FirstResult_TodaysForecast() {
        // Valid location being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh, NC"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Perform click
        onView(withId(R.id.fetch_button)).perform(click());

        // Safety to ensure weather forecast is fetched
        sleep(2000);

        // Check data count in recycler view, we need forecast for 10 days
        onView(withId(R.id.forecast_recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        // Check first result is for today
        onView(withRecyclerView(R.id.forecast_recycler_view)
                .atPositionOnView(0, R.id.day_text_view))
                .check(matches(withText(mActivityRule.getActivity().getString(R.string.str_today))));

    }

    @Test
    public void test_SecondResult_TomorrowsForecast() {
        // Valid location being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("Raleigh, NC"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Perform click
        onView(withId(R.id.fetch_button)).perform(click());

        // Safety to ensure weather forecast is fetched
        sleep(2000);

        // Check data count in recycler view, we need forecast for 10 days
        onView(withId(R.id.forecast_recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        // Check first result is for today
        onView(withRecyclerView(R.id.forecast_recycler_view)
                .atPositionOnView(1, R.id.day_text_view))
                .check(matches(withText(mActivityRule.getActivity().getString(R.string.str_tomorrow))));

    }

    @Test
    public void test_WrongLocation_ErrorSnackbarAppears() {
        // Only city being typed in
        onView(withId(R.id.location_edit_text)).perform(click())
                .perform(typeText("City, St"));
        onView(withId(R.id.fetch_button)).check(matches(isDisplayed()));

        // Click on fetch button
        onView(withId(R.id.fetch_button)).perform(click());

        sleep(2000);

        // Verify snackbar with error message is displayed to the user
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mActivityRule.getActivity().getString(R.string.str_error_fetching_forecast))))
                .check(matches(isDisplayed()));
    }

}
