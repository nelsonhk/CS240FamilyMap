package DataAccessTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.*;
import java.sql.Connection;

public class PersonDaoTest {
    private PersonDao personDao;
    private Person testPerson;
    private Person testPerson1;
    private Person testPerson2;
    private Database db;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        testPerson = new Person("TestPerson", "nelsonhk", "Hannah",
                "Nelson", "f", "motherID", "fatherID", "TestPerson1");
        testPerson1 = new Person("TestPerson1", "nelsonhk", "Sebastian",
                "Osorio", "m", "motherID", "fatherID", "TestPerson1");
        testPerson2 = new Person("TestPerson2", "lizfusano", "Liz",
                "Fusano", "f", "motherID", "fatherID", "TestPerson3");
        Connection conn = db.getConnection();
        db.clearTables();
        personDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Successful Person Creation")
    public void createPersonSuccessTest() throws DataAccessException {
        Person actualPerson;
        personDao.createPerson(testPerson);
        actualPerson = personDao.findByID(testPerson.getPersonID());
        Assertions.assertEquals(testPerson, actualPerson);
    }

    @Test
    @DisplayName("Failed Person Creation")
    public void createPersonFailTest() throws DataAccessException {
        personDao.createPerson(testPerson);
        Assertions.assertThrows(DataAccessException.class, ()-> personDao.createPerson(testPerson));
    }

    @Test
    @DisplayName("Successful Find by PersonID")
    public void findByIDSuccessTest() throws DataAccessException {
        Person actualPerson;
        personDao.createPerson(testPerson);
        actualPerson = personDao.findByID(testPerson.getPersonID());
        Assertions.assertEquals(testPerson, actualPerson);
    }

    @Test
    @DisplayName("Failed Find by PersonID")
    public void findByIDFailTest() throws DataAccessException {
        Assertions.assertNull(personDao.findByID(testPerson.getPersonID()));
    }

    @Test
    @DisplayName("Successful Find Family by ID (1)")
    public void findFamilyByIDSuccessTest1() throws DataAccessException {
        personDao.createPerson(testPerson);
        personDao.createPerson(testPerson1);

        Person[] nelsonhkPersons = personDao.findFamilyByID("nelsonhk");

        Assertions.assertEquals(nelsonhkPersons.length, 2);
    }

    @Test
    @DisplayName("Successful Find Family by ID (2)")
    public void findFamilyByIDSuccessTest2() throws DataAccessException {
        personDao.createPerson(testPerson2);

        Person[] lizfusanoPersons = personDao.findFamilyByID("lizfusano");

        Assertions.assertEquals(lizfusanoPersons.length, 1);
    }

    @Test
    @DisplayName("Success Clear Person")
    public void clearPersonSuccessTest() throws DataAccessException {
        personDao.createPerson(testPerson);
        personDao.clear();
        Assertions.assertNull(personDao.findByID(testPerson.getPersonID()));
    }

    @Test
    @DisplayName("Success Clear Persons for Username")
    public void clearPersonsForUsernameSuccessTest() throws DataAccessException {
        personDao.createPerson(testPerson);
        personDao.createPerson(testPerson1);
        personDao.createPerson(testPerson2);

        personDao.clearForUsername("nelsonhk");

        Person[] nelsonhkPersons = personDao.findFamilyByID("nelsonhk");
        Person[] lizfusanoPersons = personDao.findFamilyByID("lizfusano");

        Assertions.assertEquals(nelsonhkPersons.length, 0);
        Assertions.assertNotEquals(lizfusanoPersons.length, 0);
    }

}
