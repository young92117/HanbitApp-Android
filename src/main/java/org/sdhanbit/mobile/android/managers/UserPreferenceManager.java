package org.sdhanbit.mobile.android.managers;

import android.content.SharedPreferences;
import com.google.inject.Inject;
import com.google.inject.Provider;
import roboguice.inject.SharedPreferencesProvider;

public class UserPreferenceManager {

    private static final String TAG = UserPreferenceManager.class.getName();

    private static final String PREF_FEED_REFRESH_FREQUENCY_KEY = "FeedRefreshFrequency";

    private SharedPreferences sharedPreferences;

    @Inject
    public UserPreferenceManager(Provider<SharedPreferences> sharedPreferencesProvider) {
        sharedPreferences = sharedPreferencesProvider.get();
    }

    public int getFeedRefreshFrequencyInMinutes() {
        // default to one minute
        return sharedPreferences.getInt(PREF_FEED_REFRESH_FREQUENCY_KEY, 1);
    }

    public void setPrefFeedRefreshFrequencyKey(int frequencyInMinutes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_FEED_REFRESH_FREQUENCY_KEY, frequencyInMinutes);
        editor.commit();
    }
}
