package android.bignerdranch.familymapapplication.data;

import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.bignerdranch.familymapapplication.data.MapFragment;
import android.bignerdranch.familymapapplication.data.ServerProxy.ServerProxy;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.bignerdranch.familymapapplication.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

public class LoginFragment extends Fragment {

    EditText editTextServerHost;
    EditText editTextServerPort;
    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmailAddress;

    Button signInButton;
    Button registerButton;

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            signInButton.setEnabled(!editTextServerHost.getText().toString().equals("") &&
                    !editTextServerPort.getText().toString().equals("") &&
                    !editTextUserName.getText().toString().equals("") &&
                    !editTextPassword.getText().toString().equals(""));

            registerButton.setEnabled(!editTextServerHost.getText().toString().equals("") &&
                    !editTextServerPort.getText().toString().equals("") &&
                    !editTextUserName.getText().toString().equals("") &&
                    !editTextPassword.getText().toString().equals("") &&
                    !editTextFirstName.getText().toString().equals("") &&
                    !editTextLastName.getText().toString().equals("") &&
                    !editTextEmailAddress.getText().toString().equals(""));
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private static final String IS_SUCCESS_KEY = "IsSuccessKey";
    private static final String FIRST_NAME_KEY = "FirstName";
    private static final String LAST_NAME_KEY = "LastName";
    String gender = "f";
    DataCache mDataCache = DataCache.getInstance();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextServerHost = view.findViewById(R.id.serverHostField);
        editTextServerHost.addTextChangedListener(mTextWatcher);

        editTextServerPort = view.findViewById(R.id.serverPortField);
        editTextServerPort.addTextChangedListener(mTextWatcher);

        editTextUserName = view.findViewById(R.id.userNameField);
        editTextUserName.addTextChangedListener(mTextWatcher);

        editTextPassword = view.findViewById(R.id.passwordField);
        editTextPassword.addTextChangedListener(mTextWatcher);

        editTextFirstName = view.findViewById(R.id.firstNameField);
        editTextFirstName.addTextChangedListener(mTextWatcher);

        editTextLastName = view.findViewById(R.id.lastNameField);
        editTextLastName.addTextChangedListener(mTextWatcher);

        editTextEmailAddress = view.findViewById(R.id.emailAddressField);
        editTextEmailAddress.addTextChangedListener(mTextWatcher);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioSex);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioGenderButton = (RadioButton) view.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.maleRadio:
                        gender = "m";
                        break;
                    case R.id.femaleRadio:
                        gender = "f";
                        break;
                }
            }
        });

        signInButton = view.findViewById(R.id.loginButton);
        signInButton.setEnabled(false);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] args = new String[]{editTextServerHost.getText().toString(),
                        editTextServerPort.getText().toString()};

                LoginRequest loginRequest = new LoginRequest(editTextUserName.getText().toString(),
                        editTextPassword.getText().toString());

                Handler loginHandler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        Bundle bundle = msg.getData();
                        boolean isSuccess = bundle.getBoolean(IS_SUCCESS_KEY, false);
                        String firstName = bundle.getString(FIRST_NAME_KEY, "");
                        String lastName = bundle.getString(LAST_NAME_KEY, "");
                        String toast = "Success! Signing in " + firstName + " " + lastName;
                        if (isSuccess) {
                            Toast.makeText(view.getContext(), toast, Toast.LENGTH_LONG).show();

                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.MainActivity, new MapFragment())
                                    .commit();

                        } else {
                            Toast.makeText(view.getContext(), "Error Signing In", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                LoginTask loginTask = new LoginTask(loginHandler, loginRequest, args);

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(loginTask);
            }
        });

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] args = new String[]{editTextServerHost.getText().toString(),
                        editTextServerPort.getText().toString()};

                RegisterRequest registerRequest = new RegisterRequest(
                        editTextUserName.getText().toString(),
                        editTextPassword.getText().toString(),
                        editTextEmailAddress.getText().toString(),
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        gender
                );

                Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        Bundle bundle = msg.getData();
                        boolean isSuccess = bundle.getBoolean(IS_SUCCESS_KEY, false);
                        if (isSuccess) {
                            Toast.makeText(view.getContext(), "Success!", Toast.LENGTH_LONG).show();

                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.MainActivity, new MapFragment())
                                    .commit();

                        } else {
                            Toast.makeText(view.getContext(), "Error Registering", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                RegisterTask registerTask = new RegisterTask(registerHandler, registerRequest, args);

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(registerTask);
            }
        });

        signInButton.setActivated(false);
        registerButton.setActivated(false);

        return view;

    }


    private class LoginTask implements Runnable {

        private final Handler messageHandler;
        LoginRequest loginRequest;
        ServerProxy mServerProxy;

        LoginTask(Handler loginHandler, LoginRequest loginRequest, String[] args) {
            this.messageHandler = loginHandler;
            this.loginRequest = loginRequest;
            mServerProxy = new ServerProxy(args);
        }

        @Override
        public void run() {

            LoginResult loginResult = mServerProxy.login(loginRequest);
            boolean loginSuccess = false;

            // cache events and persons data here if successful login
            if (loginResult != null) {

                Event[] events = mServerProxy.getEvents(loginResult.getAuthtoken()).getData();
                mDataCache.loadEvents(events);

                Person[] persons = mServerProxy.getPersons(loginResult.getAuthtoken()).getData();
                mDataCache.loadPeople(persons);

                for (Person person : persons) {
                    mDataCache.loadEventsForPerson(person, events);
                    mDataCache.loadFamilyForPerson(person, persons);
                }

                // cache the father's side, mother's side, and filter for gender for the logged in user only
                mDataCache.loadFamilySides(loginResult.getPersonID());
                mDataCache.loadByGender();

                String firstName = mDataCache.getPeople().get(loginResult.getPersonID()).getFirstName();
                String lastName = mDataCache.getPeople().get(loginResult.getPersonID()).getLastName();

                loginSuccess = true;

                sendSuccessfulMessage(loginSuccess, firstName, lastName);

            } else {
                sendUnsuccessfulMessage(loginSuccess);
            }

        }

        private void sendUnsuccessfulMessage(boolean isSuccess) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(IS_SUCCESS_KEY, isSuccess);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        private void sendSuccessfulMessage(boolean isSuccess, String firstName, String lastName) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(IS_SUCCESS_KEY, isSuccess);
            messageBundle.putString(FIRST_NAME_KEY, firstName);
            messageBundle.putString(LAST_NAME_KEY, lastName);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }

    private class RegisterTask implements Runnable {

        private final Handler messageHandler;
        RegisterRequest mRegisterRequest;
        ServerProxy mServerProxy;

        RegisterTask(Handler registerHandler, RegisterRequest registerRequest, String[] args) {
            this.messageHandler = registerHandler;
            this.mRegisterRequest = registerRequest;
            mServerProxy = new ServerProxy(args);
        }

        @Override
        public void run() {

            RegisterResult registerResult = mServerProxy.register(mRegisterRequest);
            boolean registerSuccess = false;
            // cache events and persons data here if successful register
            if (registerResult != null) {
                Event[] events = mServerProxy.getEvents(registerResult.getAuthtoken()).getData();
                mDataCache.loadEvents(events);

                Person[] persons = mServerProxy.getPersons(registerResult.getAuthtoken()).getData();
                mDataCache.loadPeople(persons);

                for (Person person : persons) {
                    mDataCache.loadEventsForPerson(person, events);
                    mDataCache.loadFamilyForPerson(person, persons);
                }
                registerSuccess = true;
            }

            sendMessage(registerSuccess);

        }

        private void sendMessage(boolean isSuccess) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(IS_SUCCESS_KEY, isSuccess);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }





}