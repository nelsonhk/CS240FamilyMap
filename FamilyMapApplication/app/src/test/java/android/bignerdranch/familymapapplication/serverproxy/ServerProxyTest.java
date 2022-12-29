package android.bignerdranch.familymapapplication.serverproxy;

import android.bignerdranch.familymapapplication.data.ServerProxy.ServerProxy;

import org.junit.*;
import org.junit.Assert;

import java.util.UUID;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class ServerProxyTest {

    // Hannah's home wifi IP address
    private String serverHost = "localhost";
    private String serverPort = "8080";
    private String[] args = new String[]{serverHost, serverPort};
    private ServerProxy mServerProxy = new ServerProxy(args);
    LoginRequest successLoginRequest;
    LoginRequest failLoginRequest;
    RegisterRequest successRegisterRequest;
    RegisterRequest failRegisterRequest;
    String authToken;

    @Before
    public void setup() {
        successLoginRequest = new LoginRequest("sheila", "parker");

        // logging in this user will generate an error = "Error: User credentials invalid."
        failLoginRequest = new LoginRequest("hannah", "nelson");

        UUID username = UUID.randomUUID();
        successRegisterRequest = new RegisterRequest(username.toString(),
                "osorio", "sebastian@me.com", "Sebastian", "Osorio",
                "m");

        // registering this user will generate an error - "Error: Username already being used"
        failRegisterRequest = new RegisterRequest("sebastian",
                "osorio", "sebastian@me.com", "Sebastian", "Osorio",
                "m");
    }

    @Test
    public void loginUserSuccess() {
        LoginResult loginResult = mServerProxy.login(successLoginRequest);
        authToken = loginResult.getAuthtoken();
        Assert.assertTrue(loginResult.isSuccess());

    }

    @Test
    public void loginUserFail() {
        LoginResult loginResult = mServerProxy.login(failLoginRequest);
        Assert.assertEquals(null, loginResult);
    }

    @Test
    public void registerUserSuccess() {
        RegisterResult registerResult = mServerProxy.register(successRegisterRequest);
        Assert.assertTrue(registerResult.isSuccess());
    }

    @Test
    public void registerUserFail() {
        RegisterResult registerResult = mServerProxy.register(failRegisterRequest);
        Assert.assertEquals(null, registerResult);
    }

    @Test
    public void getPeopleSuccess() {
        LoginResult loginResult = mServerProxy.login(successLoginRequest);
        authToken = loginResult.getAuthtoken();

        PersonsResult personsResult = mServerProxy.getPersons(authToken);
        Assert.assertTrue(personsResult.isSuccess());
    }

    @Test
    public void getPeopleFail() {
        PersonsResult personsResult = mServerProxy.getPersons(null);
        Assert.assertEquals(null, personsResult);
    }

    @Test
    public void getEventsSuccess() {
        LoginResult loginResult = mServerProxy.login(successLoginRequest);
        authToken = loginResult.getAuthtoken();

        EventsResult eventsResult = mServerProxy.getEvents(authToken);
        Assert.assertTrue(eventsResult.isSuccess());
    }

    @Test
    public void getEventsFail() {
        EventsResult eventsResult = mServerProxy.getEvents(null);
        Assert.assertEquals(null, eventsResult);
    }

}
