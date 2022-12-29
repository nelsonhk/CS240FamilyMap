package android.bignerdranch.familymapapplication.data;

import android.bignerdranch.familymapapplication.R;
import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    static final String PERSON_ID_KEY = "PersonIDKey";
    Person person;
    DataCache mDataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Set the Person Info based on personID passed in from the previous activity (via the intent)
        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_ID_KEY);
        person = mDataCache.getPeople().get(personID);

        TextView firstNameTextView = findViewById(R.id.firstNameDisplay);
        firstNameTextView.setText(person.getFirstName());

        TextView lastNameTextView = findViewById(R.id.lastNameDisplay);
        lastNameTextView.setText(person.getLastName());

        TextView genderTextVew = findViewById(R.id.genderDisplay);
        String gender = "";
        if (person.getGender().equals("f")) {
            gender = "Female";
        } else {
            gender = "Male";
        }
        genderTextVew.setText(gender);

        // Set up the Expandable Views for Events and Family members
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        List<Event> eventsForUser = mDataCache.getEventsForPerson().get(personID);
        Map<String, Event> filteredEvents = mDataCache.filterEvents(mDataCache.getEvents());
        List<Event> eventsDisplayedForUser = new ArrayList<>();
        for (Event event : eventsForUser) {
            if (filteredEvents.containsKey(event.getEventID())) {
                eventsDisplayedForUser.add(event);
            }
        }
        List<Person> familyMembersForUser = mDataCache.getFamilyForPerson().get(personID);

        expandableListView.setAdapter(new ExpandableListAdapter(eventsDisplayedForUser, familyMembersForUser));

    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_ITEM_GROUP_POSITION = 0;
        private static final int FAMILY_MEMBER_ITEM_GROUP_POSITION = 1;

        private List<Event> eventsDisplayedForUser;
        private List<Person> familyMembersForUser;

        ExpandableListAdapter(List<Event> eventsDisplayedForUser, List<Person> familyMembersForUser) {
            this.eventsDisplayedForUser = eventsDisplayedForUser;
            this.familyMembersForUser = familyMembersForUser;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_ITEM_GROUP_POSITION:
                    return eventsDisplayedForUser.size();
                case FAMILY_MEMBER_ITEM_GROUP_POSITION:
                    return familyMembersForUser.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_ITEM_GROUP_POSITION:
                    return getString(R.string.eventsForUser);
                case FAMILY_MEMBER_ITEM_GROUP_POSITION:
                    return getString(R.string.familyMembersForUser);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_ITEM_GROUP_POSITION:
                    return eventsDisplayedForUser.get(childPosition);
                case FAMILY_MEMBER_ITEM_GROUP_POSITION:
                    return familyMembersForUser.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_groups, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_ITEM_GROUP_POSITION:
                    titleView.setText(R.string.eventsForUser);
                    break;
                case FAMILY_MEMBER_ITEM_GROUP_POSITION:
                    titleView.setText(R.string.familyMembersForUser);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_ITEM_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventsForUserView(itemView, childPosition);
                    break;
                case FAMILY_MEMBER_ITEM_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializeFamilyMembersView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventsForUserView(View eventItemView, final int childPosition) {

            ImageView markerIconView = eventItemView.findViewById(R.id.eventItemIconImageView);
            Drawable eventIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.white).sizeDp(40);
            markerIconView.setImageDrawable(eventIcon);
            markerIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID_KEY, eventsDisplayedForUser.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });

            TextView eventDetailsView = eventItemView.findViewById(R.id.eventDetailsTextView);
            String text = eventsDisplayedForUser.get(childPosition).getEventType() + ": "
                    + eventsDisplayedForUser.get(childPosition).getCity() + ", "
                    + eventsDisplayedForUser.get(childPosition).getCountry() + " ("
                    + eventsDisplayedForUser.get(childPosition).getYear() + ") ";
            eventDetailsView.setText(text);
            eventDetailsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID_KEY, eventsDisplayedForUser.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });

            TextView eventPersonView = eventItemView.findViewById(R.id.eventPersonNameTextView);
            String fullName = person.getFirstName() + " " + person.getLastName();
            eventPersonView.setText(fullName);
            eventPersonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID_KEY, eventsDisplayedForUser.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializeFamilyMembersView(View personItemView, final int childPosition) {
            ImageView personIconView = personItemView.findViewById(R.id.personItemIconImageView);
            Drawable personIcon;
            if (familyMembersForUser.get(childPosition).getGender().equals("m")) {
                personIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            } else {
                personIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            }
            personIconView.setImageDrawable(personIcon);

            personIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID_KEY,
                            familyMembersForUser.get(childPosition).getPersonID());
                    startActivity(intent);
                }

            });

            // Name of family member
            TextView familyMemberNameView = personItemView.findViewById(R.id.personNameTextView);
            String fullName = familyMembersForUser.get(childPosition).getFirstName() + " " +
                    familyMembersForUser.get(childPosition).getLastName();
            familyMemberNameView.setText(fullName);

            familyMemberNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID_KEY,
                            familyMembersForUser.get(childPosition).getPersonID());
                    startActivity(intent);
                }

            });

            // Relationship to current person
            TextView relationshipView = personItemView.findViewById(R.id.relationToPersonTextView);
            String relationship;

            Person person1 = familyMembersForUser.get(childPosition);
            String person1ID = familyMembersForUser.get(childPosition).getPersonID();

            if (person.getMotherID() != null && person.getMotherID().equals(person1ID)) {
                relationship = "Mother";
            } else if (person.getFatherID() != null && person.getFatherID().equals(person1ID)) {
                relationship = "Father";
            } else if (person.getSpouseID() != null && person.getSpouseID().equals(person1ID)) {
                relationship = "Spouse";
            } else if ((person1.getMotherID() != null && person1.getMotherID().equals(person.getPersonID())) ||
                    (person1.getFatherID() != null && person1.getFatherID().equals(person.getPersonID()))) {
                relationship = "Child";
            } else {
                relationship = "Error: Unknown relation";
            }

            relationshipView.setText(relationship);

            relationshipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID_KEY,
                            familyMembersForUser.get(childPosition).getPersonID());
                    startActivity(intent);
                }

            });

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    // activate the up button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}