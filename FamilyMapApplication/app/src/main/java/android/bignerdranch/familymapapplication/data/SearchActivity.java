package android.bignerdranch.familymapapplication.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bignerdranch.familymapapplication.R;
import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENT_ITEM_GROUP_POSITION = 0;
    private static final int PERSON_ITEM_GROUP_POSITION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                DataCache dataCache = DataCache.getInstance();

                query = query.toLowerCase();

                List<Person> matchPersons = dataCache.searchPersons(query);
                List<Event> matchEvents = dataCache.searchEvents(query);

                SearchActivityAdapter adapter = new SearchActivityAdapter(matchPersons, matchEvents);
                recyclerView.setAdapter(adapter);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityViewHolder> {
        private final List<Person> matchPersons;
        private final List<Event> matchEvents;

        SearchActivityAdapter(List<Person> matchPersons, List<Event> matchEvents) {
            this.matchPersons = matchPersons;
            this.matchEvents = matchEvents;
        }

        @Override
        public int getItemViewType(int position) {
            return position < matchPersons.size() ? PERSON_ITEM_GROUP_POSITION : EVENT_ITEM_GROUP_POSITION;
        }

        @NonNull
        @Override
        public SearchActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_GROUP_POSITION) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new SearchActivityViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchActivityViewHolder holder, int position) {
            if(position < matchPersons.size()) {
                holder.bind(matchPersons.get(position));
            } else {
                holder.bind(matchEvents.get(position - matchPersons.size()));
            }
        }

        @Override
        public int getItemCount() {
            return matchPersons.size() + matchEvents.size();
        }
    }

    private class SearchActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icon;
        private TextView personName;
        private TextView eventDetails;

        private int viewType;
        private Person person;
        private Event event;

        SearchActivityViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_GROUP_POSITION) {
                icon = view.findViewById(R.id.personItemIconImageView);
                personName = view.findViewById(R.id.personNameTextView);
                eventDetails = null;
            } else {
                icon = view.findViewById(R.id.eventItemIconImageView);
                eventDetails = view.findViewById(R.id.eventDetailsTextView);
                personName = view.findViewById(R.id.eventPersonNameTextView);
            }
        }

        private void bind(Person person) {
            this.person = person;
            Drawable iconDrawable;
            String genderPersonOfEvent = person.getGender();

            // Change the gender icon based on the event's person's gender
            if (genderPersonOfEvent.equals("m")) {
                iconDrawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            } else {
                iconDrawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            }

            icon.setImageDrawable(iconDrawable);
            String personNameString = person.getFirstName() + " " + person.getLastName();
            personName.setText(personNameString);
        }

        private void bind(Event event) {
            this.event = event;
            DataCache dataCache = DataCache.getInstance();
            Map<String, Person> personMap = dataCache.getPeople();

            Drawable iconDrawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.white).sizeDp(40);

            icon.setImageDrawable(iconDrawable);
            String eventDetailsString = event.getEventType() + ": " + event.getCity() + ", "
                    + event.getCountry() + " (" + event.getYear() + ") ";
            eventDetails.setText(eventDetailsString);

            String personNameString = personMap.get(event.getPersonID()).getFirstName() + " " +
                    personMap.get(event.getPersonID()).getLastName();
            personName.setText(personNameString);
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON_ITEM_GROUP_POSITION) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                startActivity(intent);

            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EVENT_ID_KEY, event.getEventID());
                startActivity(intent);

            }
        }
    }

}

