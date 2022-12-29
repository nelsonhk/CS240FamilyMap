package android.bignerdranch.familymapapplication.data;

import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.bignerdranch.familymapapplication.R;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {}
    DataCache mDataCache = DataCache.getInstance();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        /**
         * Note to the reader: I considered putting the preferences into a map with a key (boolean
         * setting found in the DataCache) and a value (the SwitchPreferenceCompat object)
         * so I could better utilize for loops for the setChecked() and setOnPreferenceClickListener()
         * functions but ultimately, for readability's sake, it didn't make sense. Hence the repetitive
         * code I have written here.
         */

        // Get all the SwitchPreferenceCompat preferences
        SwitchPreferenceCompat lifeStoryLines = (SwitchPreferenceCompat) findPreference(getString(R.string.life_story_lines_key));
        SwitchPreferenceCompat familyTreeLines = (SwitchPreferenceCompat) findPreference(getString(R.string.family_tree_lines_key));
        SwitchPreferenceCompat spouseLines = (SwitchPreferenceCompat) findPreference(getString(R.string.spouse_lines_key));
        SwitchPreferenceCompat fathersSide = (SwitchPreferenceCompat) findPreference(getString(R.string.fathers_side_key));
        SwitchPreferenceCompat mothersSide = (SwitchPreferenceCompat) findPreference(getString(R.string.mothers_side_key));
        SwitchPreferenceCompat maleEvents = (SwitchPreferenceCompat) findPreference(getString(R.string.male_events_key));
        SwitchPreferenceCompat femaleEvents = (SwitchPreferenceCompat) findPreference(getString(R.string.female_events_key));

        // Set all the preferences based on persistent DataCache booleans (initially, all will be set to true)
        lifeStoryLines.setChecked(mDataCache.isShowLifeStoryLines());
        familyTreeLines.setChecked(mDataCache.isShowFamilyTreeLines());
        spouseLines.setChecked(mDataCache.isShowSpouseLines());
        fathersSide.setChecked(mDataCache.isShowFathersSide());
        mothersSide.setChecked(mDataCache.isShowMothersSide());
        maleEvents.setChecked(mDataCache.isShowMaleEvents());
        femaleEvents.setChecked(mDataCache.isShowFemaleEvents());

        // Create on click listeners for all of the preferences; update DataCache booleans when preference is changed
        lifeStoryLines.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowLifeStoryLines(lifeStoryLines.isChecked());
                return false;
            }
        });
        familyTreeLines.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowFamilyTreeLines(familyTreeLines.isChecked());
                return false;
            }
        });
        spouseLines.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowSpouseLines(spouseLines.isChecked());
                return false;
            }
        });
        fathersSide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowFathersSide(fathersSide.isChecked());
                return false;
            }
        });
        mothersSide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowMothersSide(mothersSide.isChecked());
                return false;
            }
        });
        maleEvents.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowMaleEvents(maleEvents.isChecked());
                return false;
            }
        });
        femaleEvents.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDataCache.setShowFemaleEvents(femaleEvents.isChecked());
                return false;
            }
        });

        // Get the logout preference
        // Create on click listener on logout button to clear the task and return to the main activity
        Preference logoutButton = findPreference(getString(R.string.logout_preference_key));
        logoutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                    Intent intent= new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                return false;
            }
        });

    }

}