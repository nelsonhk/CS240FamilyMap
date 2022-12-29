package android.bignerdranch.familymapapplication.data;

import static android.bignerdranch.familymapapplication.data.EventActivity.EVENT_ID_KEY;

import android.bignerdranch.familymapapplication.R;
import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    DataCache mDataCache = DataCache.getInstance();
    Drawable genderIcon;
    GoogleMap.OnMarkerClickListener listener;
    final Map<String, Event> eventMap = mDataCache.getEvents();
    Map<String, Float> eventTypes = new HashMap<>();
    TextView textView;
    Event markerEvent;
    List<Polyline> polylines = new ArrayList<>();

    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ImageView genderImageView = view.findViewById(R.id.genderImageView);

        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                colorRes(R.color.android_icon).sizeDp(40);

        genderImageView.setImageDrawable(genderIcon);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getActivity() instanceof EventActivity) {
            Intent intent = getActivity().getIntent();
            String eventID = intent.getStringExtra(EVENT_ID_KEY);

            Map<String, Marker> markerMap = mDataCache.getMarkerMap();
            Marker eventMarker = markerMap.get(eventID);
            eventActivityHelper(view, genderImageView, eventMarker, map);
        }

        listener = new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markerEventID = (String) marker.getTag();
                markerEvent = eventMap.get(markerEventID);

                assert markerEvent != null;
                Person person = mDataCache.getPeople().get(markerEvent.getPersonID());
                String genderPersonOfEvent = person.getGender();

                // Change the gender icon based on the event's person's gender
                if (genderPersonOfEvent.equals("m")) {
                    genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                } else {
                    genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                }

                // change the icon and text that is displayed to the event details for the clicked event
                genderImageView.setImageDrawable(genderIcon);
                textView = view.findViewById(R.id.mapTextView);
                String eventText = person.getFirstName() + " " + person.getLastName() + "\n" +
                        markerEvent.getEventType() + ": " + markerEvent.getCity() + ", " +
                        markerEvent.getCountry() + " (" + markerEvent.getYear() + ")";
                textView.setText(eventText);

                // set onClick method for event text - opens Person Activity for the person
                // associated with the event displayed
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PersonActivity.class);
                        intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                        startActivity(intent);
                    }

                });

                drawEventLines(person);

                return true;
            }
        };

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapLoadedCallback(this);

        // Clear the map of all previous markers
        map.clear();

        eventTypes.put("BIRTH", mDataCache.getBirthColor());
        eventTypes.put("MARRIAGE", mDataCache.getMarriageColor());
        eventTypes.put("DEATH", mDataCache.getDeathColor());

        for (Event event : eventMap.values()) {
            float markerColor;
            if (eventTypes.containsKey(event.getEventType().toUpperCase())) {
                markerColor = eventTypes.get(event.getEventType().toUpperCase());
            } else {
                markerColor = getRandomNumberUsingNextInt();
                eventTypes.put(event.getEventType().toUpperCase(), markerColor);
            }

            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(),
                    event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            assert marker != null;
            mDataCache.loadMarkers(event, marker);
        }

        // Set the OnMarkerClickListener
        map.setOnMarkerClickListener(listener);

        // If in Event
        if (getActivity() instanceof EventActivity) {
            // create marker for selected event and center the camera on the event
            LatLng markerLatLng = new LatLng(markerEvent.getLatitude(), markerEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(markerLatLng));

            Person person = mDataCache.getPeople().get(markerEvent.getPersonID());
            // draw the lines for the selected event according to selected settings
            drawEventLines(person);

        }

    }

    @Override
    public void onMapLoaded() {}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!(getActivity() instanceof EventActivity)) {
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
            searchMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white)
                    .actionBarSize());
            searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
            settingsMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white)
                    .actionBarSize());
            settingsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (map != null) {

            map.setOnMapLoadedCallback(this);

            // Clear the map of all previous markers
            map.clear();

            Map<String, Event> filteredEventMap = mDataCache.filterEvents(eventMap);

            for (Event event : filteredEventMap.values()) {
                float markerColor;
                if (eventTypes.containsKey(event.getEventType().toUpperCase())) {
                    markerColor = eventTypes.get(event.getEventType().toUpperCase());
                } else {
                    markerColor = getRandomNumberUsingNextInt();
                    eventTypes.put(event.getEventType().toUpperCase(), markerColor);
                }

                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(),
                        event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                assert marker != null;
                mDataCache.loadMarkers(event, marker);
            }

            // Set the OnMarkerClickListener
            map.setOnMarkerClickListener(listener);

            // If in Event
            if (getActivity() instanceof EventActivity) {
                LatLng markerLatLng = new LatLng(markerEvent.getLatitude(), markerEvent.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(markerLatLng));
            }

        }
    }

    /**
     * Helper Functions
     *
     */

    private void eventActivityHelper(View view, ImageView genderImageView, Marker marker, GoogleMap map) {
        String markerEventID = (String) marker.getTag();
        markerEvent = eventMap.get(markerEventID);

        assert markerEvent != null;
        Person person = mDataCache.getPeople().get(markerEvent.getPersonID());
        String genderPersonOfEvent = person.getGender();

        // Change the gender icon based on the event's person's gender
        if (genderPersonOfEvent.equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
        }

        // change the icon and text that is displayed to the event details for the clicked event
        genderImageView.setImageDrawable(genderIcon);
        textView = view.findViewById(R.id.mapTextView);
        String eventText = person.getFirstName() + " " + person.getLastName() + "\n" +
                markerEvent.getEventType() + ": " + markerEvent.getCity() + ", " +
                markerEvent.getCountry() + " (" + markerEvent.getYear() + ")";
        textView.setText(eventText);

        // set onClick method for event text - opens Person Activity for the person
        // associated with the event displayed
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                startActivity(intent);
            }
        });

    }

    public int getRandomNumberUsingNextInt() {
        Random random = new Random();
        return random.nextInt(360);
    }

    private List<Polyline> drawLine(Event startEvent, Event endEvent, int googleColor, float width) {
        // Create start and end points for the line
        LatLng startPoint= new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint= new LatLng(endEvent.getLatitude(), endEvent.getLongitude());
        // Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions().add(startPoint).
                add(endPoint).color((int) googleColor).width(width);
        Polyline line = map.addPolyline(options);

        List<Polyline> polylines = new ArrayList<>();
        polylines.add(line);
        return polylines;
    }

    private void familyTreeLineHelper(Person person, float lineWidth) {
        if (person.getFatherID() != null) {
            Event personEarliestEvent;
            Event fatherEarliestEvent;

            List<Event> personLifeEvents = mDataCache.getEventsForPerson().get(person.getPersonID());
            assert personLifeEvents != null;
            personEarliestEvent = personLifeEvents.get(0);

            List<Event> fatherLifeEvents = mDataCache.getEventsForPerson().get(person.getFatherID());
            assert fatherLifeEvents != null;
            fatherEarliestEvent = fatherLifeEvents.get(0);

            polylines.addAll(drawLine(personEarliestEvent, fatherEarliestEvent,
                    (int) mDataCache.getFamilyTreeLine(), lineWidth));
            Person father = mDataCache.getPeople().get(person.getFatherID());
            assert father != null;
            familyTreeLineHelper(father, lineWidth / 2);
        }

        if (person.getMotherID() != null) {
            Event personEarliestEvent;
            Event motherEarliestEvent;

            List<Event> personLifeEvents = mDataCache.getEventsForPerson().get(person.getPersonID());
            assert personLifeEvents != null;
            personEarliestEvent = personLifeEvents.get(0);

            List<Event> motherLifeEvents = mDataCache.getEventsForPerson().get(person.getMotherID());
            assert motherLifeEvents != null;
            motherEarliestEvent = motherLifeEvents.get(0);

            polylines.addAll(drawLine(personEarliestEvent, motherEarliestEvent,
                    (int) mDataCache.getFamilyTreeLine(), lineWidth));
            Person mother = mDataCache.getPeople().get(person.getFatherID());
            assert mother != null;
            familyTreeLineHelper(mother, lineWidth / 2);
        }
    }

    private void drawEventLines(Person person) {
        // add necessary marker lines here by calling drawLine method as needed below
        boolean showLifeStoryLines = mDataCache.isShowLifeStoryLines();
        boolean showFamilyTreeLines = mDataCache.isShowFamilyTreeLines();
        boolean showSpouseLines = mDataCache.isShowSpouseLines();

        for (Polyline line : polylines) {
            line.remove();
        }

        if (showSpouseLines && mDataCache.isShowFemaleEvents() && mDataCache.isShowMaleEvents()) {
            // draw spouse line
            Person spouse = mDataCache.getPeople().get(person.getSpouseID());
            List<Event> eventsForSpouse = mDataCache.getEventsForPerson().get(spouse.getPersonID());
            Event earliestEvent = null;

            assert eventsForSpouse != null;
            earliestEvent = eventsForSpouse.get(0);

            for (Event event : eventsForSpouse) {
                if (event.getYear() < earliestEvent.getYear()) {
                    earliestEvent = event;
                }
            }
            polylines.addAll(drawLine(markerEvent, earliestEvent,
                    (int) mDataCache.getSpouseLine(), mDataCache.getLineWidth()));
        }

        if (showLifeStoryLines) {
            List<Event> lifeEvents = mDataCache.getEventsForPerson().get(person.getPersonID());
            assert lifeEvents != null;
            Event startEvent = lifeEvents.get(0);
            for (Event event : lifeEvents) {
                if (!event.equals(startEvent)) {
                    polylines.addAll(drawLine(startEvent, event,
                            (int) mDataCache.getLifeStoryLine(), mDataCache.getLineWidth()));
                }
                startEvent = event;
            }
        }

        if (showFamilyTreeLines) {
            float lineWidth = mDataCache.getLineWidth();
            familyTreeLineHelper(person, lineWidth);
        }
    }

}