package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import DataAccess.PersonDao;
import Model.Event;
import Model.Person;
import ProvidedJSONObjects.*;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class GenerateFamilyTree {

    static List<Location> locationList = loadLocations();
    static List<String> femaleNameList = loadFemaleNames();
    static List<String> maleNameList = loadMaleNames();
    static List<String> surnameList = loadSurnames();


    public static List<Location> loadLocations() {
        List<Location> locationList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            Reader reader =
                    new FileReader("/Users/hannahnelson/IdeaProjects/FamilyMapServerStudent-master/json/locations.json");
            LocationData locationData = (LocationData)gson.fromJson(reader, LocationData.class);
            for (Location location : locationData.getData()) {
                locationList.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return locationList;
    }

    public static List<String> loadFemaleNames() {
        List<String> femaleNameList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            Reader reader =
                    new FileReader("/Users/hannahnelson/IdeaProjects/FamilyMapServerStudent-master/json/fnames.json");
            FemaleNameData femaleNameData = (FemaleNameData) gson.fromJson(reader, FemaleNameData.class);
            for (String name : femaleNameData.getData()) {
                femaleNameList.add(name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return femaleNameList;
    }

    public static List<String> loadMaleNames() {
       List<String> maleNameList = new ArrayList<>();
       Gson gson = new Gson();
        try {
            Reader reader =
                    new FileReader("/Users/hannahnelson/IdeaProjects/FamilyMapServerStudent-master/json/mnames.json");
            MaleNameData maleNameData = (MaleNameData) gson.fromJson(reader, MaleNameData.class);
            for (String name : maleNameData.getData()) {
                maleNameList.add(name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return maleNameList;
    }

    public static List<String> loadSurnames() {
        List<String> surnameList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            Reader reader =
                    new FileReader("/Users/hannahnelson/IdeaProjects/FamilyMapServerStudent-master/json/snames.json");
            SurnameData surnameData = (SurnameData) gson.fromJson(reader, SurnameData.class);
            for (String name : surnameData.getData()) {
                surnameList.add(name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return surnameList;
    }

    public Person generateFamilyTree(Person currPerson, int numGenerations, int birthYear) {

        //create mother and father person - with IDs and associated username
        String motherID = String.valueOf(UUID.randomUUID());
        String fatherID = String.valueOf(UUID.randomUUID());

        List<String> motherName = getRandomName("f");
        List<String> fatherName = getRandomName("m");

        Person mother = new Person(
                motherID,
                currPerson.getAssociatedUsername(),
                motherName.get(0),
                motherName.get(1),
                "f",
                null,
                null,
                fatherID
                );

        Person father = new Person(
                fatherID,
                currPerson.getAssociatedUsername(),
                fatherName.get(0),
                fatherName.get(1),
                "m",
                null,
                null,
                motherID
        );

        //set MID and FID for currPerson (will be the Person IDs for mother and father)
        if (numGenerations > 0) {
            currPerson.setMotherID(motherID);
            currPerson.setFatherID(fatherID);
        }

        //insert currPerson to the DB (their Person object is now complete)
        Database db = new Database();
        try {
            db.openConnection();
            PersonDao personDao = new PersonDao(db.getConnection());
            personDao.createPerson(currPerson);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        //create all the mother/father information and events - base it off of the currPerson's birth year
        GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
        List<Event> parentBirthEvents = generateFamilyTree.createParentBirths(currPerson, mother, father, birthYear);
        List<Event> parentMarriageEvents = generateFamilyTree.createParentMarriage(currPerson, mother, father, birthYear);
        List<Event> parentDeathEvents = generateFamilyTree.createParentDeaths(currPerson, mother, father,
                parentBirthEvents.get(0).getYear());

        //insert parents' to DB
        try {
            db.openConnection();
            EventDao eventDao = new EventDao(db.getConnection());
            for (Event birth : parentBirthEvents) {
                eventDao.createEvent(birth);
            }
            for (Event marriage : parentMarriageEvents) {
                eventDao.createEvent(marriage);
            }
            for (Event death : parentDeathEvents) {
                eventDao.createEvent(death);
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        //call recursively on the parents
        if (numGenerations > 0) {
            generateFamilyTree(mother, numGenerations - 1, parentBirthEvents.get(0).getYear());
            generateFamilyTree(father, numGenerations - 1, parentBirthEvents.get(1).getYear());
        } else {
            // don't forget to save the last generation of people in the DB
            try {
                db.openConnection();
                PersonDao personDao = new PersonDao(db.getConnection());
                personDao.createPerson(mother);
                personDao.createPerson(father);
                db.closeConnection(true);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    List<Event> createParentBirths(Person currPerson, Person mother, Person father, int birthYear) {
        List<Event> parentBirthEvents = new ArrayList<Event>() {};

        GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
        String eventType = "Birth";
        String associatedUsername = currPerson.getAssociatedUsername();
        int parentBirthYear = birthYear - 45;

        Location motherBirthLocation = generateFamilyTree.getRandomLocation();
        String motherBirthEventID = String.valueOf(UUID.randomUUID());

        Event motherBirth = new Event(
                motherBirthEventID,
                associatedUsername,
                mother.getPersonID(),
                motherBirthLocation.getLatitude(),
                motherBirthLocation.getLongitude(),
                motherBirthLocation.getCountry(),
                motherBirthLocation.getCity(),
                eventType,
                parentBirthYear
        );

        Location fatherBirthLocation = generateFamilyTree.getRandomLocation();
        String fatherBirthEventID = String.valueOf(UUID.randomUUID());

        Event fatherBirth = new Event(
                fatherBirthEventID,
                associatedUsername,
                father.getPersonID(),
                fatherBirthLocation.getLatitude(),
                fatherBirthLocation.getLongitude(),
                fatherBirthLocation.getCountry(),
                fatherBirthLocation.getCity(),
                eventType,
                parentBirthYear
        );

        parentBirthEvents.add(motherBirth);
        parentBirthEvents.add(fatherBirth);

        return parentBirthEvents;

    }

    List<Event> createParentMarriage(Person currPerson, Person mother, Person father, int birthYear) {
        List<Event> parentMarriageEvents = new ArrayList<Event>() {};

        GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
        String eventType = "Marriage";
        String associatedUsername = currPerson.getAssociatedUsername();
        int marriageYear = birthYear - 21;

        Location marriageLocation = generateFamilyTree.getRandomLocation();
        String motherMarriageEventID = String.valueOf(UUID.randomUUID());
        String fatherMarriageEventID = String.valueOf(UUID.randomUUID());

        Event motherMarriage = new Event(
                motherMarriageEventID,
                associatedUsername,
                mother.getPersonID(),
                marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),
                marriageLocation.getCountry(),
                marriageLocation.getCity(),
                eventType,
                marriageYear
        );

        Event fatherMarriage = new Event(
                fatherMarriageEventID,
                associatedUsername,
                father.getPersonID(),
                marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),
                marriageLocation.getCountry(),
                marriageLocation.getCity(),
                eventType,
                marriageYear
        );

        parentMarriageEvents.add(motherMarriage);
        parentMarriageEvents.add(fatherMarriage);

        return parentMarriageEvents;

    }

    private List<Event> createParentDeaths(Person currPerson, Person mother, Person father, int parentBirthYear) {
        List<Event> parentDeathEvents = new ArrayList<Event>() {};

        GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
        String eventType = "Death";
        String associatedUsername = currPerson.getAssociatedUsername();
        int parentDeathYear = parentBirthYear + 96;

        Location motherDeathLocation = generateFamilyTree.getRandomLocation();
        String motherDeathEventID = String.valueOf(UUID.randomUUID());

        Event motherDeath = new Event(
                motherDeathEventID,
                associatedUsername,
                mother.getPersonID(),
                motherDeathLocation.getLatitude(),
                motherDeathLocation.getLongitude(),
                motherDeathLocation.getCountry(),
                motherDeathLocation.getCity(),
                eventType,
                parentDeathYear
        );

        Location fatherBirthLocation = generateFamilyTree.getRandomLocation();
        String fatherBirthEventID = String.valueOf(UUID.randomUUID());

        Event fatherDeath = new Event(
                fatherBirthEventID,
                associatedUsername,
                father.getPersonID(),
                fatherBirthLocation.getLatitude(),
                fatherBirthLocation.getLongitude(),
                fatherBirthLocation.getCountry(),
                fatherBirthLocation.getCity(),
                eventType,
                parentDeathYear
        );

        parentDeathEvents.add(motherDeath);
        parentDeathEvents.add(fatherDeath);
        return parentDeathEvents;

    }

    public Location getRandomLocation() {
        Random rand = new Random();

        int randomIndex = rand.nextInt(locationList.size());
        float latitude = locationList.get(randomIndex).getLatitude();
        float longitude = locationList.get(randomIndex).getLongitude();
        String city = locationList.get(randomIndex).getCity();
        String country = locationList.get(randomIndex).getCountry();

        Location eventLocation = new Location(country, city, latitude, longitude);

        return eventLocation;
    }

    public List<String> getRandomName(String gender) {
        Random rand = new Random();
        String firstName = "";
        String lastName = "";

        if (gender == "f") {
            int randomIndex = rand.nextInt(femaleNameList.size());
            firstName = femaleNameList.get(randomIndex);
        } else {
            int randomIndex = rand.nextInt(maleNameList.size());
            firstName = maleNameList.get(randomIndex);
        }
        int randomIndex = rand.nextInt(surnameList.size());
        lastName = surnameList.get(randomIndex);

        List<String> name = new ArrayList<>();
        name.add(firstName);
        name.add(lastName);

        return name;
    }

}
