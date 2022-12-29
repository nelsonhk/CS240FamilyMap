package android.bignerdranch.familymapapplication.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.bignerdranch.familymapapplication.R;
import android.os.Bundle;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = getSupportFragmentManager();

        mLoginFragment = new LoginFragment();

        // inserting the loginFragment into the Main Activity layout
        fragmentManager.beginTransaction().add(R.id.MainActivity, mLoginFragment).commit();

    }

}