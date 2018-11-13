package eu.epitech.epicture;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MainActivity extends AppCompatActivity {
    String SHARED_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImgurToken token = getToken();
        if (token != null) {
            Log.e("imgur", "token: " + token.toAuthHeader());
        }
        else
            logout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        clearToken();
        clearBackStack();
        LoginButtonFragment newFragment = new LoginButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, newFragment, "login_button");
        transaction.commit();
    }

    private void saveToken(ImgurToken token) {
        Gson gson = new Gson();
        String authJson = gson.toJson(token);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this );
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SHARED_TOKEN, authJson);
        editor.apply();
    }

    private void clearToken() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(SHARED_TOKEN);
        editor.apply();
    }

    private ImgurToken getToken() {
        ImgurToken token = null;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String authJson = settings.getString(SHARED_TOKEN, null);

        if (!TextUtils.isEmpty(authJson)) {
            Gson gson = new Gson();
            try {
                token = gson.fromJson(authJson, ImgurToken.class);
            } catch (JsonSyntaxException e) {
                Log.e("imgur", "Error while retrieving Token - " + e.toString());
            }
        }
        return token;
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() != 0)
            fm.popBackStackImmediate();
    }
}
